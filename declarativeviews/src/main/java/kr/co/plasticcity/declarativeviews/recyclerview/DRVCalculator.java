package kr.co.plasticcity.declarativeviews.recyclerview;

import android.support.annotation.NonNull;

/**
 * Created by JongsunYu on 2018-01-09.
 */

interface DRVCalculator
{
	@NonNull
	static DRVCalculator empty()
	{
		return positionInGroup -> 0;
	}
	
	int getPositionInList(final int positionInGroup);
}
