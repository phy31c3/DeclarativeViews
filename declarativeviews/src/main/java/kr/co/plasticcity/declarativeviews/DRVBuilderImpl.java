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
	private final Consumer<DRVAdapter> applier;
	@NonNull
	private final DRVAdapter adapter;
	
	DRVBuilderImpl(@NonNull final Consumer<DRVAdapter> applier)
	{
		this.applier = applier;
		this.adapter = new DRVAdapter();
	}
	
	@Override
	public <M> SingleGroupAdder<M, View> addGroup(@Nullable M model, int layoutResId)
	{
		return addGroup(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V> addGroup(@Nullable final M model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model), adapter, adapter::getItemCount, layoutResId, viewType);
		return new GroupAdderImpl<>(group);
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V> addGroup(@Nullable final M model, @NonNull final Supplier<V> supplier)
	{
		final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model), adapter, adapter::getItemCount, supplier);
		return new GroupAdderImpl<>(group);
	}
	
	@Override
	public <M> SingleGroupAdder<M, View> addGroup(@NonNull final SingleModel<M> model, final int layoutResId)
	{
		return addGroup(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V> addGroup(@NonNull final SingleModel<M> model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, adapter::getItemCount, layoutResId, viewType);
			m.init(group, group::getPositionInList);
			return new GroupAdderImpl<>(group);
		}
		else
		{
			Log.w("DeclarativeRecyclerView", "Using an abnormal SingleModel. Use 'SingleModel.of(M m)' of 'SingleModel.empty()'");
			final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model.get()), adapter, adapter::getItemCount, layoutResId, viewType);
			return new GroupAdderImpl<>(group);
		}
	}
	
	@Override
	public <M, V> SingleGroupAdder<M, V> addGroup(@NonNull final SingleModel<M> model, @NonNull final Supplier<V> supplier)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, adapter::getItemCount, supplier);
			m.init(group, group::getPositionInList);
			return new GroupAdderImpl<>(group);
		}
		else
		{
			Log.w("DeclarativeRecyclerView", "Using an abnormal SingleModel. Use 'SingleModel.of(M m)' of 'SingleModel.empty()'");
			final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model.get()), adapter, adapter::getItemCount, supplier);
			return new GroupAdderImpl<>(group);
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
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, adapter::getItemCount, layoutResId, viewType);
			m.init(group, group::getPositionInList);
			return new GroupAdderImpl<>(group);
		}
		else
		{
			if (model instanceof ListModel)
			{
				Log.w("DeclarativeRecyclerView", "Using an abnormal ListModel. Use 'ListModel.of(Collection<M> m)' or 'ListModel.empty()'");
			}
			final DRVGroup<M, V> group = new DRVGroup<>(model, adapter, adapter::getItemCount, layoutResId, viewType);
			return new GroupAdderImpl<>(group);
		}
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, @NonNull final Supplier<V> supplier)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, adapter::getItemCount, supplier);
			m.init(group, group::getPositionInList);
			return new GroupAdderImpl<>(group);
		}
		else
		{
			if (model instanceof ListModel)
			{
				Log.w("DeclarativeRecyclerView", "Using an abnormal ListModel. Use 'ListModel.of(Collection<M> m)' or 'ListModel.empty()'");
			}
			final DRVGroup<M, V> group = new DRVGroup<>(model, adapter, adapter::getItemCount, supplier);
			return new GroupAdderImpl<>(group);
		}
	}
	
	@Override
	public <M> FooterAdder<M, View> addFooter(@NonNull final M model, final int layoutResId)
	{
		return addFooter(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> FooterAdder<M, V> addFooter(@NonNull final M model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model), adapter, adapter::getItemCount, layoutResId, viewType);
		return new FooterAdderImpl<>(group);
	}
	
	@Override
	public <M, V> FooterAdder<M, V> addFooter(@NonNull final M model, @NonNull final Supplier<V> supplier)
	{
		final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model), adapter, adapter::getItemCount, supplier);
		return new FooterAdderImpl<>(group);
	}
	
	@Override
	public <M> FooterAdder<M, View> addFooter(@NonNull final SingleModel<M> model, final int layoutResId)
	{
		return addFooter(model, layoutResId, View.class);
	}
	
	@Override
	public <M, V> FooterAdder<M, V> addFooter(@NonNull final SingleModel<M> model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, adapter::getItemCount, layoutResId, viewType);
			m.init(group, group::getPositionInList);
			return new FooterAdderImpl<>(group);
		}
		else
		{
			Log.w("DeclarativeRecyclerView", "Using an abnormal SingleModel. Use 'SingleModel.of(M m)' of 'SingleModel.empty()'");
			final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model.get()), adapter, adapter::getItemCount, layoutResId, viewType);
			return new FooterAdderImpl<>(group);
		}
	}
	
	@Override
	public <M, V> FooterAdder<M, V> addFooter(@NonNull final SingleModel<M> model, @NonNull final Supplier<V> supplier)
	{
		if (model instanceof DRVModel)
		{
			final DRVModel<M> m = (DRVModel<M>)model;
			final DRVGroup<M, V> group = new DRVGroup<>(m, adapter, adapter::getItemCount, supplier);
			m.init(group, group::getPositionInList);
			return new FooterAdderImpl<>(group);
		}
		else
		{
			Log.w("DeclarativeRecyclerView", "Using an abnormal SingleModel. Use 'SingleModel.of(M m)' of 'SingleModel.empty()'");
			final DRVGroup<M, V> group = new DRVGroup<>(Collections.singletonList(model.get()), adapter, adapter::getItemCount, supplier);
			return new FooterAdderImpl<>(group);
		}
	}
	
	@Override
	public void build()
	{
		applier.accept(adapter);
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
	
	private class GroupAdderImpl<M, V> implements SingleGroupAdder<M, V>
	{
		@NonNull
		private final DRVGroup<M, V> group;
		
		private GroupAdderImpl(@NonNull final DRVGroup<M, V> group)
		{
			this.group = group;
		}
		
		@Override
		public SingleGroupAdder<M, V> onCreate(@NonNull final Consumer<V> onCreate)
		{
			group.setOnCreate(onCreate);
			return this;
		}
		
		@Override
		public GroupAdder<M, V> onFistBind(@NonNull final BiConsumer<V, M> onFirstBind)
		{
			group.setOnFirstBind((v, m, position) -> onFirstBind.accept(v, m));
			return this;
		}
		
		@Override
		public GroupAdder<M, V> onFistBind(@NonNull final TriConsumer<V, M, ItemPosition> onFirstBind)
		{
			group.setOnFirstBind(onFirstBind);
			return this;
		}
		
		@Override
		public GroupAdder<M, V> onBind(@NonNull final BiConsumer<V, M> onBind)
		{
			group.setOnBind((v, m, position) -> onBind.accept(v, m));
			return this;
		}
		
		@Override
		public GroupAdder<M, V> onBind(@NonNull final TriConsumer<V, M, ItemPosition> onBind)
		{
			group.setOnBind(onBind);
			return this;
		}
		
		@Override
		public GroupAdder<M, V> setPlaceholder(final int count, @NonNull final Consumer<V> onPlaceholderBind)
		{
			if (count > 0)
			{
				group.setPlaceholder(count, (v, position) -> onPlaceholderBind.accept(v));
			}
			return this;
		}
		
		@Override
		public GroupAdder<M, V> setPlaceholder(final int count, @NonNull final BiConsumer<V, ItemPosition> onPlaceholderBind)
		{
			if (count > 0)
			{
				group.setPlaceholder(count, onPlaceholderBind);
			}
			return this;
		}
		
		@Override
		public GroupAdder<M, V> setDivider(final int heightDp, final int colorRes)
		{
			group.setDividerCreator(new DRVDivider.Creator(heightDp, colorRes, false));
			return this;
		}
		
		@Override
		public GroupAdder<M, V> setDividerExcludeLast(final int heightDp, final int colorRes)
		{
			group.setDividerCreator(new DRVDivider.Creator(heightDp, colorRes, true));
			return this;
		}
		
		@Override
		public Definable apply()
		{
			adapter.addGroup(group);
			return DRVBuilderImpl.this;
		}
	}
	
	private class FooterAdderImpl<M, V> implements FooterAdder<M, V>
	{
		@NonNull
		private final DRVGroup<M, V> group;
		
		private FooterAdderImpl(@NonNull final DRVGroup<M, V> group)
		{
			this.group = group;
			this.group.setFooter();
		}
		
		@Override
		public FooterAdder<M, V> onCreate(@NonNull final Consumer<V> onCreate)
		{
			group.setOnCreate(onCreate);
			return this;
		}
		
		@Override
		public FooterAdder<M, V> onFistBind(@NonNull final BiConsumer<V, M> onFirstBind)
		{
			group.setOnFirstBind((v, m, position) -> onFirstBind.accept(v, m));
			return this;
		}
		
		@Override
		public FooterAdder<M, V> onFistBind(@NonNull final TriConsumer<V, M, ItemPosition> onFirstBind)
		{
			group.setOnFirstBind(onFirstBind);
			return this;
		}
		
		@Override
		public FooterAdder<M, V> onBind(@NonNull final BiConsumer<V, M> onBind)
		{
			group.setOnBind((v, m, position) -> onBind.accept(v, m));
			return this;
		}
		
		@Override
		public FooterAdder<M, V> onBind(@NonNull final TriConsumer<V, M, ItemPosition> onBind)
		{
			group.setOnBind(onBind);
			return this;
		}
		
		@Override
		public Buildable apply()
		{
			adapter.addGroup(group);
			return DRVBuilderImpl.this;
		}
	}
}