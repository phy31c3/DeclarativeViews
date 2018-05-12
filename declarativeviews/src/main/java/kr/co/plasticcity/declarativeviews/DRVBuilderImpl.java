package kr.co.plasticcity.declarativeviews;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.Collections;
import java.util.List;

import kr.co.plasticcity.declarativeviews.function.BiConsumer;
import kr.co.plasticcity.declarativeviews.function.Consumer;
import kr.co.plasticcity.declarativeviews.function.Supplier;
import kr.co.plasticcity.declarativeviews.function.TriConsumer;

/**
 * Created by JongsunYu on 2017-01-03.
 */

class DRVBuilderImpl implements DRVBuilder.Definable
{
	@NonNull
	private final BiConsumer<DRVAdapter, Boolean> applier;
	@NonNull
	private final DRVAdapter adapter;
	private boolean hasFooter;
	
	DRVBuilderImpl(@NonNull final BiConsumer<DRVAdapter, Boolean> applier)
	{
		this.applier = applier;
		this.adapter = new DRVAdapter();
		this.hasFooter = false;
	}
	
	@Override
	public <M> SingleGroupAdder<M, View, Definable> addGroup(@Nullable M model, int layoutResId)
	{
		return addGroup(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V, Definable> addGroup(@Nullable final M model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model), adapter, layoutResId, viewType);
		return new GroupAdderImpl<>(group, this);
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V, Definable> addGroup(@Nullable final M model, @NonNull final Supplier<V> supplier)
	{
		final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model), adapter, supplier);
		return new GroupAdderImpl<>(group, this);
	}
	
	@Override
	public <M> SingleGroupAdder<M, View, Definable> addGroup(@NonNull final SingleModel<M> model, final int layoutResId)
	{
		return addGroup(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V, Definable> addGroup(@NonNull final SingleModel<M> model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, layoutResId, viewType);
			m.init(group, group);
			return new GroupAdderImpl<>(group, this);
		}
		else
		{
			Log.w("DeclarativeRecyclerView", "Using an abnormal SingleModel. Use 'SingleModel.of(M m)' of 'SingleModel.empty()'");
			final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model.get()), adapter, layoutResId, viewType);
			return new GroupAdderImpl<>(group, this);
		}
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V, Definable> addGroup(@NonNull final SingleModel<M> model, @NonNull final Supplier<V> supplier)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, supplier);
			m.init(group, group);
			return new GroupAdderImpl<>(group, this);
		}
		else
		{
			Log.w("DeclarativeRecyclerView", "Using an abnormal SingleModel. Use 'SingleModel.of(M m)' of 'SingleModel.empty()'");
			final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model.get()), adapter, supplier);
			return new GroupAdderImpl<>(group, this);
		}
	}
	
	@Override
	public <M> GroupAdder<M, View, Definable> addGroup(@NonNull List<M> model, int layoutResId)
	{
		return addGroup(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> GroupAdder<M, V, Definable> addGroup(@NonNull final List<M> model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, layoutResId, viewType);
			m.init(group, group);
			return new GroupAdderImpl<>(group, this);
		}
		else
		{
			if (model instanceof ListModel)
			{
				Log.w("DeclarativeRecyclerView", "Using an abnormal ListModel. Use 'ListModel.of(Collection<M> m)' or 'ListModel.empty()'");
			}
			final DRVGroup<M, V> group = new DRVGroup<>(model, adapter, layoutResId, viewType);
			return new GroupAdderImpl<>(group, this);
		}
	}
	
	@Override
	public <M, V> GroupAdder<M, V, Definable> addGroup(@NonNull final List<M> model, @NonNull final Supplier<V> supplier)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, supplier);
			m.init(group, group);
			return new GroupAdderImpl<>(group, this);
		}
		else
		{
			if (model instanceof ListModel)
			{
				Log.w("DeclarativeRecyclerView", "Using an abnormal ListModel. Use 'ListModel.of(Collection<M> m)' or 'ListModel.empty()'");
			}
			final DRVGroup<M, V> group = new DRVGroup<>(model, adapter, supplier);
			return new GroupAdderImpl<>(group, this);
		}
	}
	
	@Override
	public <M> SingleGroupAdder<M, View, Buildable> addFooter(@NonNull final M model, final int layoutResId)
	{
		return addFooter(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V, Buildable> addFooter(@NonNull final M model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		hasFooter = true;
		final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model), adapter, layoutResId, viewType);
		group.setFooter();
		return new GroupAdderImpl<>(group, this);
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V, Buildable> addFooter(@NonNull final M model, @NonNull final Supplier<V> supplier)
	{
		hasFooter = true;
		final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model), adapter, supplier);
		group.setFooter();
		return new GroupAdderImpl<>(group, this);
	}
	
	@Override
	public <M> SingleGroupAdder<M, View, Buildable> addFooter(@NonNull final SingleModel<M> model, final int layoutResId)
	{
		return addFooter(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V, Buildable> addFooter(@NonNull final SingleModel<M> model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		hasFooter = true;
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, layoutResId, viewType);
			group.setFooter();
			m.init(group, group);
			return new GroupAdderImpl<>(group, this);
		}
		else
		{
			Log.w("DeclarativeRecyclerView", "Using an abnormal SingleModel. Use 'SingleModel.of(M m)' of 'SingleModel.empty()'");
			final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model.get()), adapter, layoutResId, viewType);
			group.setFooter();
			return new GroupAdderImpl<>(group, this);
		}
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V, Buildable> addFooter(@NonNull final SingleModel<M> model, @NonNull final Supplier<V> supplier)
	{
		hasFooter = true;
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, supplier);
			group.setFooter();
			m.init(group, group);
			return new GroupAdderImpl<>(group, this);
		}
		else
		{
			Log.w("DeclarativeRecyclerView", "Using an abnormal SingleModel. Use 'SingleModel.of(M m)' of 'SingleModel.empty()'");
			final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model.get()), adapter, supplier);
			group.setFooter();
			return new GroupAdderImpl<>(group, this);
		}
	}
	
	@Override
	public void build()
	{
		applier.accept(adapter, hasFooter);
	}
	
	@Override
	public void buildOnUiThread()
	{
		new Handler(Looper.getMainLooper()).post(this::build);
	}
	
	@Override
	public void buildOnUiThread(@NonNull final Runnable onBuildFinished)
	{
		new Handler(Looper.getMainLooper()).post(() ->
		{
			build();
			onBuildFinished.run();
		});
	}
	
	private class GroupAdderImpl<M, V, R> implements SingleGroupAdder<M, V, R>
	{
		@NonNull
		private final DRVGroup<M, V> group;
		@NonNull
		private final R r;
		
		private GroupAdderImpl(@NonNull final DRVGroup<M, V> group, @NonNull final R r)
		{
			this.group = group;
			this.r = r;
		}
		
		@Override
		public SingleGroupAdder<M, V, R> onCreate(@NonNull final Consumer<V> func)
		{
			group.setOnCreate(func);
			return this;
		}
		
		@Override
		public GroupAdder<M, V, R> onFistBind(@NonNull final BiConsumer<V, M> func)
		{
			group.setOnFirstBind((v, m, position) -> func.accept(v, m));
			return this;
		}
		
		@Override
		public GroupAdder<M, V, R> onFistBind(@NonNull final TriConsumer<V, M, ItemPosition> func)
		{
			group.setOnFirstBind(func);
			return this;
		}
		
		@Override
		public GroupAdder<M, V, R> onBind(@NonNull final BiConsumer<V, M> func)
		{
			group.setOnBind((v, m, position) -> func.accept(v, m));
			return this;
		}
		
		@Override
		public GroupAdder<M, V, R> onBind(@NonNull final TriConsumer<V, M, ItemPosition> func)
		{
			group.setOnBind(func);
			return this;
		}
		
		@Override
		public R apply()
		{
			adapter.addGroup(group);
			return r;
		}
	}
}