package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IRepository {

    <T, A> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key, IStorage storage, IExpirePolicy policy, IProvider provider);

    <T, A> int remove(IDataKey<T, A> key, IStorage storage, IRemovePolicy policy);

}
