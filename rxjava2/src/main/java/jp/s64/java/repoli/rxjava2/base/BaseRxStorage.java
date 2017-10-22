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

package jp.s64.java.repoli.rxjava2.base;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import jp.s64.java.repoli.base.StorageHelper;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.ISerializerUser;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.rxjava2.core.IRxStorage;

public abstract class BaseRxStorage<TB, AB> implements IRxStorage<TB, AB>, ISerializerUser {

    private final StorageHelper helper = new StorageHelper();

    @Override
    public void addSerializer(ISerializer serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void addSerializer(Collection<ISerializer> serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(ISerializer serializer) {
        helper.removeSerializer(serializer);
    }

    @Override
    public void removeSerializer(Collection<ISerializer> serializer) {
        helper.removeSerializer(serializer);
    }

    @Override
    public void clearSerializer() {
        helper.clearSerializer();
    }

    @Override
    public <T extends TB, A extends AB> Single<IRepositoryDataContainer<T, A>> getAsync(final IDataKey<T, A> key) {
        return getBySerializedKey(key.getSerialized())
                .map(new Function<IRepositoryDataContainer<byte[], byte[]>, IRepositoryDataContainer<T, A>>() {
                    @Override
                    public IRepositoryDataContainer<T, A> apply(IRepositoryDataContainer<byte[], byte[]> bytes) throws Exception {
                        return helper.convertBytesToReturning(key, bytes);
                    }
                });
    }

    @Override
    public Single<Integer> removeAsync(IDataKey<?, ?> key) {
        return removeBySerializedKey(key.getSerialized());
    }

    @Override
    public Single<Integer> removeRelativesAsync(IDataKey<?, ?> key) {
        return removeRelativesByRelatedKey(key.getRelatedKey());
    }

    @Override
    public <T extends TB, A extends AB> Single<IRepositoryDataContainer<T, A>> saveAsync(IDataKey<T, A> key, IRepositoryDataContainer<T, A> original) {
        final ReturningRepositoryDataContainer<T, A> container = new ReturningRepositoryDataContainer<>(original);
        {
            container.setSavedAtTimeMillis(System.currentTimeMillis());
        }
        IRepositoryDataContainer<byte[], byte[]> save = helper.convertContainerToBytes(key, container);
        return saveBySerializedKey(key.getSerialized(), key.getRelatedKey(), save)
                .toSingle(new Callable<IRepositoryDataContainer<T, A>>() {
                    @Override
                    public IRepositoryDataContainer<T, A> call() throws Exception {
                        return container;
                    }
                });
    }

    @NotNull
    public abstract Single<IRepositoryDataContainer<byte[], byte[]>> getBySerializedKey(@NotNull String serializedKey);

    @NotNull
    public abstract Single<Integer> removeBySerializedKey(@NotNull String serializedKey);

    @NotNull
    public abstract Single<Integer> removeRelativesByRelatedKey(@NotNull String relatedKey);

    @NotNull
    public abstract Completable saveBySerializedKey(@NotNull String serializedKey, @NotNull String relatedKey, @NotNull IRepositoryDataContainer<byte[], byte[]> container);

}
