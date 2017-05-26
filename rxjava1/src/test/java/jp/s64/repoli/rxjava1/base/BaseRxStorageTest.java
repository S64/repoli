package jp.s64.repoli.rxjava1.base;

import com.google.common.reflect.TypeToken;

import org.junit.Test;

import jp.s64.java.repoli.core.DataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.preset.serializer.SerializableSerializer;
import jp.s64.java.repoli.rxjava1.preset.SimpleRxOnMemoryStorage;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

/**
 * Created by shuma on 2017/05/26.
 */

public class BaseRxStorageTest {

    @Test
    public void test() {
        final DataKey<String, String> key = new DataKey<>(TypeToken.of(String.class), TypeToken.of(String.class), "", "");
        final String bodyValue, attachmentValue;
        final ReturningRepositoryDataContainer<String, String> org = new ReturningRepositoryDataContainer<>();
        {
            SerializableSerializer serializer = SerializableSerializer.INSTANCE;
            org.setBody("body-value");
            org.setAttachment("attachment-value");
        }

        SimpleRxOnMemoryStorage storage = new SimpleRxOnMemoryStorage();

        {
            TestSubscriber<IRepositoryDataContainer<String, String>> sub;
            storage.saveAsync(key, org).subscribe(sub = new TestSubscriber<IRepositoryDataContainer<String, String>>());

            sub.awaitTerminalEvent();

            sub.assertNoErrors();
            sub.assertValueCount(1);
            IRepositoryDataContainer<String, String> ret = sub.getOnNextEvents().get(0);

            assertEquals(org.getBody(), ret.getBody());
            assertEquals(org.getAttachment(), ret.getAttachment());
        }

        {
            TestSubscriber<IRepositoryDataContainer<String, String>> sub;
            storage.getAsync(key).subscribe(sub = new TestSubscriber<IRepositoryDataContainer<String, String>>());

            sub.awaitTerminalEvent();
            sub.assertNoErrors();
            sub.assertValueCount(1);

            IRepositoryDataContainer<String, String> ret = sub.getOnNextEvents().get(0);

            assertEquals(org.getBody(), ret.getBody());
            assertEquals(org.getAttachment(), ret.getAttachment());
        }
    }

}
