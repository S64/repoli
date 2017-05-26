package jp.s64.java.repoli.core;

import com.google.common.reflect.TypeToken;

/**
 * Created by shuma on 2017/05/26.
 */

public class DataKey<T, A> implements IDataKey<T, A> {

    private final TypeToken<T> bodyType;
    private final TypeToken<A> attachmentType;
    private final String serializedKey;
    private final String relatedKey;

    public DataKey(TypeToken<T> bodyType, TypeToken<A> attachmentType, String serializedKey, String relatedKey) {
        this.bodyType = bodyType;
        this.attachmentType = attachmentType;
        this.serializedKey = serializedKey;
        this.relatedKey = relatedKey;
    }

    @Override
    public TypeToken getBodyType() {
        return bodyType;
    }

    @Override
    public TypeToken getAttachmentType() {
        return attachmentType;
    }

    @Override
    public String getSerialized() {
        return serializedKey;
    }

    @Override
    public String getRelatedKey() {
        return relatedKey;
    }

}
