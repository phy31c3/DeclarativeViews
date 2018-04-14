package kr.co.plasticcity.declarativeviews.function;

import android.support.annotation.NonNull;

/**
 * Created by JongsunYu on 2018-01-01.
 */

@FunctionalInterface
public interface BiFunction<T, U, R>
{
	@NonNull
	R apply(@NonNull final T t, @NonNull final U u);
}
