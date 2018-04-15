package kr.co.plasticcity.declarativeviews;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

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
	
	@Override
	@SuppressWarnings("unchecked")
	public void notifyChanged(final int position)
	{
		view.beginChange();
		getGroupAt(position).onBind(view.getChild(position), position);
	}
	
	@Override
	public void notifyInserted(final int position)
	{
		reorder();
		view.add(position, createView(position));
	}
	
	@Override
	public void notifyRemoved(final int position)
	{
		reorder();
		view.remove(position);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void notifyRangeChanged(final int start, final int count)
	{
		view.beginChange();
		for (int i = 0 ; i < count ; ++i)
		{
			getGroupAt(start + i).onBind(view.getChild(start + i), start + i);
		}
	}
	
	@Override
	public void notifyRangeInserted(final int start, final int count)
	{
		reorder();
		final List<View> views = new ArrayList<>();
		for (int i = 0 ; i < count ; ++i)
		{
			views.add(createView(start + i));
		}
		view.addAll(start, views);
	}
	
	@Override
	public void notifyRangeRemoved(final int start, final int count)
	{
		reorder();
		view.removeRange(start, count);
	}
	
	@Override
	public void notifyMoved(final int from, final int to)
	{
		view.move(from, to);
	}
	
	void notifyDataSetChanged()
	{
		reorder();
		view.removeAll();
		final List<View> views = new ArrayList<>();
		for (int i = 0 ; i < itemCount ; ++i)
		{
			views.add(createView(i));
		}
		view.addAll(views);
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
	
	@NonNull
	@SuppressWarnings("unchecked")
	private View createView(final int position)
	{
		final DRVGroup group = getGroupAt(position);
		final Object v = group.createView(view.viewGroup());
		group.onFirstBind(v, position);
		if (v instanceof ViewDataBinding)
		{
			return ((ViewDataBinding)v).getRoot();
		}
		else
		{
			return (View)v;
		}
	}
}
