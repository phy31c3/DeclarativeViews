package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import kr.co.plasticcity.declarativeviews.function.BiConsumer;
import kr.co.plasticcity.declarativeviews.function.Supplier;

/**
 * Created by JongsunYu on 2018-04-02.
 */

class DLLBuilderImpl implements DLLBuilder
{
	@Override
	public <M> GroupAdder<M, View> addGroup(@NonNull final M model, final int layoutResId)
	{
		return null;
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@NonNull final M model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		return null;
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@NonNull final M model, @NonNull final Supplier<V> supplier)
	{
		return null;
	}
	
	@Override
	public <M> GroupAdder<M, View> addGroup(@NonNull final List<M> model, final int layoutResId)
	{
		return null;
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, final int layoutResId, @NonNull final Class<V> viewType)
	{
		return null;
	}
	
	@Override
	public <M, V> GroupAdder<M, V> addGroup(@NonNull final List<M> model, @NonNull final Supplier<V> supplier)
	{
		return null;
	}
	
	private class GroupAdderImple<M, V> implements GroupAdder<M, V>
	{
		@Override
		public GroupAdder<M, V> onCreate(@NonNull final BiConsumer<V, M> func)
		{
			return null;
		}
		
		@Override
		public GroupAdder<M, V> onModelChanged(@NonNull final BiConsumer<V, M> func)
		{
			return null;
		}
		
		@Override
		public GroupAdder<M, V> onRemove(@NonNull final BiConsumer<V, M> func)
		{
			return null;
		}
		
		@Override
		public Buildable apply()
		{
			return null;
		}
	}
}
