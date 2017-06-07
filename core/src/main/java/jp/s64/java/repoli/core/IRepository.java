package jp.s64.java.repoli.core;

import org.jetbrains.annotations.NotNull;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IRepository<TB, AB> {

    @NotNull <T extends TB, A extends AB> IRepositoryDataContainer<T, A> get(@NotNull IDataKey<T, A> key, @NotNull IStorage<TB, AB> storage, @NotNull IExpirePolicy<TB, AB> policy, @NotNull IProvider<TB, AB> provider);

    @NotNull <T extends TB, A extends AB> int remove(@NotNull IDataKey<T, A> key, @NotNull IStorage<TB, AB> storage, @NotNull IRemovePolicy<TB, AB> policy);

}
