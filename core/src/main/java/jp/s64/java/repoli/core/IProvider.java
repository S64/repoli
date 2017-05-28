package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IProvider {

    <T, A> IRepositoryDataContainer<T, A> request(IDataKey<T, A> key);

}
