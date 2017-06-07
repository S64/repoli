package jp.s64.java.repoli.rxjava1.core;

import org.jetbrains.annotations.NotNull;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import rx.Observable;

/**
 * Created by shuma on 2017/05/26.
 */

public interface IRxStorage<TB, AB> {

    @NotNull <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> getAsync(@NotNull IDataKey<T, A> key);

    @NotNull
    Observable<Integer> removeAsync(@NotNull IDataKey<?, ?> key);

    @NotNull
    Observable<Integer> removeRelativesAsync(@NotNull IDataKey<?, ?> key);

    @NotNull <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> saveAsync(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<T, A> container);

}
