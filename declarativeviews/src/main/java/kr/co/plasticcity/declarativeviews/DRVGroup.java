package kr.co.plasticcity.declarativeviews;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import kr.co.plasticcity.declarativeviews.function.Consumer;
import kr.co.plasticcity.declarativeviews.function.Supplier;
import kr.co.plasticcity.declarativeviews.function.TriConsumer;

/**
 * Created by JongsunYu on 2017-01-03.
 */

class DRVGroup<M, V> implements DRVNotifier, DRVCalculator, Comparable<DRVGroup>
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
	
	private Consumer<V> onCreate;
	private TriConsumer<V, M, DRVBuilder.Position> onFirstBind;
	private TriConsumer<V, M, DRVBuilder.Position> onBind;
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
	
	void setOnFirstBind(@NonNull final TriConsumer<V, M, DRVBuilder.Position> onFirstBind)
	{
		this.onFirstBind = onFirstBind;
	}
	
	void setOnBind(@NonNull final TriConsumer<V, M, DRVBuilder.Position> onBind)
	{
		this.onBind = onBind;
	}
	
	int size()
	{
		return model.size();
	}
	
	void setPosition(int position)
	{
		this.position = position;
	}
	
	@NonNull
	@SuppressWarnings("unchecked")
	V createView(@NonNull final ViewGroup parent)
	{
		final V v;
		if (viewSupplier != null)
		{
			v = viewSupplier.get();
		}
		else if (viewType != null && viewType.getSuperclass() != null && viewType.getSuperclass().equals(ViewDataBinding.class))
		{
			v = (V)DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
		}
		else
		{
			v = (V)LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
		}
		
		if (onCreate != null)
		{
			onCreate.accept(v);
		}
		
		return v;
	}
	
	void onFirstBind(@NonNull final V v, final int pos)
	{
		final int local = pos - this.position;
		final M m = model.get(local);
		if (m != null && onFirstBind != null)
		{
			onFirstBind.accept(v, m, new DRVBuilder.Position(local, pos));
		}
		else if (m != null && onBind != null)
		{
			onBind.accept(v, m, new DRVBuilder.Position(local, pos));
		}
	}
	
	void onBind(@NonNull final V v, final int pos)
	{
		final int local = pos - this.position;
		final M m = model.get(local);
		if (m != null && onBind != null)
		{
			onBind.accept(v, m, new DRVBuilder.Position(local, pos));
		}
	}
	
	@Override
	public void notifyChanged(final int position)
	{
		notifier.notifyChanged(this.position + position);
	}
	
	@Override
	public void notifyInserted(final int position)
	{
		notifier.notifyInserted(this.position + position);
	}
	
	@Override
	public void notifyRemoved(final int position)
	{
		notifier.notifyRemoved(this.position + position);
	}
	
	@Override
	public void notifyRangeChanged(final int start, final int count)
	{
		notifier.notifyRangeChanged(position + start, count);
	}
	
	@Override
	public void notifyRangeInserted(final int start, final int count)
	{
		notifier.notifyRangeInserted(position + start, count);
	}
	
	@Override
	public void notifyRangeRemoved(final int start, final int count)
	{
		notifier.notifyRangeRemoved(position + start, count);
	}
	
	@Override
	public void notifyMoved(final int from, final int to)
	{
		notifier.notifyMoved(position + from, position + to);
	}
	
	@Override
	public int getPositionInList(final int positionInGroup)
	{
		return this.position + positionInGroup;
	}
	
	@Override
	public int compareTo(@NonNull DRVGroup another)
	{
		return this.position - another.position;
	}
}