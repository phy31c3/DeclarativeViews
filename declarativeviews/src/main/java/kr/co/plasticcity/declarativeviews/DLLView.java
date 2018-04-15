package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by JongsunYu on 2018-04-15.
 */

interface DLLView
{
	ViewGroup viewGroup();
	
	View getChild(final int index);
	
	void add(@NonNull final View view);
	
	void add(final int index, @NonNull final View view);
	
	void addAll(@NonNull final List<View> views);
	
	void addAll(final int index, @NonNull final List<View> views);
	
	void remove(final int index);
	
	void removeRange(final int start, final int count);
	
	void removeAll();
	
	void beginChange();
	
	void move(final int from, final int to);
}
