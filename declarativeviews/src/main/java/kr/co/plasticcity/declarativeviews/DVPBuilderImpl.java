package kr.co.plasticcity.declarativeviews;

import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import kr.co.plasticcity.declarativeviews.function.BiConsumer;
import kr.co.plasticcity.declarativeviews.function.Consumer;
import kr.co.plasticcity.declarativeviews.function.Supplier;

/**
 * Created by JongsunYu on 2017-01-05.
 */

class DVPBuilderImpl implements DVPBuilder.Builder, DVPBuilder.SetCircular
{
	@NonNull
	private final BiConsumer<DVPAdapter, Integer> registrator;
	private int itemCount;
	private int offscreenPageLimit;
	private boolean circular;
	
	DVPBuilderImpl(@NonNull final BiConsumer<DVPAdapter, Integer> registrator)
	{
		this.registrator = registrator;
	}
	
	@Override
	public SetCircular setItemCount(final int itemCount)
	{
		this.itemCount = itemCount;
		this.offscreenPageLimit = itemCount;
		return this;
	}
	
	@Override
	public SetPageView setInfiniteMode(final int offscreenPageLimit)
	{
		this.itemCount = 0;
		this.offscreenPageLimit = offscreenPageLimit;
		this.circular = true;
		return this;
	}
	
	@Override
	public SetPageView setCircular()
	{
		this.circular = true;
		return this;
	}
	
	@Override
	public Options<View> setPageView(final int layoutResId)
	{
		return new OptionsImpl<>(layoutResId, null);
	}
	
	@Override
	public <V> Options<V> setPageView(final int layoutResId, @NonNull final Class<V> viewType)
	{
		return new OptionsImpl<>(layoutResId, viewType);
	}
	
	@Override
	public <V> Options<V> setPageView(@NonNull final Supplier<V> supplier)
	{
		return new OptionsImpl<>(supplier);
	}
	
	private class OptionsImpl<V> implements Options<V>
	{
		private final int layoutResId;
		@Nullable
		private final Class<V> viewType;
		@Nullable
		private final Supplier<V> viewSupplier;
		@Nullable
		private BiConsumer<V, Integer> onPageCreated;
		@Nullable
		private Consumer<Integer> onPageDestroyed;
		@Nullable
		private Consumer<Integer> onPageSelected;
		private boolean vertical;
		
		private OptionsImpl(final int layoutResId, @Nullable final Class<V> viewType)
		{
			this.layoutResId = layoutResId;
			this.viewType = viewType;
			this.viewSupplier = null;
		}
		
		private OptionsImpl(@NonNull final Supplier<V> viewSupplier)
		{
			this.layoutResId = 0;
			this.viewType = null;
			this.viewSupplier = viewSupplier;
		}
		
		@Override
		public Options<V> onPageCreated(@NonNull final BiConsumer<V, Integer> onPageCreated)
		{
			this.onPageCreated = onPageCreated;
			return this;
		}
		
		@Override
		public Options<V> onPageDestroyed(@NonNull final Consumer<Integer> onPageDestroyed)
		{
			this.onPageDestroyed = onPageDestroyed;
			return this;
		}
		
		@Override
		public Options<V> onPageSelected(@NonNull final Consumer<Integer> onPageSelected)
		{
			this.onPageSelected = onPageSelected;
			return this;
		}
		
		@Override
		public Options<V> setVertical()
		{
			this.vertical = true;
			return this;
		}
		
		@Override
		public void build()
		{
			final boolean useDataBinding = viewType != null && viewType.getSuperclass() != null && viewType.getSuperclass().equals(ViewDataBinding.class);
			registrator.accept(new DVPAdapter<>(layoutResId, useDataBinding, itemCount, circular, vertical, viewSupplier, onPageCreated, onPageDestroyed, onPageSelected), offscreenPageLimit);
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
	}
}