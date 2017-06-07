package jp.s64.java.repoli.rxjava1.core;

import org.jetbrains.annotations.NotNull;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import rx.Observable;

/**
 * Created by shuma on 2017/05/26.
 */

public interface IRxProvider<TB, AB> {

    @NotNull <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> request(@NotNull IDataKey<T, A> key);

}
