package kr.co.plasticcity.declarativeviews.function;

import android.support.annotation.NonNull;

/**
 * Created by JongsunYu on 2018-01-01.
 */

@FunctionalInterface
public interface TriConsumer<T, U, V>
{
	void accept(@NonNull final T t, @NonNull final U u, @NonNull final V v);
}
