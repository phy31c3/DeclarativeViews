package kr.co.plasticcity.declarativeviews;

/**
 * Created by JongsunYu on 2018-04-15.
 */

public class ItemPosition
{
	/**
	 * It is accurate only when ItemPosition is obtained, and is not synchronized with the model change.
	 */
	public final int inGroup;
	
	/**
	 * It is accurate only when ItemPosition is obtained, and is not synchronized with the model change.
	 */
	public final int inList;
	
	ItemPosition(final int inGroup, final int inList)
	{
		this.inGroup = inGroup;
		this.inList = inList;
	}
}
