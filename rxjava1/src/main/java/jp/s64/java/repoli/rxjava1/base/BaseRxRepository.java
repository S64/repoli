package jp.s64.java.repoli.rxjava1.base;

import jp.s64.java.repoli.base.BaseRepository;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IProvider;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepository;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.IStorage;
import jp.s64.java.repoli.rxjava1.core.IRxProvider;
import jp.s64.java.repoli.rxjava1.core.IRxRepository;
import jp.s64.java.repoli.rxjava1.core.IRxStorage;
import rx.Observable;

/**
 * Created by shuma on 2017/05/26.
 */

public class BaseRxRepository implements IRxRepository, IRepository {

    private final IRepository repository;
    private final RxRepositoryHelper helper = new RxRepositoryHelper();

    public BaseRxRepository() {
        repository = new BaseRepository() {
        };
    }

    public BaseRxRepository(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T, A> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key, IStorage storage, IExpirePolicy policy, IProvider provider) {
        return repository.get(key, storage, policy, provider);
    }

    @Override
    public <T, A> int remove(IDataKey<T, A> key, IStorage storage, IRemovePolicy policy) {
        return repository.remove(key, storage, policy);
    }

    @Override
    public <T, A> Observable<IRepositoryDataContainer<T, A>> get(IDataKey<T, A> key, IRxStorage storage, IExpirePolicy policy, IRxProvider provider) {
        return helper.get(key, storage, policy, provider);
    }

}
