package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IStorage {

    <T, A> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key);

    int remove(IDataKey<?, ?> key);

    int removeRelatives(IDataKey<?, ?> key);

    <T, A> IRepositoryDataContainer<T, A> save(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

}
