package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IDataKey<T, A> {

    TypeReference<T> getBodyClass();

    TypeReference<A> getAttachmentClass();

    String getSerialized();

    String getRelatedKey();

}
