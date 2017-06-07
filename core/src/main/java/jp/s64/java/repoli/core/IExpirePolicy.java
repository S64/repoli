package jp.s64.java.repoli.core;

import org.jetbrains.annotations.NotNull;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IExpirePolicy<TB, AB> extends IPolicy {

    <T extends TB, A extends AB> boolean shouldExpire(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<T, A> container);

    <T extends TB, A extends AB> boolean shouldExpireWithRelatives(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<T, A> container);

    <T extends TB, A extends AB> boolean shouldRequest(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<T, A> container);

    <T extends TB, A extends AB> boolean shouldSave(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<T, A> container);

}
