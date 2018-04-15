package kr.co.plasticcity.declarativeviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

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
	
	@Override
	public ViewGroup viewGroup()
	{
		return this;
	}
	
	@Override
	public View getChild(final int index)
	{
		return getChildAt(index);
	}
	
	@Override
	public void add(@NonNull final View view)
	{
		beginTransition();
		addView(view);
	}
	
	@Override
	public void add(final int index, @NonNull final View view)
	{
		beginTransition();
		addView(view, index);
	}
	
	@Override
	public void addAll(@NonNull final List<View> views)
	{
		beginTransition();
		for (final View view : views)
		{
			addView(view);
		}
	}
	
	@Override
	public void addAll(final int index, @NonNull final List<View> views)
	{
		beginTransition();
		for (int i = 0 ; i < views.size() ; ++i)
		{
			addView(views.get(i), index + i);
		}
	}
	
	@Override
	public void remove(final int index)
	{
		beginTransition();
		removeViewAt(index);
	}
	
	@Override
	public void removeRange(final int start, final int count)
	{
		beginTransition();
		for (int i = 0 ; i < count ; ++i)
		{
			removeViewAt(start);
		}
	}
	
	@Override
	public void removeAll()
	{
		beginTransition();
		removeAllViews();
	}
	
	@Override
	public void beginChange()
	{
		beginTransition();
	}
	
	@Override
	public void move(final int from, final int to)
	{
		beginTransition();
		final View view = getChildAt(from);
		removeViewAt(from);
		addView(view, to);
	}
	
	private void beginTransition()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			TransitionManager.beginDelayedTransition(this);
		}
	}
}
