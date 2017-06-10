/*
 * Copyright (C) 2017 Shuma Yoshioka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.s64.java.repoli.realm.bin;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.KeepMember;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

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
