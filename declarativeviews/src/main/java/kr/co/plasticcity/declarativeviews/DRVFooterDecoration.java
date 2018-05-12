package kr.co.plasticcity.declarativeviews;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by JongsunYu on 2018-05-10.
 */

class DRVFooterDecoration extends RecyclerView.ItemDecoration
{
	@Override
	public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state)
	{
		if ((Boolean)view.getTag(ViewTag.IS_FOOTER))
		{
			final int itemCount = parent.getAdapter().getItemCount();
			
			final boolean isFooterNotPrepared = view.getBottom() == 0;
			final boolean isInserted = itemCount > parent.getChildCount();
			
			if (isFooterNotPrepared || isInserted)
			{
				view.post(() -> parent.getAdapter().notifyItemChanged(parent.getAdapter().getItemCount() - 1));
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
				outRect.top = padding > 0 ? padding : 0;
			}
		}
	}
}
