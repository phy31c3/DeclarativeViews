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
	
	<M> SingleGroupAdder<M, View> addGroup(@NonNull final M model, final int layoutResId);
	
	<M, V> SingleGroupAdder<M, V> addGroup(@NonNull final M model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> SingleGroupAdder<M, V> addGroup(@NonNull final M model, @NonNull final Supplier<V> supplier);
	
	<M> SingleGroupAdder<M, View> addGroup(@NonNull final SingleModel<M> model, final int layoutResId);
	
	<M, V> SingleGroupAdder<M, V> addGroup(@NonNull final SingleModel<M> model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> SingleGroupAdder<M, V> addGroup(@NonNull final SingleModel<M> model, @NonNull final Supplier<V> supplier);
	
	<M> GroupAdder<M, View> addGroup(@NonNull final List<M> model, final int layoutResId);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, @NonNull final Supplier<V> supplier);
	
	<M> FooterAdder<M, View> addFooter(@NonNull final M model, final int layoutResId);
	
	<M, V> FooterAdder<M, V> addFooter(@NonNull final M model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> FooterAdder<M, V> addFooter(@NonNull final M model, @NonNull final Supplier<V> supplier);
	
	<M> FooterAdder<M, View> addFooter(@NonNull final SingleModel<M> model, final int layoutResId);
	
	<M, V> FooterAdder<M, V> addFooter(@NonNull final SingleModel<M> model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> FooterAdder<M, V> addFooter(@NonNull final SingleModel<M> model, @NonNull final Supplier<V> supplier);
	
	interface GroupAdder<M, V>
	{
		GroupAdder<M, V> onCreate(@NonNull final Consumer<V> func);
		
		GroupAdder<M, V> onBind(@NonNull final BiConsumer<V, M> func);
		
		GroupAdder<M, V> onBind(@NonNull final TriConsumer<V, M, ItemPosition> func);
		
		Definable apply();
	}
	
	interface SingleGroupAdder<M, V> extends GroupAdder<M, V>
	{
		SingleGroupAdder<M, V> onCreate(@NonNull final Consumer<V> func);
		
		GroupAdder<M, V> onFistBind(@NonNull final BiConsumer<V, M> func);
		
		GroupAdder<M, V> onFistBind(@NonNull final TriConsumer<V, M, ItemPosition> func);
		
		GroupAdder<M, V> onBind(@NonNull final BiConsumer<V, M> func);
		
		GroupAdder<M, V> onBind(@NonNull final TriConsumer<V, M, ItemPosition> func);
		
		Definable apply();
	}
	
	interface FooterAdder<M, V>
	{
		FooterAdder<M, V> onCreate(@NonNull final Consumer<V> func);
		
		FooterAdder<M, V> onFistBind(@NonNull final BiConsumer<V, M> func);
		
		FooterAdder<M, V> onFistBind(@NonNull final TriConsumer<V, M, ItemPosition> func);
		
		FooterAdder<M, V> onBind(@NonNull final BiConsumer<V, M> func);
		
		FooterAdder<M, V> onBind(@NonNull final TriConsumer<V, M, ItemPosition> func);
		
		Buildable apply();
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