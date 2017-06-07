package jp.s64.java.repoli.rxjava1.base;

import org.jetbrains.annotations.NotNull;

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

public class BaseRxRepository<TB, AB> implements IRxRepository<TB, AB>, IRepository<TB, AB> {

    @NotNull
    private final IRepository repository;

    private final RxRepositoryHelper<TB, AB> helper = new RxRepositoryHelper<>();

    public BaseRxRepository() {
        repository = new BaseRepository() {
        };
    }

    public BaseRxRepository(@NotNull IRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends TB, A extends AB> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key, IStorage<TB, AB> storage, IExpirePolicy<TB, AB> policy, IProvider<TB, AB> provider) {
        return repository.get(key, storage, policy, provider);
    }

    @Override
    public <T extends TB, A extends AB> int remove(IDataKey<T, A> key, IStorage<TB, AB> storage, IRemovePolicy<TB, AB> policy) {
        return repository.remove(key, storage, policy);
    }

    @Override
    public <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> get(IDataKey<T, A> key, IRxStorage<TB, AB> storage, IExpirePolicy<TB, AB> policy, IRxProvider<TB, AB> provider) {
        return helper.get(key, storage, policy, provider);
    }

    @Override
    public <T extends TB, A extends AB> Observable<Integer> remove(IDataKey<T, A> key, IRxStorage<TB, AB> storage, IRemovePolicy<TB, AB> policy) {
        return helper.remove(key, storage, policy);
    }

}
