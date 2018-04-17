package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import kr.co.plasticcity.declarativeviews.function.BiFunction;
import kr.co.plasticcity.declarativeviews.function.Function;

/**
 * Created by JongsunYu on 2017-12-27.
 */

class DRVModel<M> implements SingleModel<M>, ListModel<M>
{
	@NonNull
	private final List<M> list;
	@NonNull
	private DRVNotifier notifier;
	@NonNull
	private DRVCalculator calculator;
	
	DRVModel()
	{
		this.list = new ArrayList<>();
		this.notifier = DRVNotifier.empty();
		this.calculator = DRVCalculator.empty();
	}
	
	DRVModel(@NonNull final List<M> l)
	{
		this.list = l;
		this.notifier = DRVNotifier.empty();
		this.calculator = DRVCalculator.empty();
	}
	
	void init(@NonNull final DRVNotifier notifier, @NonNull final DRVCalculator calculator)
	{
		this.notifier = notifier;
		this.calculator = calculator;
	}
	
	/////////////////////////////////////////////////////////////////////////
	// from SingleModel
	/////////////////////////////////////////////////////////////////////////
	@Nullable
	@Override
	public M get()
	{
		return list.isEmpty() ? null : list.get(0);
	}
	
	@Override
	public void set(@NonNull final M m)
	{
		if (list.isEmpty())
		{
			list.add(m);
			notifier.notifyInserted(0);
		}
		else if (!m.equals(list.set(0, m)))
		{
			notifier.notifyChanged(0);
		}
	}
	
	@Override
	public void replace(@NonNull final Function<M, M> f)
	{
		if (!list.isEmpty())
		{
			final M old = list.get(0);
			if (old != null)
			{
				final M neW = f.apply(old);
				if (!neW.equals(list.set(0, neW)))
				{
					notifier.notifyChanged(0);
				}
			}
		}
	}
	
	@Override
	public void remove()
	{
		if (!list.isEmpty())
		{
			list.remove(0);
			notifier.notifyRemoved(0);
		}
	}
	
	@Override
	public int getPositionInList()
	{
		return calculator.getPositionInList(0);
	}
	
	@Override
	public void performChanged()
	{
		notifier.notifyChanged(0);
	}
	
	/////////////////////////////////////////////////////////////////////////
	// from ListModel
	/////////////////////////////////////////////////////////////////////////
	
	@Override
	public void replace(final int index, @NonNull final Function<M, M> f)
	{
		if (index >= 0 && index < list.size())
		{
			final M old = list.get(index);
			if (old != null)
			{
				final M neW = f.apply(old);
				if (!neW.equals(list.set(index, neW)))
				{
					notifier.notifyChanged(index);
				}
			}
		}
	}
	
	@Override
	public void replaceRange(final int start, final int end, @NonNull final BiFunction<M, Integer, M> f)
	{
		if (start <= end && start >= 0 && end < list.size())
		{
			final List<Range> ranges = new ArrayList<>();
			Range range = null;
			for (int i = start ; i <= end ; ++i)
			{
				final M old = list.get(i);
				if (old != null)
				{
					final M neW = f.apply(old, i);
					if (!neW.equals(list.set(i, neW)))
					{
						if (range != null && range.isNext(i))
						{
							range.increase();
						}
						else
						{
							range = new Range(i);
							ranges.add(range);
						}
					}
				}
			}
			for (final Range r : ranges)
			{
				notifier.notifyRangeChanged(r.start, r.count);
			}
		}
	}
	
	@Override
	public void move(final int from, final int to)
	{
		if (from != to && from >= 0 && from < list.size() && to >= 0 && to < list.size())
		{
			list.add(to, list.remove(from));
			notifier.notifyMoved(from, to);
		}
	}
	
	@Override
	public void update(@NonNull final List<M> l)
	{
		try
		{
			int pin = 0;
			List<M> add = new ArrayList<>();
			for (int i = 0 ; i < l.size() ; ++i)
			{
				final M cur = l.get(i);
				final int mark = indexOf(list, cur, pin);
				if (mark >= 0)
				{
					if (mark > pin)
					{
						list.removeAll(list.subList(pin, mark));
						notifier.notifyRangeRemoved(pin, mark - pin);
					}
					if (add.size() > 0)
					{
						list.addAll(pin, add);
						notifier.notifyRangeInserted(pin, add.size());
						pin += add.size();
						add = new ArrayList<>();
					}
					if (!cur.equals(list.set(pin, cur)))
					{
						notifier.notifyChanged(pin);
					}
					++pin;
				}
				else
				{
					add.add(cur);
				}
			}
			if (pin < list.size())
			{
				final int count = list.size() - pin;
				list.removeAll(list.subList(pin, list.size()));
				notifier.notifyRangeRemoved(pin, count);
			}
			if (add.size() > 0)
			{
				list.addAll(pin, add);
				notifier.notifyRangeInserted(pin, add.size());
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			Log.e("DeclarativeRecyclerView", "IndexOutOfBoundsException occurred during update. Check the rule of update method by javadoc.");
			throw e;
		}
		catch (ClassCastException e)
		{
			Log.e("DeclarativeRecyclerView", "ClassCastException occurred during update. The model M must implement IdComparable<M>, not IdComparable<Other>");
			throw e;
		}
	}
	
	@Override
	public int getPositionInList(final int index)
	{
		return calculator.getPositionInList(index);
	}
	
	@Override
	public int getPositionInList(@NonNull final M m)
	{
		return calculator.getPositionInList(list.indexOf(m));
	}
	
	@Override
	public void performChanged(final int index)
	{
		if (index >= 0 && index < list.size())
		{
			notifier.notifyChanged(index);
		}
	}
	
	@Override
	public void performRangeChanged(final int start, final int end)
	{
		if (start <= end && start >= 0 && end < list.size())
		{
			notifier.notifyRangeChanged(start, end - start + 1);
		}
	}
	
	@Override
	public void performAllChanged()
	{
		notifier.notifyRangeChanged(0, list.size());
	}
	
	/////////////////////////////////////////////////////////////////////////
	// from List<M>
	/////////////////////////////////////////////////////////////////////////
	@Override
	public int size()
	{
		return list.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}
	
	@Override
	public boolean contains(final Object o)
	{
		return list.contains(o);
	}
	
	@NonNull
	@Override
	public Iterator<M> iterator()
	{
		return list.iterator();
	}
	
	@NonNull
	@Override
	public Object[] toArray()
	{
		return list.toArray();
	}
	
	@NonNull
	@Override
	@SuppressWarnings("SuspiciousToArrayCall")
	public <T> T[] toArray(@NonNull final T[] a)
	{
		return list.toArray(a);
	}
	
	@Override
	public boolean add(final M m)
	{
		final int position = list.size();
		list.add(m);
		notifier.notifyInserted(position);
		return true;
	}
	
	@Override
	public boolean remove(final Object o)
	{
		@SuppressWarnings("SuspiciousMethodCalls")
		final int position = list.indexOf(o);
		final boolean modified = list.remove(o);
		if (modified && position != -1)
		{
			notifier.notifyRemoved(position);
		}
		return modified;
	}
	
	@Override
	public boolean containsAll(@NonNull final Collection<?> c)
	{
		return list.containsAll(c);
	}
	
	@Override
	public boolean addAll(@NonNull final Collection<? extends M> c)
	{
		final int start = list.size();
		final boolean modified = list.addAll(c);
		if (modified)
		{
			notifier.notifyRangeInserted(start, c.size());
		}
		return modified;
	}
	
	@Override
	public boolean addAll(final int index, @NonNull final Collection<? extends M> c)
	{
		final boolean modified = list.addAll(index, c);
		if (modified)
		{
			notifier.notifyRangeInserted(index, c.size());
		}
		return modified;
	}
	
	@Override
	public boolean removeAll(@NonNull final Collection<?> c)
	{
		final List<Range> ranges = new ArrayList<>();
		Range range = null;
		int removed = 0;
		for (int i = 0 ; i < list.size() ; ++i)
		{
			if (c.contains(list.get(i)))
			{
				if (range == null)
				{
					range = new Range(i);
					ranges.add(range);
				}
				else if (range.isNext(i))
				{
					range.increase();
				}
				else
				{
					removed += range.count;
					range = new Range(i, removed);
					ranges.add(range);
				}
			}
		}
		final boolean modified = list.removeAll(c);
		if (modified)
		{
			for (final Range r : ranges)
			{
				notifier.notifyRangeRemoved(r.start, r.count);
			}
		}
		return modified;
	}
	
	@Override
	public boolean retainAll(@NonNull final Collection<?> c)
	{
		final List<Range> ranges = new ArrayList<>();
		Range range = null;
		int removed = 0;
		for (int i = 0 ; i < list.size() ; ++i)
		{
			if (!c.contains(list.get(i)))
			{
				if (range == null)
				{
					range = new Range(i);
					ranges.add(range);
				}
				else if (range.isNext(i))
				{
					range.increase();
				}
				else
				{
					removed += range.count;
					range = new Range(i, removed);
					ranges.add(range);
				}
			}
		}
		final boolean modified = list.retainAll(c);
		if (modified)
		{
			for (final Range r : ranges)
			{
				notifier.notifyRangeRemoved(r.start, r.count);
			}
		}
		return modified;
	}
	
	@Override
	public void clear()
	{
		final int size = list.size();
		list.clear();
		notifier.notifyRangeRemoved(0, size);
	}
	
	@Override
	public M get(final int index)
	{
		return list.get(index);
	}
	
	@Override
	public M set(final int index, @NonNull final M element)
	{
		final M old = list.set(index, element);
		if (!element.equals(old))
		{
			notifier.notifyChanged(index);
		}
		return old;
	}
	
	@Override
	public void add(final int index, final M element)
	{
		list.add(index, element);
		notifier.notifyInserted(index);
	}
	
	@Override
	public M remove(final int index)
	{
		final M removed = list.remove(index);
		notifier.notifyRemoved(index);
		return removed;
	}
	
	@Override
	public int indexOf(final Object o)
	{
		return list.indexOf(o);
	}
	
	@Override
	public int lastIndexOf(final Object o)
	{
		return list.lastIndexOf(o);
	}
	
	@NonNull
	@Override
	public ListIterator<M> listIterator()
	{
		return list.listIterator();
	}
	
	@NonNull
	@Override
	public ListIterator<M> listIterator(final int index)
	{
		return list.listIterator(index);
	}
	
	@NonNull
	@Override
	public List<M> subList(final int fromIndex, final int toIndex)
	{
		return list.subList(fromIndex, toIndex);
	}
	
	@SuppressWarnings("unchecked")
	private int indexOf(@NonNull final List<M> list, @NonNull final M element, final int from)
	{
		if (element instanceof IdComparable)
		{
			final IdComparable<M> comp = (IdComparable)element;
			for (int i = from ; i < list.size() ; ++i)
			{
				if (comp.equalId(list.get(i)))
				{
					return i;
				}
			}
			return -1;
		}
		else
		{
			for (int i = from ; i < list.size() ; ++i)
			{
				if (element.equals(list.get(i)))
				{
					return i;
				}
			}
			return -1;
		}
	}
	
	private class Range
	{
		private final int space;
		private int start;
		private int end;
		private int count;
		
		private Range(final int start)
		{
			this.space = 0;
			this.start = start;
			this.end = start;
			this.count = 1;
		}
		
		private Range(final int start, final int space)
		{
			this.space = space;
			this.start = start - space;
			this.end = start - space;
			this.count = 1;
		}
		
		private boolean isNext(final int next)
		{
			return next == this.end + space + 1;
		}
		
		private void increase()
		{
			this.end++;
			this.count++;
		}
	}
}
