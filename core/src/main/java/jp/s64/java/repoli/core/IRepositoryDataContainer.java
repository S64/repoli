package jp.s64.java.repoli.core;

import org.jetbrains.annotations.Nullable;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IRepositoryDataContainer<T, A> {

    @Nullable
    T getBody();

    @Nullable
    A getAttachment();

    @Nullable
    Long getSavedAtTimeMillis();

    @Nullable
    Long getRequestedAtTimeMillis();

}
