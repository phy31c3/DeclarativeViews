package kr.co.plasticcity.declarativeviews.function;

import android.support.annotation.NonNull;

/**
 * Created by JongsunYu on 2018-01-01.
 */

@FunctionalInterface
public interface Supplier<T>
{
	@NonNull
	T get();
}
