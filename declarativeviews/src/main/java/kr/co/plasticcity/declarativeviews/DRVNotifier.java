package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;

/**
 * Created by JongsunYu on 2017-12-28.
 */

interface DRVNotifier
{
	@NonNull
	static DRVNotifier empty()
	{
		return new DRVNotifier()
		{
			@Override
			public void notifyChanged(final int position) {}
			
			@Override
			public void notifyInserted(final int position) {}
			
			@Override
			public void notifyRemoved(final int position) {}
			
			@Override
			public void notifyRangeChanged(final int start, final int count) {}
			
			@Override
			public void notifyChangedWithNoAnimation(final int position) {}
			
			@Override
			public void notifyRangeInserted(final int start, final int count) {}
			
			@Override
			public void notifyRangeRemoved(final int start, final int count) {}
			
			@Override
			public void notifyMoved(final int from, final int to) {}
		};
	}
	
	void notifyChanged(final int position);
	
	void notifyChangedWithNoAnimation(final int position);
	
	void notifyInserted(final int position);
	
	void notifyRemoved(final int position);
	
	void notifyRangeChanged(final int start, final int count);
	
	void notifyRangeInserted(final int start, final int count);
	
	void notifyRangeRemoved(final int start, final int count);
	
	void notifyMoved(final int from, final int to);
}
