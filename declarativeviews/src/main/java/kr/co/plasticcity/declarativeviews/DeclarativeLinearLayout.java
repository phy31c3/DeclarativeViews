package kr.co.plasticcity.declarativeviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import kr.co.plasticcity.declarativeviews.function.Consumer;

/**
 * Created by JongsunYu on 2018-04-09.
 */

public class DeclarativeLinearLayout extends LinearLayout implements DLLView
{
	@Nullable
	private DLLAdapter adapter;
	
	public DeclarativeLinearLayout(final Context context)
	{
		this(context, null);
	}
	
	public DeclarativeLinearLayout(final Context context, final AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public DeclarativeLinearLayout(final Context context, final AttributeSet attrs, final int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	public void build(@NonNull final Consumer<DLLBuilder.Builder> builder)
	{
		builder.accept(new DLLBuilderImpl(this, adapter ->
		{
			if (this.adapter != null)
			{
				adapter.release();
			}
			this.adapter = adapter;
			adapter.notifyDataSetChanged();
		}));
	}
	
	public void notifyDataSetChanged()
	{
		if (adapter != null)
		{
			adapter.notifyDataSetChanged();
		}
	}
	
	public int getItemCount()
	{
		if (adapter != null)
		{
			return adapter.getItemCount();
		}
		else
		{
			return 0;
		}
	}
}
