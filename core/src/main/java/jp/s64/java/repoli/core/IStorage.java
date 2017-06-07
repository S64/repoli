package jp.s64.java.repoli.core;

import org.jetbrains.annotations.NotNull;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IStorage<TB, AB> {

    @NotNull <T extends TB, A extends AB> IRepositoryDataContainer<T, A> get(@NotNull IDataKey<T, A> key);

    int remove(@NotNull IDataKey<?, ?> key);

    int removeRelatives(@NotNull IDataKey<?, ?> key);

    @NotNull <T extends TB, A extends AB> IRepositoryDataContainer<T, A> save(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<T, A> container);

}
