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

public interface DRVBuilder
{
	interface Builder extends DRVBuilder {}
	
	<M> SingleGroupAdder<M, View, Definable> addGroup(@NonNull final M model, final int layoutResId);
	
	<M, V> SingleGroupAdder<M, V, Definable> addGroup(@NonNull final M model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> SingleGroupAdder<M, V, Definable> addGroup(@NonNull final M model, @NonNull final Supplier<V> supplier);
	
	<M> SingleGroupAdder<M, View, Definable> addGroup(@NonNull final SingleModel<M> model, final int layoutResId);
	
	<M, V> SingleGroupAdder<M, V, Definable> addGroup(@NonNull final SingleModel<M> model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> SingleGroupAdder<M, V, Definable> addGroup(@NonNull final SingleModel<M> model, @NonNull final Supplier<V> supplier);
	
	<M> GroupAdder<M, View, Definable> addGroup(@NonNull final List<M> model, final int layoutResId);
	
	<M, V> GroupAdder<M, V, Definable> addGroup(@NonNull final List<M> model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> GroupAdder<M, V, Definable> addGroup(@NonNull final List<M> model, @NonNull final Supplier<V> supplier);
	
	<M> SingleGroupAdder<M, View, Buildable> addFooter(@NonNull final M model, final int layoutResId);
	
	<M, V> SingleGroupAdder<M, V, Buildable> addFooter(@NonNull final M model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> SingleGroupAdder<M, V, Buildable> addFooter(@NonNull final M model, @NonNull final Supplier<V> supplier);
	
	<M> SingleGroupAdder<M, View, Buildable> addFooter(@NonNull final SingleModel<M> model, final int layoutResId);
	
	<M, V> SingleGroupAdder<M, V, Buildable> addFooter(@NonNull final SingleModel<M> model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> SingleGroupAdder<M, V, Buildable> addFooter(@NonNull final SingleModel<M> model, @NonNull final Supplier<V> supplier);
	
	interface GroupAdder<M, V, R>
	{
		GroupAdder<M, V, R> onCreate(@NonNull final Consumer<V> func);
		
		GroupAdder<M, V, R> onBind(@NonNull final BiConsumer<V, M> func);
		
		GroupAdder<M, V, R> onBind(@NonNull final TriConsumer<V, M, ItemPosition> func);
		
		R apply();
	}
	
	interface SingleGroupAdder<M, V, R> extends GroupAdder<M, V, R>
	{
		SingleGroupAdder<M, V, R> onCreate(@NonNull final Consumer<V> func);
		
		GroupAdder<M, V, R> onFistBind(@NonNull final BiConsumer<V, M> func);
		
		GroupAdder<M, V, R> onFistBind(@NonNull final TriConsumer<V, M, ItemPosition> func);
	}
	
	interface Buildable
	{
		void build();
		
		void buildOnUiThread();
		
		void buildOnUiThread(@NonNull final Runnable onBuildFinished);
	}
	
	interface Definable extends Builder, Buildable
	{
		/* nothing */
	}
}