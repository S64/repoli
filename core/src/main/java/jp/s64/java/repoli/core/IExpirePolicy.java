package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IExpirePolicy extends IPolicy {

    <T, A> boolean shouldExpire(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

    <T, A> boolean shouldExpireWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

    <T, A> boolean shouldRequest(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

    <T, A> boolean shouldSave(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

}
