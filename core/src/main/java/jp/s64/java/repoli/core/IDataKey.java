package jp.s64.java.repoli.core;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IDataKey<T, A> {

    @NotNull
    TypeToken<T> getBodyType();

    @NotNull
    TypeToken<A> getAttachmentType();

    @NotNull
    String getSerialized();

    @NotNull
    String getRelatedKey();

}
