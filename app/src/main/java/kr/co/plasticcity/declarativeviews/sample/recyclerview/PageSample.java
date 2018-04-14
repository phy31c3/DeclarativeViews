package kr.co.plasticcity.declarativeviews.sample.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import kr.co.plasticcity.declarativeviews.sample.databinding.DrvPageSampleBinding;

/**
 * Created by JongsunYu on 2018-01-09.
 */

public class PageSample extends RelativeLayout
{
	public DrvPageSampleBinding binding;
	
	public PageSample(final Context context)
	{
		this(context, null);
	}
	
	public PageSample(final Context context, final AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public PageSample(final Context context, final AttributeSet attrs, final int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		binding = DrvPageSampleBinding.bind(this);
	}
}
