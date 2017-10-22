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

import io.reactivex.Single;
import io.reactivex.functions.Function;
import jp.s64.java.repoli.base.BaseProvider;
import jp.s64.java.repoli.base.ProviderHelper;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.ISerializerUser;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.rxjava2.core.IRxProvider;

public abstract class BaseRxProvider implements IRxProvider<Object, Object>, ISerializerUser {

    private final ProviderHelper helper = new ProviderHelper();

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
    public <T, A> Single<IRepositoryDataContainer<T, A>> request(final IDataKey<T, A> key) {
        return requestBySerializedKey(key.getSerialized())
                .map(new Function<BaseProvider.ProvidedContainer, IRepositoryDataContainer<T, A>>() {
                    @Override
                    public IRepositoryDataContainer<T, A> apply(BaseProvider.ProvidedContainer bytes) throws Exception {
                        ReturningRepositoryDataContainer<byte[], byte[]> org = new ReturningRepositoryDataContainer<byte[], byte[]>();
                        {
                            org.setBody(bytes.getBody());
                            org.setAttachment(bytes.getAttachment());
                            org.setRequestedAtTimeMillis(bytes.getRequestedAtTimeMillis());
                        }
                        return helper.convertBytesToReturning(key, org);
                    }
                });
    }

    @NotNull
    public abstract Single<BaseProvider.ProvidedContainer> requestBySerializedKey(@NotNull String serializedKey);

}
