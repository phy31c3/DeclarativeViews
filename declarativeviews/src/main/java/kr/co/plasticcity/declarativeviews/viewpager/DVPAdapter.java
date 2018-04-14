package kr.co.plasticcity.declarativeviews.viewpager;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.co.plasticcity.declarativeviews.function.BiConsumer;
import kr.co.plasticcity.declarativeviews.function.Consumer;
import kr.co.plasticcity.declarativeviews.function.Supplier;

/**
 * Created by JongsunYu on 2017-01-05.
 */

class DVPAdapter<V> extends PagerAdapter
{
	private static final int MAXCNT = Integer.MAX_VALUE;
	private int center = MAXCNT / 2;
	
	private final int layoutResId;
	private final boolean useDataBinding;
	private final int itemCount;
	private final int offscreenPageLimit;
	private final boolean circular;
	private final boolean vertical;
	@Nullable
	private final Supplier<V> viewSupplier;
	@Nullable
	private final BiConsumer<V, Integer> onPageCreated;
	@Nullable
	private final Consumer<Integer> onPageDestroyed;
	@Nullable
	private final Consumer<Integer> onPageSelected;
	
	@SuppressLint("UseSparseArrays")
	DVPAdapter(final int layoutResId, final boolean useDataBinding, final int itemCount, final int offscreenPageLimit, final boolean circular, final boolean vertical,
	           @Nullable final Supplier<V> viewSupplier,
	           @Nullable final BiConsumer<V, Integer> onPageCreated,
	           @Nullable final Consumer<Integer> onPageDestroyed,
	           @Nullable final Consumer<Integer> onPageSelected)
	{
		this.layoutResId = layoutResId;
		this.useDataBinding = useDataBinding;
		this.itemCount = itemCount;
		this.offscreenPageLimit = offscreenPageLimit;
		this.circular = circular;
		this.vertical = vertical;
		this.viewSupplier = viewSupplier;
		this.onPageCreated = onPageCreated;
		this.onPageDestroyed = onPageDestroyed;
		this.onPageSelected = onPageSelected;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Object instantiateItem(final ViewGroup container, final int position)
	{
		final V v;
		final View view;
		if (viewSupplier != null)
		{
			v = viewSupplier.get();
			view = (View)v;
		}
		else if (useDataBinding)
		{
			v = (V)DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), layoutResId, container, false);
			view = ((ViewDataBinding)v).getRoot();
		}
		else
		{
			v = (V)LayoutInflater.from(container.getContext()).inflate(layoutResId, container, false);
			view = (View)v;
		}
		
		container.addView(view);
		if (onPageCreated != null)
		{
			onPageCreated.accept(v, inToOut(position));
		}
		return view;
	}
	
	@Override
	public void destroyItem(final ViewGroup container, final int position, final Object object)
	{
		container.removeView((View)object);
		if (onPageDestroyed != null)
		{
			onPageDestroyed.accept(inToOut(position));
		}
	}
	
	void onPageSelected(final int in)
	{
		if (onPageSelected != null)
		{
			onPageSelected.accept(inToOut(in));
		}
	}
	
	@Override
	@Deprecated
	public int getCount()
	{
		if (circular)
		{
			return MAXCNT;
		}
		else
		{
			return itemCount;
		}
	}
	
	@Override
	@Deprecated
	public int getItemPosition(final Object object)
	{
		return POSITION_NONE;
	}
	
	@Override
	public boolean isViewFromObject(final View view, final Object object)
	{
		return view == object;
	}
	
	int getOffscreenPageLimit()
	{
		return offscreenPageLimit;
	}
	
	boolean isVertical()
	{
		return vertical;
	}
	
	int getPositionZero()
	{
		if (circular)
		{
			return MAXCNT / 2;
		}
		else
		{
			return 0;
		}
	}
	
	void setCenterPositionTo(final int curIn)
	{
		center = curIn;
	}
	
	int inToOut(final int in)
	{
		if (isInfiniteMode())
		{
			return in - center;
		}
		else if (circular)
		{
			return ((in - center) % itemCount + itemCount) % itemCount;
		}
		else
		{
			if (in < 0) { return 0; }
			else if (in >= itemCount) { return itemCount - 1; }
			else { return in; }
		}
	}
	
	int outToIn(final int out, final int curIn)
	{
		if (isInfiniteMode())
		{
			return out + center;
		}
		else if (circular)
		{
			return curIn + out - inToOut(curIn);
		}
		else
		{
			if (out < 0) { return 0; }
			else if (out >= itemCount) { return itemCount - 1; }
			else { return out; }
		}
	}
	
	PagerAdapter getFakeAdapter()
	{
		return new PagerAdapter()
		{
			@Override
			public int getCount()
			{
				return itemCount;
			}
			
			@Override
			public boolean isViewFromObject(final View view, final Object object)
			{
				return false;
			}
		};
	}
	
	private boolean isInfiniteMode()
	{
		return itemCount < 1;
	}
}