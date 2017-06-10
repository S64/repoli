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

package jp.s64.repoli.rxjava1.base;

import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.s64.java.repoli.base.BaseProvider;
import jp.s64.java.repoli.core.DataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.preset.DefaultPolicy;
import jp.s64.java.repoli.preset.ForceRequestPolicy;
import jp.s64.java.repoli.preset.serializer.SerializableSerializer;
import jp.s64.java.repoli.rxjava1.base.BaseRxProvider;
import jp.s64.java.repoli.rxjava1.base.RxRepositoryHelper;
import jp.s64.java.repoli.rxjava1.core.IRxProvider;
import jp.s64.java.repoli.rxjava1.core.IRxStorage;
import jp.s64.java.repoli.rxjava1.preset.SimpleRxOnMemoryStorage;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

public class RxRepositoryHelperTest {

    private IRxStorage storage;

    @Before
    public void setUp() {
        storage = new SimpleRxOnMemoryStorage();
    }

    @After
    public void shutDown() {
        storage = null;
    }

    @Test
    public void test() {
        DataKey<String, String> key = new DataKey<>(TypeToken.of(String.class), TypeToken.of(String.class), "single-key", "relative-key");
        final String bodyValue, attachmentValue;
        final ReturningRepositoryDataContainer<byte[], byte[]> org = new ReturningRepositoryDataContainer<>();
        {
            SerializableSerializer serializer = SerializableSerializer.INSTANCE;
            org.setBody(serializer.serialize(key.getBodyType(), bodyValue = "body-value", Sets.<ISerializer>newHashSet()));
            org.setAttachment(serializer.serialize(key.getAttachmentType(), attachmentValue = "attachment-value", Sets.<ISerializer>newHashSet()));
        }

        RxRepositoryHelper helper = new RxRepositoryHelper();

        {
            IRxProvider provider = new BaseRxProvider() {
                @Override
                public Observable<BaseProvider.ProvidedContainer> requestBySerializedKey(String serializedKey) {
                    BaseProvider.ProvidedContainer ret = new BaseProvider.ProvidedContainer(System.currentTimeMillis());
                    {
                        ret.setBody(org.getBody());
                        ret.setAttachment(org.getAttachment());
                    }
                    return Observable.just(ret);
                }
            };

            TestSubscriber<IRepositoryDataContainer<String, String>> sub = new TestSubscriber<>();
            helper.get(key, storage, new DefaultPolicy(), provider).subscribe(sub);

            sub.awaitTerminalEvent();

            sub.assertNoErrors();
            sub.assertValueCount(1);

            IRepositoryDataContainer<String, String> model = sub.getOnNextEvents().get(0);

            assertEquals(bodyValue, model.getBody());
            assertEquals(attachmentValue, model.getAttachment());
        }

        IRxProvider provider = new BaseRxProvider() {
            @Override
            public Observable<BaseProvider.ProvidedContainer> requestBySerializedKey(String serializedKey) {
                throw new UnsupportedOperationException();
            }
        };

        {
            TestSubscriber<IRepositoryDataContainer<String, String>> sub = new TestSubscriber<>();
            helper.get(key, storage, new DefaultPolicy(), provider).subscribe(sub);

            sub.awaitTerminalEvent();

            sub.assertNoErrors();
            sub.assertValueCount(1);

            IRepositoryDataContainer<String, String> model = sub.getOnNextEvents().get(0);

            assertEquals(bodyValue, model.getBody());
            assertEquals(attachmentValue, model.getAttachment());
        }

        {
            TestSubscriber<IRepositoryDataContainer<String, String>> sub = new TestSubscriber<>();
            helper.get(key, storage, new ForceRequestPolicy(), provider).subscribe(sub);

            sub.awaitTerminalEvent();

            sub.assertError(UnsupportedOperationException.class);
            sub.assertValueCount(0);
        }
    }

}
