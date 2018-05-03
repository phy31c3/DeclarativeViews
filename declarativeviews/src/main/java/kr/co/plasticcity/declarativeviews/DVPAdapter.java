package kr.co.plasticcity.declarativeviews;

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
	private static final int MAXCNT = 10000;
	private int center = MAXCNT / 2;
	
	private final int layoutResId;
	private final boolean useDataBinding;
	private final int itemCount;
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
	DVPAdapter(final int layoutResId, final boolean useDataBinding, final int itemCount, final boolean circular, final boolean vertical,
	           @Nullable final Supplier<V> viewSupplier,
	           @Nullable final BiConsumer<V, Integer> onPageCreated,
	           @Nullable final Consumer<Integer> onPageDestroyed,
	           @Nullable final Consumer<Integer> onPageSelected)
	{
		this.layoutResId = layoutResId;
		this.useDataBinding = useDataBinding;
		this.itemCount = itemCount;
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
		// TODO: 2018-05-03 in값이 0 또는 MAX일 경우에 대한 처리 
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
	
	/**
	 * @return only circular, not infinite
	 */
	boolean isPureCircular()
	{
		return circular && !isInfinite();
	}
	
	boolean isVertical()
	{
		return vertical;
	}
	
	int getItemCount()
	{
		return itemCount;
	}
	
	int getPositionZero()
	{
		if (circular)
		{
			return center;
		}
		else
		{
			return 0;
		}
	}
	
	int inToOut(final int in)
	{
		if (isInfinite())
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
		if (isInfinite())
		{
			return out + center;
		}
		else if (circular)
		{
			final int curOut = inToOut(curIn);
			if (out == curOut)
			{
				return curIn;
			}
			else
			{
				final int opt1 = curIn + out - curOut;
				final int opt2 = curIn + out + itemCount - curOut;
				final int margin1 = Math.abs(opt1 - curIn);
				final int margin2 = Math.abs(opt2 - curIn);
				if (margin1 != margin2)
				{
					return margin1 < margin2 ? opt1 : opt2;
				}
				else // margin1 == margin2
				{
					return out < curOut ? Math.min(opt1, opt2) : Math.max(opt1, opt2);
				}
			}
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
	
	private boolean isInfinite()
	{
		return circular && itemCount == 0;
	}
}