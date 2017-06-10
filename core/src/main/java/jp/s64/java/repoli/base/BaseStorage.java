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

package jp.s64.java.repoli.base;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.ISerializerUser;
import jp.s64.java.repoli.core.IStorage;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;

public abstract class BaseStorage<TB, AB> implements IStorage<TB, AB>, ISerializerUser {

    private final StorageHelper helper = new StorageHelper();

    @Override
    public void addSerializer(@NotNull ISerializer serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(@NotNull ISerializer serializer) {
        helper.removeSerializer(serializer);
    }

    @Override
    public void addSerializer(@NotNull Collection<ISerializer> serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(@NotNull Collection<ISerializer> serializer) {
        helper.removeSerializer(serializer);
    }

    @Override
    public void clearSerializer() {
        helper.clearSerializer();
    }

    @NotNull
    @Override
    public <T extends TB, A extends AB> IRepositoryDataContainer<T, A> get(@NotNull IDataKey<T, A> key) {
        IRepositoryDataContainer<byte[], byte[]> raw;
        {
            raw = new ReturningRepositoryDataContainer<>(getBySerializedKey(key.getSerialized()));
        }
        return helper.convertBytesToReturning(key, raw);
    }

    @NotNull
    @Override
    public int remove(@NotNull IDataKey<?, ?> key) {
        return removeBySerializedKey(key.getSerialized());
    }

    @NotNull
    @Override
    public int removeRelatives(@NotNull IDataKey<?, ?> key) {
        return removeRelativesByRelatedKey(key.getRelatedKey());
    }

    @NotNull
    @Override
    public <T extends TB, A extends AB> IRepositoryDataContainer<T, A> save(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<T, A> original) {
        ReturningRepositoryDataContainer<T, A> container = new ReturningRepositoryDataContainer<>(original);
        {
            container.setSavedAtTimeMillis(System.currentTimeMillis());
        }
        IRepositoryDataContainer<byte[], byte[]> save = helper.convertContainerToBytes(key, container);
        {
            saveBySerializedKey(key.getSerialized(), key.getRelatedKey(), save);
        }
        return container;
    }

    @NotNull
    public abstract IRepositoryDataContainer<byte[], byte[]> getBySerializedKey(@NotNull String serializedKey);

    @NotNull
    public abstract int removeBySerializedKey(@NotNull String serializedKey);

    @NotNull
    public abstract int removeRelativesByRelatedKey(@NotNull String relatedKey);

    @NotNull
    public abstract void saveBySerializedKey(@NotNull String serializedKey, @NotNull String relatedKey, @NotNull IRepositoryDataContainer<byte[], byte[]> container);

}
