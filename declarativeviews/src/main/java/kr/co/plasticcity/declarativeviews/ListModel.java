package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.util.Collection;
import java.util.List;

import kr.co.plasticcity.declarativeviews.function.BiFunction;
import kr.co.plasticcity.declarativeviews.function.Function;

/**
 * Created by JongsunYu on 2017-12-27.
 */

/**
 * @param <M> Recommend that M be an immutable object.
 */
public interface ListModel<M> extends List<M>
{
	/**
	 * Creates a wrapper for the list.
	 * The contents manipulated by the ListModel are also reflected in the original list.
	 *
	 * @param l If null, an empty ListModel will be created. Also it should not contain null value.
	 */
	static <M> ListModel<M> of(@Nullable final List<M> l)
	{
		if (l != null)
		{
			return new DRVModel<>(l);
		}
		else
		{
			return empty();
		}
	}
	
	static <M> ListModel<M> empty()
	{
		return new DRVModel<>();
	}
	
	/**
	 * If model M of index is null, the function is not called.
	 *
	 * @param index Should be valid index. {@literal (0 <= index < size)}
	 */
	@UiThread
	void replace(final int index, @NonNull final Function<M, M> f);
	
	/**
	 * If model M of position is null, the function is not called.
	 *
	 * @param start Should be valid index. {@literal (0 <= start <= end)}
	 * @param end   Should be valid index. {@literal (start <= end < size)}
	 */
	@UiThread
	void replaceRange(final int start, final int end, @NonNull final BiFunction<M, Integer, M> f);
	
	@UiThread
	void move(final int from, final int to);
	
	/**
	 * For update execution, each model M must be be able to compare 'id equality' as {@link IdComparable}.
	 * 'id equality' is basically determined by IdComparable's 'equalId()' method, but if model M does not implement IdComparable, 'id equality' is determined by 'equals()' method.
	 * Also, the original and the list l must follow the same order.<br>
	 * (ex : origin = [3, 5, 10], l = [0, 2, 5, 6, 9], result = [0, 2, 5, 6, 9])<br>
	 * Update does not mean union, so the result list is the same size and value as the list l.
	 *
	 * @param l List with the same order as the original list.
	 */
	@UiThread
	void update(@NonNull final List<M> l);
	
	/**
	 * @param index The position in group. {@literal (0 <= index < size)}
	 * @return The position in list.
	 */
	int getPositionInList(final int index);
	
	int getPositionInList(@NonNull final M m);
	
	/**
	 * @param index Should be valid index. {@literal (0 <= index < size)}
	 */
	@UiThread
	void performChanged(final int index);
	
	/**
	 * @param start Should be valid index. {@literal (0 <= start <= end)}
	 * @param end   Should be valid index. {@literal (start <= end < size)}
	 */
	@UiThread
	void performRangeChanged(final int start, final int end);
	
	@UiThread
	void performAllChanged();
	
	/////////////////////////////////////////////////////////////////////////
	// from List<M>
	/////////////////////////////////////////////////////////////////////////
	@Override
	@UiThread
	M set(int index, M element);
	
	@Override
	@UiThread
	boolean add(M m);
	
	@Override
	@UiThread
	void add(int index, M element);
	
	@Override
	@UiThread
	boolean addAll(@NonNull Collection<? extends M> c);
	
	@Override
	@UiThread
	boolean addAll(int index, @NonNull Collection<? extends M> c);
	
	@Override
	@UiThread
	M remove(int index);
	
	@Override
	@UiThread
	boolean remove(Object o);
	
	@Override
	@UiThread
	boolean removeAll(@NonNull Collection<?> c);
	
	@Override
	@UiThread
	boolean retainAll(@NonNull Collection<?> c);
	
	@Override
	@UiThread
	void clear();
}
