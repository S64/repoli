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

package jp.s64.java.repoli.core;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

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
