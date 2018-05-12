package kr.co.plasticcity.declarativeviews;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by JongsunYu on 2018-05-10.
 */

class DRVFooterDecoration extends RecyclerView.ItemDecoration
{
	@NonNull
	private final Set<Integer> footerHashes;
	private int lastPadding;
	
	DRVFooterDecoration()
	{
		this.footerHashes = new HashSet<>();
	}
	
	@Override
	public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state)
	{
		if ((Boolean)view.getTag(ViewTag.IS_FOOTER))
		{
			footerHashes.add(view.hashCode());
			
			final int itemCount = parent.getAdapter().getItemCount();
			
			final boolean footerNotPrepared = view.getBottom() == 0;
			final boolean inserted = itemCount > parent.getChildCount();
			final boolean modifying = itemCount > 1 && footerHashes.contains(parent.getChildAt(0).hashCode());
			
			if (footerNotPrepared || inserted || modifying)
			{
				view.post(() -> parent.getAdapter().notifyItemChanged(itemCount - 1));
				outRect.top = lastPadding;
			}
			else
			{
				final int listHeight = parent.getHeight();
				final int footerHeight = view.getHeight();
				
				int contentHeight = 0;
				for (int i = 0 ; i < itemCount - 1 ; i++)
				{
					contentHeight += parent.getChildAt(i).getHeight();
				}
				contentHeight += footerHeight;
				
				final int padding = listHeight - contentHeight;
				lastPadding = padding > 0 ? padding : 0;
				outRect.top = lastPadding;
			}
		}
	}
}
