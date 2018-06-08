package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;

import kr.co.plasticcity.declarativeviews.function.Supplier;

/**
 * Created by JongsunYu on 2018-04-15.
 */

public class ItemPosition
{
	public final int inGroup;
	public final int inList;
	@NonNull
	private final Supplier<Integer> groupSize;
	@NonNull
	private final Supplier<Integer> listSize;
	
	ItemPosition(final int inGroup, final int inList, @NonNull final Supplier<Integer> groupSize, @NonNull final Supplier<Integer> listSize)
	{
		this.inGroup = inGroup;
		this.inList = inList;
		this.groupSize = groupSize;
		this.listSize = listSize;
	}
	
	int groupSize()
	{
		return groupSize.get();
	}
	
	int listSize()
	{
		return listSize.get();
	}
}
