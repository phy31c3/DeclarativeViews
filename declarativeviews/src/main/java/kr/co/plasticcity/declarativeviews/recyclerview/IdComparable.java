package kr.co.plasticcity.declarativeviews.recyclerview;

import android.support.annotation.NonNull;

/**
 * Created by JongsunYu on 2018-03-27.
 */

public interface IdComparable<T>
{
	boolean equalId(@NonNull final T t);
}
