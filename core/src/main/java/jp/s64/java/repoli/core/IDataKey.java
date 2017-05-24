package jp.s64.java.repoli.core;

import com.google.common.reflect.TypeToken;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IDataKey<T, A> {

    TypeToken<T> getBodyType();

    TypeToken<A> getAttachmentType();

    String getSerialized();

    String getRelatedKey();

}
