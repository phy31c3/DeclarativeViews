package kr.co.plasticcity.declarativeviews;

import android.support.annotation.NonNull;

/**
 * Created by JongsunYu on 2018-03-27.
 */

public interface IdComparable<T>
{
	boolean hasSameId(@NonNull final T t);
}
