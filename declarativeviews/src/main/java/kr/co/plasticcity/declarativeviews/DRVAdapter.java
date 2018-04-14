package kr.co.plasticcity.declarativeviews;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by JongsunYu on 2017-01-03.
 */

class DRVAdapter extends RecyclerView.Adapter<DRVAdapter.ViewHolder> implements DRVNotifier
{
	@NonNull
	private final List<DRVGroup> groups;
	@NonNull
	private TreeMap<Integer, DRVGroup> groupMap;
	@NonNull
	private SparseArray<DRVGroup> hashCodes;
	private int itemCount;
	
	DRVAdapter()
	{
		this.groups = new ArrayList<>();
		this.groupMap = new TreeMap<>();
		this.hashCodes = new SparseArray<>();
		this.itemCount = 0;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public DRVAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType)
	{
		final Object v = hashCodes.get(viewType).createView(parent);
		if (v instanceof ViewDataBinding)
		{
			return new ViewHolder(((ViewDataBinding)v).getRoot(), v);
		}
		else
		{
			return new ViewHolder((View)v, v);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void onBindViewHolder(@NonNull final DRVAdapter.ViewHolder holder, final int position)
	{
		if (holder.fresh)
		{
			holder.fresh = false;
			getGroupAt(position).onFirstBind(holder.v, position);
		}
		else
		{
			getGroupAt(position).onBind(holder.v, position);
		}
	}
	
	@Override
	public int getItemCount()
	{
		return itemCount;
	}
	
	@Override
	public int getItemViewType(int position)
	{
		return getGroupAt(position).hashCode();
	}
	
	@Override
	public void notifyChanged(final int position)
	{
		super.notifyItemChanged(position);
	}
	
	@Override
	public void notifyInserted(final int position)
	{
		reorder();
		super.notifyItemInserted(position);
	}
	
	@Override
	public void notifyRemoved(final int position)
	{
		reorder();
		super.notifyItemRemoved(position);
	}
	
	@Override
	public void notifyRangeChanged(final int start, final int count)
	{
		super.notifyItemRangeChanged(start, count);
	}
	
	@Override
	public void notifyRangeInserted(final int start, final int count)
	{
		reorder();
		super.notifyItemRangeInserted(start, count);
	}
	
	@Override
	public void notifyRangeRemoved(final int start, final int count)
	{
		reorder();
		super.notifyItemRangeRemoved(start, count);
	}
	
	@Override
	public void notifyMoved(final int from, final int to)
	{
		super.notifyItemMoved(from, to);
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
	
	void refreshAll()
	{
		reorder();
		notifyDataSetChanged();
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
	
	class ViewHolder<V> extends RecyclerView.ViewHolder
	{
		@NonNull
		private final V v;
		private boolean fresh;
		
		private ViewHolder(final View itemView, @NonNull final V v)
		{
			super(itemView);
			this.v = v;
			this.fresh = true;
		}
	}
}