package jp.s64.java.repoli.realm.bin;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.KeepMember;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/27.
 */

@KeepMember
public class BinaryStorageObject extends RealmObject implements IRepositoryDataContainer<byte[], byte[]> {

    @PrimaryKey
    private String serializedKey;

    @Index
    private String relatedKey;

    private byte[] body;
    private byte[] attachment;

    private Long savedAtTimeMillis;
    private Long requestedAtTimeMillis;

    public String getSerializedKey() {
        return serializedKey;
    }

    public void setSerializedKey(String serializedKey) {
        this.serializedKey = serializedKey;
    }

    public String getRelatedKey() {
        return relatedKey;
    }

    public void setRelatedKey(String relatedKey) {
        this.relatedKey = relatedKey;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    @Override
    public Long getSavedAtTimeMillis() {
        return savedAtTimeMillis;
    }

    public void setSavedAtTimeMillis(Long savedAtTimeMillis) {
        this.savedAtTimeMillis = savedAtTimeMillis;
    }

    @Override
    public Long getRequestedAtTimeMillis() {
        return requestedAtTimeMillis;
    }

    public void setRequestedAtTimeMillis(Long requestedAtTimeMillis) {
        this.requestedAtTimeMillis = requestedAtTimeMillis;
    }

}
