package kr.co.plasticcity.declarativeviews;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by JongsunYu on 2018-05-10.
 */

class DRVItemDecoration extends RecyclerView.ItemDecoration
{
	@NonNull
	private final Set<Integer> footerHashes;
	private int lastPadding;
	
	DRVItemDecoration()
	{
		this.footerHashes = new HashSet<>();
	}
	
	@Override
	public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state)
	{
		final DRVDivider divider = (DRVDivider)view.getTag(ViewTag.DIVIDER);
		if (divider != null)
		{
			outRect.bottom = divider.getItemOffset();
		}
		else if ((Boolean)view.getTag(ViewTag.IS_FOOTER))
		{
			footerHashes.add(view.hashCode());
			
			final int itemCount = parent.getAdapter().getItemCount();
			final int childCount = parent.getChildCount();
			
			final boolean footerNotPrepared = view.getBottom() == 0;
			final boolean deleted = itemCount < childCount;
			final boolean modifying = itemCount > 1 && footerHashes.contains(parent.getChildAt(0).hashCode());
			
			if (footerNotPrepared)
			{
				view.post(() -> ((DRVNotifier)parent.getAdapter()).notifyChangedWithNoAnimation(itemCount - 1));
				lastPadding = parent.getHeight();
				outRect.top = lastPadding;
			}
			else if (deleted | modifying)
			{
				view.post(() -> ((DRVNotifier)parent.getAdapter()).notifyChangedWithNoAnimation(itemCount - 1));
				outRect.top = lastPadding;
			}
			else
			{
				final int listHeight = parent.getHeight();
				final int footerHeight = view.getHeight();
				
				int contentHeight = 0;
				for (int i = 0 ; i < childCount - 1 ; i++)
				{
					final View child = parent.getChildAt(childCount - 2 - i);
					final DRVDivider childDivider = (DRVDivider)child.getTag(ViewTag.DIVIDER);
					if (childDivider != null)
					{
						contentHeight += childDivider.getItemOffset();
					}
					contentHeight += child.getHeight();
					if (contentHeight > listHeight)
					{
						break;
					}
				}
				contentHeight += footerHeight;
				
				final int padding = listHeight - contentHeight;
				if (padding > 0)
				{
					if (itemCount > childCount)
					{
						view.post(() -> ((DRVNotifier)parent.getAdapter()).notifyChangedWithNoAnimation(itemCount - 1));
					}
					else
					{
						lastPadding = padding;
					}
				}
				else
				{
					lastPadding = 0;
				}
				outRect.top = lastPadding;
			}
		}
	}
	
	@Override
	public void onDraw(final Canvas c, final RecyclerView parent, final RecyclerView.State state)
	{
		super.onDraw(c, parent, state);
		final int childCount = parent.getChildCount();
		for (int i = 0 ; i < childCount ; ++i)
		{
			final View child = parent.getChildAt(i);
			final DRVDivider divider = (DRVDivider)child.getTag(ViewTag.DIVIDER);
			if (divider != null)
			{
				divider.draw(c, parent, child);
			}
		}
	}
}
