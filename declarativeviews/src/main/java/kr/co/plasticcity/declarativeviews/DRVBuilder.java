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
	
	interface GroupAdder<M, V>
	{
		GroupAdder<M, V> onCreate(@NonNull final Consumer<V> func);
		
		/**
		 * Ensures that the model (the first param of BiConsumer) is not null.
		 */
		GroupAdder<M, V> onBind(@NonNull final BiConsumer<V, M> func);
		
		/**
		 * Ensures that the model (the first param of TriConsumer) is not null.
		 */
		GroupAdder<M, V> onBind(@NonNull final TriConsumer<V, M, Position> func);
		
		Buildable apply();
	}
	
	interface SingleGroupAdder<M, V> extends GroupAdder<M, V>
	{
		SingleGroupAdder<M, V> onCreate(@NonNull final Consumer<V> func);
		
		/**
		 * Ensures that the model (the first param of BiConsumer) is not null.
		 */
		GroupAdder<M, V> onFistBind(@NonNull final BiConsumer<V, M> func);
		
		/**
		 * Ensures that the model (the first param of BiConsumer) is not null.
		 */
		GroupAdder<M, V> onFistBind(@NonNull final TriConsumer<V, M, Position> func);
	}
	
	interface Buildable extends Builder
	{
		void build();
	}
	
	class Position
	{
		public final int inGroup;
		public final int inList;
		
		Position(final int inGroup, final int inList)
		{
			this.inGroup = inGroup;
			this.inList = inList;
		}
	}
}