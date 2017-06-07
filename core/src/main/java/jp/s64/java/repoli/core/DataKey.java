package jp.s64.java.repoli.core;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

/**
 * Created by shuma on 2017/05/26.
 */

public class DataKey<T, A> implements IDataKey<T, A> {

    @NotNull
    private final TypeToken<T> bodyType;

    @NotNull
    private final TypeToken<A> attachmentType;

    @NotNull
    private final String serializedKey;

    @NotNull
    private final String relatedKey;

    public DataKey(@NotNull TypeToken<T> bodyType, @NotNull TypeToken<A> attachmentType, @NotNull String serializedKey, @NotNull String relatedKey) {
        this.bodyType = bodyType;
        this.attachmentType = attachmentType;
        this.serializedKey = serializedKey;
        this.relatedKey = relatedKey;
    }

    @NotNull
    @Override
    public TypeToken getBodyType() {
        return bodyType;
    }

    @NotNull
    @Override
    public TypeToken getAttachmentType() {
        return attachmentType;
    }

    @NotNull
    @Override
    public String getSerialized() {
        return serializedKey;
    }

    @NotNull
    @Override
    public String getRelatedKey() {
        return relatedKey;
    }

}
