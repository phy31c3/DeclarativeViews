package kr.co.plasticcity.declarativeviews;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.plasticcity.declarativeviews.function.BiConsumer;
import kr.co.plasticcity.declarativeviews.function.Consumer;
import kr.co.plasticcity.declarativeviews.function.Supplier;
import kr.co.plasticcity.declarativeviews.function.TriConsumer;

/**
 * Created by JongsunYu on 2017-01-03.
 */

class DRVGroup<M, V> implements DRVNotifier, Comparable<DRVGroup>
{
	@NonNull
	private final List<M> model;
	@NonNull
	private final DRVNotifier notifier;
	@Nullable
	private final Supplier<V> viewSupplier;
	@Nullable
	private final Class<V> viewType;
	private final int layoutResId;
	
	@Nullable
	private Consumer<V> onCreate;
	@Nullable
	private TriConsumer<V, M, ItemPosition> onFirstBind;
	@Nullable
	private TriConsumer<V, M, ItemPosition> onBind;
	@Nullable
	private Placeholder placeholder;
	@Nullable
	private DRVDivider.Creator dividerCreator;
	private boolean isFooter;
	private int position;
	
	DRVGroup(@NonNull final List<M> model, @NonNull final DRVNotifier notifier, final int layoutResId, @NonNull final Class<V> viewType)
	{
		this.model = model;
		this.notifier = notifier;
		this.viewSupplier = null;
		this.viewType = viewType;
		this.layoutResId = layoutResId;
	}
	
	DRVGroup(@NonNull final List<M> model, @NonNull final DRVNotifier notifier, @NonNull final Supplier<V> supplier)
	{
		this.model = model;
		this.notifier = notifier;
		this.viewSupplier = supplier;
		this.viewType = null;
		this.layoutResId = 0;
	}
	
	void setOnCreate(@NonNull final Consumer<V> onCreate)
	{
		this.onCreate = onCreate;
	}
	
	void setOnFirstBind(@NonNull final TriConsumer<V, M, ItemPosition> onFirstBind)
	{
		this.onFirstBind = onFirstBind;
	}
	
	void setOnBind(@NonNull final TriConsumer<V, M, ItemPosition> onBind)
	{
		this.onBind = onBind;
	}
	
	void setDividerCreator(@NonNull final DRVDivider.Creator dividerCreator)
	{
		this.dividerCreator = dividerCreator;
	}
	
	void setPlaceholder(final int count, @NonNull final BiConsumer<V, ItemPosition> onPlaceholderBind)
	{
		this.placeholder = new Placeholder(count, onPlaceholderBind);
	}
	
	void setFooter()
	{
		isFooter = true;
	}
	
	void setPosition(int position)
	{
		this.position = position;
		if (placeholder != null)
		{
			placeholder.reset();
		}
	}
	
	int size()
	{
		if (placeholder != null && model.size() < placeholder.count)
		{
			return placeholder.count;
		}
		else
		{
			return model.size();
		}
	}
	
	int getPositionInList(final int positionInGroup)
	{
		return this.position + positionInGroup;
	}
	
	@NonNull
	@SuppressWarnings("unchecked")
	Pair<View, V> createView(@NonNull final ViewGroup parent)
	{
		final V v;
		final View view;
		if (viewSupplier != null)
		{
			v = viewSupplier.get();
			if (v.getClass().getSuperclass() != null && v.getClass().getSuperclass().equals(ViewDataBinding.class))
			{
				view = ((ViewDataBinding)v).getRoot();
			}
			else
			{
				view = (View)v;
			}
		}
		else
		{
			if (viewType != null && viewType.getSuperclass() != null && viewType.getSuperclass().equals(ViewDataBinding.class))
			{
				v = (V)DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
				view = ((ViewDataBinding)v).getRoot();
			}
			else
			{
				v = (V)LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
				view = (View)v;
			}
		}
		
		if (dividerCreator != null)
		{
			view.setTag(ViewTag.DIVIDER, dividerCreator.create(view.getContext()));
		}
		view.setTag(ViewTag.IS_FOOTER, isFooter);
		
		if (onCreate != null)
		{
			onCreate.accept(v);
		}
		
		return new Pair<>(view, v);
	}
	
	boolean hasOnFirstBind()
	{
		return onFirstBind != null;
	}
	
	/**
	 * @return if true, onPlaceholderBind was bound, not onFirstBind
	 */
	boolean onFirstBind(@NonNull final V v, @NonNull final View view, final int pos)
	{
		return performBind(onFirstBind, v, view, pos);
	}
	
	void onBind(@NonNull final V v, @NonNull final View view, final int pos)
	{
		performBind(onBind, v, view, pos);
	}
	
	private boolean performBind(@Nullable final TriConsumer<V, M, ItemPosition> bindFunc, @NonNull final V v, @NonNull final View view, final int pos)
	{
		final int local = pos - this.position;
		final ItemPosition itemPosition = new ItemPosition(local, pos);
		final DRVDivider divider = (DRVDivider)view.getTag(ViewTag.DIVIDER);
		
		if (divider != null)
		{
			divider.setLast(local == size() - 1);
		}
		
		if (placeholder != null)
		{
			if (local < model.size())
			{
				if (local < placeholder.count)
				{
					placeholder.modelPlaced.set(local, true);
				}
				final M m = model.get(local);
				if (m != null && bindFunc != null)
				{
					bindFunc.accept(v, m, itemPosition);
				}
				return false;
			}
			else
			{
				placeholder.modelPlaced.set(local, false);
				placeholder.onBind.accept(v, itemPosition);
				return true;
			}
		}
		else
		{
			final M m = model.get(local);
			if (m != null && bindFunc != null)
			{
				bindFunc.accept(v, m, itemPosition);
			}
			return false;
		}
	}
	
	@Override
	public void notifyChanged(final int position)
	{
		notifier.notifyChanged(this.position + position);
	}
	
	@Override
	public void notifyChangedWithNoAnimation(final int position)
	{
		notifier.notifyChangedWithNoAnimation(this.position + position);
	}
	
	@Override
	public void notifyInserted(final int position)
	{
		if (placeholder != null && model.size() <= placeholder.count)
		{
			notifier.notifyRangeChanged(this.position + position, model.size() - position);
			placeholder.modelPlaced.set(model.size() - 1, true);
		}
		else
		{
			notifier.notifyInserted(this.position + position);
		}
		
		if (dividerCreator != null && dividerCreator.isExcludeLast() && position > 0 && position == size() - 1)
		{
			notifier.notifyChangedWithNoAnimation(this.position + position - 1);
		}
	}
	
	@Override
	public void notifyRemoved(final int position)
	{
		if (placeholder != null && model.size() < placeholder.count)
		{
			notifier.notifyRangeChanged(this.position + position, model.size() - position + 1);
			placeholder.modelPlaced.set(model.size(), false);
		}
		else
		{
			notifier.notifyRemoved(this.position + position);
		}
		
		if (dividerCreator != null && dividerCreator.isExcludeLast() && position > 0 && position == size())
		{
			notifier.notifyChangedWithNoAnimation(this.position + position - 1);
		}
	}
	
	@Override
	public void notifyRangeChanged(final int start, final int count)
	{
		notifier.notifyRangeChanged(position + start, count);
	}
	
	@Override
	public void notifyRangeInserted(final int start, final int count)
	{
		final int beforeSize = model.size() - count;
		if (placeholder != null && beforeSize < placeholder.count)
		{
			final int overflow = model.size() - placeholder.count;
			final int placedSize = overflow < 0 ? model.size() : placeholder.count;
			
			notifier.notifyRangeChanged(this.position + start, placedSize - start);
			for (int i = beforeSize ; i < placedSize ; ++i)
			{
				placeholder.modelPlaced.set(i, true);
			}
			
			if (overflow > 0)
			{
				notifier.notifyRangeInserted(position + placeholder.count, overflow);
			}
		}
		else
		{
			notifier.notifyRangeInserted(position + start, count);
		}
		
		if (dividerCreator != null && dividerCreator.isExcludeLast() && start > 0 && start == beforeSize)
		{
			notifier.notifyChangedWithNoAnimation(this.position + start - 1);
		}
	}
	
	@Override
	public void notifyRangeRemoved(final int start, final int count)
	{
		if (placeholder != null && model.size() < placeholder.count)
		{
			final int beforeSize = model.size() + count;
			final int overflow = beforeSize - placeholder.count;
			final int placedSize = overflow < 0 ? beforeSize : placeholder.count;
			
			if (overflow > 0)
			{
				notifier.notifyRangeRemoved(position + placeholder.count, overflow);
			}
			
			notifier.notifyRangeChanged(this.position + start, placedSize - start);
			for (int i = model.size() ; i < placedSize ; ++i)
			{
				placeholder.modelPlaced.set(i, false);
			}
		}
		else
		{
			notifier.notifyRangeRemoved(position + start, count);
		}
		
		if (dividerCreator != null && dividerCreator.isExcludeLast() && start > 0 && start == model.size())
		{
			notifier.notifyChangedWithNoAnimation(this.position + start - 1);
		}
	}
	
	@Override
	public void notifyMoved(final int from, final int to)
	{
		if (dividerCreator != null && dividerCreator.isExcludeLast() && (from == size() - 1 || to == size() - 1))
		{
			notifier.notifyChangedWithNoAnimation(this.position + from);
			notifier.notifyChangedWithNoAnimation(this.position + to);
		}
		
		notifier.notifyMoved(position + from, position + to);
	}
	
	@Override
	public int compareTo(@NonNull DRVGroup another)
	{
		return this.position - another.position;
	}
	
	private class Placeholder
	{
		private final int count;
		@NonNull
		private final BiConsumer<V, ItemPosition> onBind;
		@NonNull
		private final ArrayList<Boolean> modelPlaced;
		
		private Placeholder(final int count, @NonNull final BiConsumer<V, ItemPosition> onBind)
		{
			this.count = count;
			this.onBind = onBind;
			this.modelPlaced = new ArrayList<>(count);
			reset();
		}
		
		private void reset()
		{
			modelPlaced.clear();
			for (int i = 0 ; i < count ; ++i)
			{
				modelPlaced.add(false);
			}
		}
	}
}