package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IRemovePolicy<TB, AB> extends IPolicy {

    <T extends TB, A extends AB> boolean shouldRemoveWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container);

}
