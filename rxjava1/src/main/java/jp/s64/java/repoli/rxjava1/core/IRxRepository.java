package jp.s64.java.repoli.rxjava1.core;

import org.jetbrains.annotations.NotNull;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import rx.Observable;

/**
 * Created by shuma on 2017/05/26.
 */

public interface IRxRepository<TB, AB> {

    @NotNull <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> get(@NotNull IDataKey<T, A> key, @NotNull IRxStorage<TB, AB> storage, @NotNull IExpirePolicy<TB, AB> policy, @NotNull IRxProvider<TB, AB> provider);

    @NotNull <T extends TB, A extends AB> Observable<Integer> remove(@NotNull IDataKey<T, A> key, @NotNull IRxStorage<TB, AB> storage, @NotNull IRemovePolicy<TB, AB> policy);

}
