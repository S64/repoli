package jp.s64.repoli.rxjava1.base;

import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.s64.java.repoli.core.DataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.internal.ByteArrayContainer;
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

/**
 * Created by shuma on 2017/05/26.
 */

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
        ByteArrayContainer org = new ByteArrayContainer();
        {
            SerializableSerializer serializer = SerializableSerializer.INSTANCE;
            org.setBody(serializer.serialize(key.getBodyType(), bodyValue = "body-value", Sets.newHashSet()));
            org.setAttachment(serializer.serialize(key.getAttachmentType(), attachmentValue = "attachment-value", Sets.newHashSet()));
        }

        RxRepositoryHelper helper = new RxRepositoryHelper();

        {
            IRxProvider provider = new BaseRxProvider() {
                @Override
                public Observable<ByteArrayContainer> requestBySerializedKey(String serializedKey) {
                    return Observable.just(org);
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
            public Observable<ByteArrayContainer> requestBySerializedKey(String serializedKey) {
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
