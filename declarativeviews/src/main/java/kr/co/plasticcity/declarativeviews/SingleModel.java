package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.util.ArrayList;
import java.util.Collections;

import kr.co.plasticcity.declarativeviews.function.Function;

/**
 * Created by JongsunYu on 2017-12-27.
 */

/**
 * @param <M> Recommend that M be an immutable object.
 */
public interface SingleModel<M>
{
	static <M> SingleModel<M> of(@NonNull final M m)
	{
		return new DRVModel<>(new ArrayList<>(Collections.singletonList(m)));
	}
	
	static <M> SingleModel<M> empty()
	{
		return new DRVModel<>();
	}
	
	boolean isEmpty();
	
	/**
	 * @return If SingleModel is empty, return null
	 */
	@Nullable
	M get();
	
	@UiThread
	void set(@NonNull final M m);
	
	/**
	 * If SignleModel is empty or model (M) is null, the function is not called.
	 */
	@UiThread
	void replace(@NonNull final Function<M, M> f);
	
	@UiThread
	void remove();
	
	int getPositionInList();
	
	/**
	 * Forcibly notifies the model has changed and invokes onBind.
	 */
	@UiThread
	void performChanged();
}
