package jp.s64.java.repoli.rxjava1.core;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import rx.Observable;

/**
 * Created by shuma on 2017/05/26.
 */

public interface IRxProvider<TB, AB> {

    <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> request(IDataKey<T, A> key);

}
