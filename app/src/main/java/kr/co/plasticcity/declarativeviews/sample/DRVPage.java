package kr.co.plasticcity.declarativeviews.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import kr.co.plasticcity.declarativeviews.sample.databinding.DrvPageBinding;

/**
 * Created by JongsunYu on 2018-01-09.
 */

public class DRVPage extends RelativeLayout
{
	public DrvPageBinding binding;
	
	public DRVPage(final Context context)
	{
		this(context, null);
	}
	
	public DRVPage(final Context context, final AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public DRVPage(final Context context, final AttributeSet attrs, final int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		binding = DrvPageBinding.bind(this);
	}
}
