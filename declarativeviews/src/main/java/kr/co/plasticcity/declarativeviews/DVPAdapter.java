package kr.co.plasticcity.declarativeviews;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.Queue;

import kr.co.plasticcity.declarativeviews.function.BiConsumer;
import kr.co.plasticcity.declarativeviews.function.Consumer;
import kr.co.plasticcity.declarativeviews.function.Supplier;

/**
 * Created by JongsunYu on 2017-01-05.
 */

class DVPAdapter<V> extends PagerAdapter
{
	private static final int KEY_V = 0xFA9710D3;
	
	private static final int MAXCNT = 19999;
	private static final int INFINITY_MAX = (MAXCNT - 1) - MAXCNT / 2;
	private static final int INFINITY_MIN = -INFINITY_MAX - 1;
	private int center = MAXCNT / 2;
	
	private final int layoutResId;
	private final boolean useDataBinding;
	private final int itemCount;
	private final boolean circular;
	private final boolean vertical;
	private final boolean recycle;
	@Nullable
	private final Supplier<V> viewSupplier;
	@Nullable
	private final BiConsumer<V, Integer> onPageCreated;
	@Nullable
	private final Consumer<Integer> onPageDestroyed;
	@Nullable
	private final Consumer<Integer> onPageSelected;
	
	@NonNull
	private final Queue<View> viewPool;
	
	@SuppressLint("UseSparseArrays")
	DVPAdapter(final int layoutResId, final boolean useDataBinding, final int itemCount, final boolean circular, final boolean vertical, final boolean recycle,
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
		this.recycle = recycle;
		this.viewSupplier = viewSupplier;
		this.onPageCreated = onPageCreated;
		this.onPageDestroyed = onPageDestroyed;
		this.onPageSelected = onPageSelected;
		
		this.viewPool = new LinkedList<>();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Object instantiateItem(final ViewGroup container, final int position)
	{
		final V v;
		final View view;
		if (!recycle || viewPool.isEmpty())
		{
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
			view.setTag(KEY_V, v);
		}
		else
		{
			view = viewPool.poll();
			v = (V)view.getTag(KEY_V);
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
		final View view = (View)object;
		container.removeView(view);
		if (recycle)
		{
			viewPool.offer(view);
		}
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
	
	/**
	 * @return only circular, not infinite
	 */
	boolean isPureCircular()
	{
		return circular && !isInfinite();
	}
	
	boolean isPureCircularLimit(final int in)
	{
		return isPureCircular() && (in == 0 || in == MAXCNT - 1);
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
			final int out = in - center;
			if (out < INFINITY_MIN)
			{
				return INFINITY_MIN;
			}
			else if (out > INFINITY_MAX)
			{
				return INFINITY_MAX;
			}
			else
			{
				return out;
			}
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
	
	int outToIn(int out, final int curIn)
	{
		if (isInfinite())
		{
			if (out < INFINITY_MIN)
			{
				out = INFINITY_MIN;
			}
			else if (out > INFINITY_MAX)
			{
				out = INFINITY_MAX;
			}
			
			int in = out + center;
			if (in < 0)
			{
				in = 0;
			}
			else if (in >= MAXCNT)
			{
				in = MAXCNT - 1;
			}
			return in;
		}
		else if (circular)
		{
			out = (out % itemCount + itemCount) % itemCount;
			final int curOut = inToOut(curIn);
			if (out == curOut)
			{
				return curIn;
			}
			else
			{
				final int opt1 = curIn + out - curOut;
				final int opt2 = curIn + out - curOut + (out < curOut ? itemCount : -itemCount);
				final int margin1 = Math.abs(opt1 - curIn);
				final int margin2 = Math.abs(opt2 - curIn);
				int in;
				if (margin1 != margin2)
				{
					in = margin1 < margin2 ? opt1 : opt2;
				}
				else // margin1 == margin2
				{
					in = out < curOut ? Math.min(opt1, opt2) : Math.max(opt1, opt2);
				}
				
				if (in < 0)
				{
					in = 0;
				}
				else if (in >= MAXCNT)
				{
					in = MAXCNT - 1;
				}
				return in;
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