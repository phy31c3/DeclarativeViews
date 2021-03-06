package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;
import android.view.View;

import kr.co.plasticcity.declarativeviews.function.BiConsumer;
import kr.co.plasticcity.declarativeviews.function.Consumer;
import kr.co.plasticcity.declarativeviews.function.Supplier;

/**
 * Created by JongsunYu on 2017-01-05.
 */

public interface DVPBuilder
{
	interface Builder extends DVPBuilder {}
	
	SetCircular setItemCount(final int itemCount);
	
	SetRecycle setInfiniteMode(final int offscreenPageLimit);
	
	interface SetCircular extends SetPageView
	{
		SetRecycle setCircular();
	}
	
	interface SetRecycle extends SetPageView
	{
		SetPageView setRecycle();
	}
	
	interface SetPageView
	{
		Options<View> setPageView(final int layoutResId);
		
		<V> Options<V> setPageView(final int layoutResId, @NonNull final Class<V> viewType);
		
		<V> Options<V> setPageView(@NonNull final Supplier<V> supplier);
	}
	
	interface Options<V>
	{
		Options<V> onPageCreated(@NonNull final BiConsumer<V, Integer> onPageCreated);
		
		Options<V> onPageDestroyed(@NonNull final Consumer<Integer> onPageDestroyed);
		
		Options<V> onPageSelected(@NonNull final Consumer<Integer> onPageSelected);
		
		Options<V> setVertical();
		
		void build();
		
		void buildOnUiThread();
		
		void buildOnUiThread(@NonNull final Runnable onBuildFinished);
	}
}