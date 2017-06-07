package jp.s64.java.repoli.realm.bin;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @NotNull
    @NonNull
    @PrimaryKey
    private String serializedKey;

    @NotNull
    @NonNull
    @Index
    private String relatedKey;

    @Nullable
    @android.support.annotation.Nullable
    private byte[] body;

    @Nullable
    @android.support.annotation.Nullable
    private byte[] attachment;

    @Nullable
    @android.support.annotation.Nullable
    private Long savedAtTimeMillis;

    @Nullable
    @android.support.annotation.Nullable
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
