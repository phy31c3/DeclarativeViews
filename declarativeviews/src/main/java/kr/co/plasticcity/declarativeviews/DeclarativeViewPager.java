package kr.co.plasticcity.declarativeviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.atomic.AtomicBoolean;

import kr.co.plasticcity.declarativeviews.function.Consumer;

/**
 * Created by JongsunYu on 2017-01-05.
 */

public class DeclarativeViewPager extends ViewPager
{
	@Nullable
	private DVPAdapter adapter;
	@NonNull
	private final AtomicBoolean scrolling;
	
	public DeclarativeViewPager(final Context context)
	{
		this(context, null);
	}
	
	public DeclarativeViewPager(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);
		this.scrolling = new AtomicBoolean(false);
	}
	
	/**
	 * Should call this method first
	 */
	public void build(@NonNull Consumer<DVPBuilder.Builder> builder)
	{
		builder.accept(new DVPBuilderImpl(adapter ->
		{
			this.adapter = adapter;
			onAttachedToWindow(); // For prevent slow 'first setCurrentItem'
			super.setAdapter(adapter);
			super.setCurrentItem(adapter.getPositionZero());
			super.setOffscreenPageLimit(adapter.getOffscreenPageLimit());
			super.clearOnPageChangeListeners();
			super.addOnPageChangeListener(new OnPageChangeListener()
			{
				@Override
				public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels)
				{
					/* empty */
				}
				
				@Override
				public void onPageSelected(final int position)
				{
					adapter.onPageSelected(position);
				}
				
				@Override
				public void onPageScrollStateChanged(final int state)
				{
					scrolling.set(state != SCROLL_STATE_IDLE);
				}
			});
			post(() ->
			{
				super.requestLayout(); // For ViewPager's height is wrap_content
				if (getCurrentItem() == 0)
				{
					adapter.onPageSelected(adapter.getPositionZero()); // To call A for the first visible page
				}
				if (adapter.isVertical())
				{
					super.setPageTransformer(true, (page, position) ->
					{
						if (position < -1)
						{
							page.setAlpha(0);
						}
						else if (position <= 1)
						{
							page.setAlpha(1);
							page.setTranslationX(page.getWidth() * -position);
							float yPosition = position * page.getHeight();
							page.setTranslationY(yPosition);
						}
						else
						{
							page.setAlpha(0);
						}
					});
				}
				else
				{
					super.setPageTransformer(false, null);
				}
			});
		}));
	}
	
	/**
	 * It makes current postion to 0 and all pages will be recreated
	 */
	public void reset()
	{
		if (adapter != null)
		{
			adapter.setCenterPositionTo(super.getCurrentItem());
			adapter.notifyDataSetChanged();
		}
	}
	
	public void showNext()
	{
		if (!scrolling.get())
		{
			setCurrentItem(getCurrentItem() + 1);
		}
	}
	
	public void showPrev()
	{
		if (!scrolling.get())
		{
			setCurrentItem(getCurrentItem() - 1);
		}
	}
	
	/**
	 * @return fake adapter
	 */
	@Override
	@Nullable
	public PagerAdapter getAdapter()
	{
		if (adapter != null)
		{
			return adapter.getFakeAdapter();
		}
		else
		{
			return null;
		}
	}
	
	@Override
	@Deprecated
	public void setAdapter(@Nullable final PagerAdapter adapter)
	{
		/* empty */
	}
	
	@Override
	@Deprecated
	public void setOffscreenPageLimit(final int limit)
	{
		/* empty */
	}
	
	@Override
	public int getCurrentItem()
	{
		if (adapter != null)
		{
			return adapter.inToOut(super.getCurrentItem());
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public void setCurrentItem(final int item)
	{
		if (adapter != null)
		{
			super.setCurrentItem(adapter.outToIn(item, super.getCurrentItem()));
		}
	}
	
	@Override
	public void setCurrentItem(final int item, final boolean smoothScroll)
	{
		if (adapter != null)
		{
			super.setCurrentItem(adapter.outToIn(item, super.getCurrentItem()), smoothScroll);
		}
	}
	
	@Override
	public void addOnPageChangeListener(final OnPageChangeListener listener)
	{
		super.addOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels)
			{
				if (adapter != null)
				{
					listener.onPageScrolled(adapter.inToOut(position), positionOffset, positionOffsetPixels);
				}
			}
			
			@Override
			public void onPageSelected(final int position)
			{
				if (adapter != null)
				{
					listener.onPageSelected(adapter.inToOut(position));
				}
			}
			
			@Override
			public void onPageScrollStateChanged(final int state)
			{
				listener.onPageScrollStateChanged(state);
			}
		});
	}
	
	/**
	 * Do not call if vertical mode is set.
	 */
	@Override
	public void setPageTransformer(final boolean reverseDrawingOrder, final PageTransformer transformer)
	{
		post(() -> super.setPageTransformer(reverseDrawingOrder, transformer));
	}
	
	/**
	 * Do not call if vertical mode is set.
	 */
	@Override
	public void setPageTransformer(final boolean reverseDrawingOrder, final PageTransformer transformer, final int pageLayerType)
	{
		post(() -> super.setPageTransformer(reverseDrawingOrder, transformer, pageLayerType));
	}
	
	@Override
	public boolean onInterceptTouchEvent(final MotionEvent ev)
	{
		if (adapter != null && adapter.isVertical())
		{
			boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
			swapXY(ev);
			return intercepted;
		}
		else
		{
			return super.onInterceptTouchEvent(ev);
		}
	}
	
	@Override
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(final MotionEvent ev)
	{
		if (adapter != null && adapter.isVertical())
		{
			return super.onTouchEvent(swapXY(ev));
		}
		else
		{
			return super.onTouchEvent(ev);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT)
		{
			int height = 0;
			for (int i = 0 ; i < getChildCount() ; i++)
			{
				final View child = getChildAt(i);
				child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				final int childMeasuredHeight = child.getMeasuredHeight();
				if (childMeasuredHeight > height)
				{
					height = childMeasuredHeight;
				}
			}
			
			if (height < getMinimumHeight())
			{
				height = getMinimumHeight();
			}
			
			if (height != 0)
			{
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
			}
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private MotionEvent swapXY(final MotionEvent ev)
	{
		final float width = getWidth();
		final float height = getHeight();
		final float newX = (ev.getY() / height) * width;
		final float newY = (ev.getX() / width) * height;
		ev.setLocation(newX, newY);
		return ev;
	}
}