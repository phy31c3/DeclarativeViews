package kr.co.plasticcity.declarativeviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by JongsunYu on 2018-05-22.
 */

class DRVDivider
{
	private final int height;
	@NonNull
	private final Drawable drawable;
	private final boolean includeLast;
	@NonNull
	private ItemPosition itemPosition;
	
	private DRVDivider(final int height, @NonNull final Drawable drawable, final boolean includeLast)
	{
		this.height = height;
		this.drawable = drawable;
		this.includeLast = includeLast;
		this.itemPosition = new ItemPosition(0, 0, () -> 0, () -> 0);
	}
	
	void setItemPosition(@NonNull final ItemPosition itemPosition)
	{
		this.itemPosition = itemPosition;
	}
	
	int getPositionInGroup()
	{
		return itemPosition.inGroup;
	}
	
	int getItemOffset()
	{
		if (itemPosition.inGroup < itemPosition.groupSize() - (includeLast ? 0 : 1))
		{
			return height;
		}
		else
		{
			return 0;
		}
	}
	
	void draw(@NonNull final Canvas c, @NonNull final RecyclerView parent, @NonNull final View child)
	{
		if (itemPosition.inGroup < itemPosition.groupSize() - (includeLast ? 0 : 1))
		{
			final int lineLeft = parent.getPaddingLeft();
			final int lineRight = parent.getWidth() - parent.getPaddingRight();
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
			final int lineTop = child.getBottom() + params.bottomMargin + Math.round(child.getTranslationY());
			final int lineBottom = lineTop + height;
			drawable.setAlpha((int)(child.getAlpha() * 255));
			drawable.setBounds(lineLeft, lineTop, lineRight, lineBottom);
			drawable.draw(c);
		}
	}
	
	static class Creator
	{
		private final int heightDp;
		private final int colorRes;
		private final boolean includeLast;
		
		Creator(final int heightDp, final int colorRes, final boolean includeLast)
		{
			this.heightDp = heightDp;
			this.colorRes = colorRes;
			this.includeLast = includeLast;
		}
		
		DRVDivider create(@NonNull final Context c)
		{
			final int height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDp, c.getResources().getDisplayMetrics()));
			final GradientDrawable drawable = new GradientDrawable();
			drawable.setColor(ContextCompat.getColor(c, colorRes));
			return new DRVDivider(height, drawable, includeLast);
		}
		
		boolean isIncludeLast()
		{
			return includeLast;
		}
	}
}
