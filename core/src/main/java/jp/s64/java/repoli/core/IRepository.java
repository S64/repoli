package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IRepository<TB, AB> {

    <T extends TB, A extends AB> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key, IStorage<TB, AB> storage, IExpirePolicy<TB, AB> policy, IProvider<TB, AB> provider);

    <T extends TB, A extends AB> int remove(IDataKey<T, A> key, IStorage<TB, AB> storage, IRemovePolicy<TB, AB> policy);

}
