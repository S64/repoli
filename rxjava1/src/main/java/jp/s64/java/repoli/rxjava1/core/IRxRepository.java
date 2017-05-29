package jp.s64.java.repoli.rxjava1.core;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import rx.Observable;

/**
 * Created by shuma on 2017/05/26.
 */

public interface IRxRepository<TB, AB> {

    <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> get(IDataKey<T, A> key, IRxStorage<TB, AB> storage, IExpirePolicy<TB, AB> policy, IRxProvider<TB, AB> provider);

}
