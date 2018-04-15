package kr.co.plasticcity.declarativeviews;

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
 * Created by JongsunYu on 2018-04-02.
 */

class DLLBuilderImpl implements DLLBuilder.Buildable
{
	@NonNull
	private final Consumer<DLLAdapter> applier;
	@NonNull
	private final DLLAdapter adapter;
	
	DLLBuilderImpl(@NonNull final DLLView view, @NonNull final Consumer<DLLAdapter> applier)
	{
		this.applier = applier;
		this.adapter = new DLLAdapter(view);
	}
	
	@Override
	public <M> GroupAdder<M, View> addGroup(@Nullable M model, int layoutResId)
	{
		return addGroup(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@Nullable final M model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		return new GroupAdderImpl<>(new DRVGroup<>(Collections.singletonList(model), adapter, layoutResId, viewType));
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@Nullable final M model, @NonNull final Supplier<V> supplier)
	{
		return new GroupAdderImpl<>(new DRVGroup<>(Collections.singletonList(model), adapter, supplier));
	}
	
	@Override
	public <M> GroupAdder<M, View> addGroup(@NonNull final SingleModel<M> model, final int layoutResId)
	{
		return addGroup(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@NonNull final SingleModel<M> model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, layoutResId, viewType);
			m.init(group, group);
			return new GroupAdderImpl<>(group);
		}
		else
		{
			Log.w("DeclarativeLinearLayout", "Using an abnormal SingleModel. Use 'SingleModel.of(M m)' of 'SingleModel.empty()'");
			return new GroupAdderImpl<>(new DRVGroup<>(Collections.singletonList(model.get()), adapter, layoutResId, viewType));
		}
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@NonNull final SingleModel<M> model, @NonNull final Supplier<V> supplier)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, supplier);
			m.init(group, group);
			return new GroupAdderImpl<>(group);
		}
		else
		{
			Log.w("DeclarativeLinearLayout", "Using an abnormal SingleModel. Use 'SingleModel.of(M m)' of 'SingleModel.empty()'");
			return new GroupAdderImpl<>(new DRVGroup<>(Collections.singletonList(model.get()), adapter, supplier));
		}
	}
	
	@Override
	public <M> GroupAdder<M, View> addGroup(@NonNull List<M> model, int layoutResId)
	{
		return addGroup(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, layoutResId, viewType);
			m.init(group, group);
			return new GroupAdderImpl<>(group);
		}
		else
		{
			if (model instanceof ListModel)
			{
				Log.w("DeclarativeLinearLayout", "Using an abnormal ListModel. Use 'ListModel.of(Collection<M> m)' or 'ListModel.empty()'");
			}
			return new GroupAdderImpl<>(new DRVGroup<>(model, adapter, layoutResId, viewType));
		}
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, @NonNull final Supplier<V> supplier)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, supplier);
			m.init(group, group);
			return new GroupAdderImpl<>(group);
		}
		else
		{
			if (model instanceof ListModel)
			{
				Log.w("DeclarativeLinearLayout", "Using an abnormal ListModel. Use 'ListModel.of(Collection<M> m)' or 'ListModel.empty()'");
			}
			return new GroupAdderImpl<>(new DRVGroup<>(model, adapter, supplier));
		}
	}
	
	@Override
	public void build()
	{
		applier.accept(adapter);
	}
	
	private class GroupAdderImpl<M, V> implements GroupAdder<M, V>
	{
		@NonNull
		private final DRVGroup<M, V> group;
		
		private GroupAdderImpl(@NonNull final DRVGroup<M, V> group)
		{
			this.group = group;
		}
		
		@Override
		public GroupAdder<M, V> onCreate(@NonNull final BiConsumer<V, M> func)
		{
			group.setOnFirstBind((m, v, position) -> func.accept(m, v));
			return this;
		}
		
		@Override
		public GroupAdder<M, V> onCreate(@NonNull final TriConsumer<V, M, ItemPosition> func)
		{
			group.setOnFirstBind(func);
			return this;
		}
		
		@Override
		public GroupAdder<M, V> onModelChanged(@NonNull final BiConsumer<V, M> func)
		{
			group.setOnBind((m, v, position) -> func.accept(m, v));
			return this;
		}
		
		@Override
		public GroupAdder<M, V> onModelChanged(@NonNull final TriConsumer<V, M, ItemPosition> func)
		{
			group.setOnBind(func);
			return this;
		}
		
		@Override
		public Buildable apply()
		{
			adapter.addGroup(group);
			return DLLBuilderImpl.this;
		}
	}
}
