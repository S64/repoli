package jp.s64.java.repoli.core;

import org.jetbrains.annotations.NotNull;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IProvider<TB, TA> {

    @NotNull <T extends TB, A extends TA> IRepositoryDataContainer<T, A> request(@NotNull IDataKey<T, A> key);

}
