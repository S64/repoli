package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IExpirePolicy<TB, AB> extends IPolicy {

    <T extends TB, A extends AB> boolean shouldExpire(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

    <T extends TB, A extends AB> boolean shouldExpireWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

    <T extends TB, A extends AB> boolean shouldRequest(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

    <T extends TB, A extends AB> boolean shouldSave(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

}
