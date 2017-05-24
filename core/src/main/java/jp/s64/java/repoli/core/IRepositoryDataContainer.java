package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IRepositoryDataContainer<T, A> {

    T getBody();

    A getAttachment();

    Long getSavedAtTimeMillis();

    Long getRequestedAtTimeMillis();

}
