package kr.co.plasticcity.declarativeviews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import kr.co.plasticcity.declarativeviews.function.Consumer;


/**
 * Created by JongsunYu on 2016-08-09.
 */
public class DeclarativeRecyclerView extends RecyclerView
{
	@Nullable
	private DRVAdapter adapter;
	
	public DeclarativeRecyclerView(final Context context)
	{
		this(context, null);
	}
	
	public DeclarativeRecyclerView(final Context context, final AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public DeclarativeRecyclerView(final Context context, final AttributeSet attrs, final int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	public void build(@NonNull final Consumer<DRVBuilder.Builder> builder)
	{
		build(builder, null);
	}
	
	public void build(@NonNull final Consumer<DRVBuilder.Builder> builder, @Nullable final LayoutManager layoutManager)
	{
		builder.accept(new DRVBuilderImpl(adapter ->
		{
			this.adapter = adapter;
			super.setAdapter(adapter);
			if (layoutManager != null)
			{
				setLayoutManager(layoutManager);
			}
			else if (getLayoutManager() == null)
			{
				setLayoutManager(new LinearLayoutManager(getContext()));
			}
		}));
	}
	
	public void notifyDataSetChanged()
	{
		if (adapter != null)
		{
			adapter.refreshAll();
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
	
	/**
	 * We recommend that you do not use it.
	 */
	@Override
	@Deprecated
	public Adapter getAdapter()
	{
		return adapter;
	}
	
	/**
	 * No function
	 */
	@Override
	@Deprecated
	public void setAdapter(@Nullable final Adapter adapter)
	{
		/* empty */
	}
}