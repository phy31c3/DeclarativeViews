package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by JongsunYu on 2018-04-15.
 */

public class DLLAdapter implements DRVNotifier
{
	@NonNull
	private final DLLView view;
	@NonNull
	private final List<DRVGroup> groups;
	@NonNull
	private TreeMap<Integer, DRVGroup> groupMap;
	@NonNull
	private SparseArray<DRVGroup> hashCodes;
	private int itemCount;
	
	DLLAdapter(@NonNull final DLLView view)
	{
		this.view = view;
		this.groups = new ArrayList<>();
		this.groupMap = new TreeMap<>();
		this.hashCodes = new SparseArray<>();
		this.itemCount = 0;
	}
	
	void release()
	{
		// TODO: 2018-04-15
	}
	
	@Override
	public void notifyChanged(final int position)
	{
		// TODO: 2018-04-15
	}
	
	@Override
	public void notifyInserted(final int position)
	{
		reorder();
		// TODO: 2018-04-15
	}
	
	@Override
	public void notifyRemoved(final int position)
	{
		reorder();
		// TODO: 2018-04-15
	}
	
	@Override
	public void notifyRangeChanged(final int start, final int count)
	{
		// TODO: 2018-04-15
	}
	
	@Override
	public void notifyRangeInserted(final int start, final int count)
	{
		reorder();
		// TODO: 2018-04-15
	}
	
	@Override
	public void notifyRangeRemoved(final int start, final int count)
	{
		reorder();
		// TODO: 2018-04-15
	}
	
	@Override
	public void notifyMoved(final int from, final int to)
	{
		// TODO: 2018-04-15
	}
	
	void notifyDataSetChanged()
	{
		reorder();
		// TODO: 2018-04-15
	}
	
	int getItemCount()
	{
		return itemCount;
	}
	
	void addGroup(@NonNull final DRVGroup group)
	{
		groups.add(group);
		hashCodes.put(group.hashCode(), group);
		group.setPosition(itemCount);
		if (group.size() > 0)
		{
			groupMap.put(itemCount, group);
			itemCount += group.size();
		}
	}
	
	private void reorder()
	{
		itemCount = 0;
		final TreeMap<Integer, DRVGroup> groupMap = new TreeMap<>();
		for (DRVGroup group : groups)
		{
			group.setPosition(itemCount);
			if (group.size() > 0)
			{
				groupMap.put(itemCount, group);
				itemCount += group.size();
			}
		}
		this.groupMap = groupMap;
	}
	
	@NonNull
	private DRVGroup getGroupAt(final int position)
	{
		return groupMap.floorEntry(position).getValue();
	}
}
