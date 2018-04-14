package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import kr.co.plasticcity.declarativeviews.function.BiConsumer;
import kr.co.plasticcity.declarativeviews.function.Supplier;

/**
 * Created by JongsunYu on 2017-01-03.
 */

public interface DLLBuilder
{
	<M> GroupAdder<M, View> addGroup(@NonNull final M model, final int layoutResId);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final M model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final M model, @NonNull final Supplier<V> supplier);
	
	<M> GroupAdder<M, View> addGroup(@NonNull final List<M> model, final int layoutResId);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, final int layoutResId, @NonNull Class<V> viewType);
	
	<M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, @NonNull final Supplier<V> supplier);
	
	interface GroupAdder<M, V>
	{
		GroupAdder<M, V> onCreate(@NonNull final BiConsumer<V, M> func);
		
		GroupAdder<M, V> onModelChanged(@NonNull final BiConsumer<V, M> func);
		
		GroupAdder<M, V> onRemove(@NonNull final BiConsumer<V, M> func);
		
		Buildable apply();
	}
	
	interface Buildable extends DLLBuilder
	{
		void build();
	}
}