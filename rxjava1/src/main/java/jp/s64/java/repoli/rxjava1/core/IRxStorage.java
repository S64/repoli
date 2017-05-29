package jp.s64.java.repoli.rxjava1.core;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import rx.Observable;

/**
 * Created by shuma on 2017/05/26.
 */

public interface IRxStorage<TB, AB> {

    <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> getAsync(IDataKey<T, A> key);

    Observable<Integer> removeAsync(IDataKey<?, ?> key);

    Observable<Integer> removeRelativesAsync(IDataKey<?, ?> key);

    <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> saveAsync(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

}
