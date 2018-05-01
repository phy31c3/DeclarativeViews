package kr.co.plasticcity.declarativeviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
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
	
	@UiThread
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
	
	/* ############################################################
	 * For background scrolling
	 * ############################################################ */
	
	@Nullable
	private Paint backgroundPaint;
	private int backgroundY;
	
	@Override
	public void setBackgroundDrawable(final Drawable background)
	{
		if (background instanceof BitmapDrawable)
		{
			super.setBackground(null);
			final BitmapDrawable bitmapDrawable = (BitmapDrawable)background;
			final Bitmap bitmap = bitmapDrawable.getBitmap();
			final BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			backgroundPaint = new Paint();
			backgroundPaint.setShader(bitmapShader);
		}
		else
		{
			super.setBackgroundDrawable(background);
			backgroundPaint = null;
		}
	}
	
	@Override
	public void onScrolled(final int dx, final int dy)
	{
		super.onScrolled(dx, dy);
		backgroundY -= dy;
	}
	
	@Override
	public void onDraw(final Canvas c)
	{
		if (backgroundPaint != null)
		{
			c.translate(0, backgroundY);
			c.drawPaint(backgroundPaint);
			c.translate(0, -backgroundY);
		}
		super.onDraw(c);
	}
}