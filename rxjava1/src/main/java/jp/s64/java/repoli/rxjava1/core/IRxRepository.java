package jp.s64.java.repoli.rxjava1.core;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import rx.Observable;

/**
 * Created by shuma on 2017/05/26.
 */

public interface IRxRepository {

    <T, A> Observable<IRepositoryDataContainer<T, A>> get(IDataKey<T, A> key, IRxStorage storage, IExpirePolicy policy, IRxProvider provider);

}
