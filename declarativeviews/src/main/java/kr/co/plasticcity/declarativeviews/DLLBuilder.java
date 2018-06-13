package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import kr.co.plasticcity.declarativeviews.function.BiConsumer;
import kr.co.plasticcity.declarativeviews.function.Consumer;
import kr.co.plasticcity.declarativeviews.function.Supplier;
import kr.co.plasticcity.declarativeviews.function.TriConsumer;

/**
 * Created by JongsunYu on 2017-01-03.
 */

public interface DLLBuilder
{
	interface Builder extends DLLBuilder {}
	
	<M> GroupAdder<M, View> addGroup(@NonNull final M model, final int layoutResId);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final M model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final M model, @NonNull final Supplier<V> supplier);
	
	<M> GroupAdder<M, View> addGroup(@NonNull final SingleModel<M> model, final int layoutResId);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final SingleModel<M> model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final SingleModel<M> model, @NonNull final Supplier<V> supplier);
	
	<M> GroupAdder<M, View> addGroup(@NonNull final List<M> model, final int layoutResId);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, @NonNull final Supplier<V> supplier);
	
	interface GroupAdder<M, V>
	{
		GroupAdder<M, V> onCreate(@NonNull final Consumer<V> onCreate);
		
		GroupAdder<M, V> onCreate(@NonNull final BiConsumer<V, ItemPosition> onCreate);
		
		GroupAdder<M, V> onBind(@NonNull final BiConsumer<V, M> onBind);
		
		GroupAdder<M, V> onBind(@NonNull final TriConsumer<V, M, ItemPosition> onBind);
		
		Buildable apply();
	}
	
	interface Buildable extends DLLBuilder.Builder
	{
		void build();
		
		void buildOnUiThread();
		
		void buildOnUiThread(@NonNull final Runnable onBuildFinished);
	}
}