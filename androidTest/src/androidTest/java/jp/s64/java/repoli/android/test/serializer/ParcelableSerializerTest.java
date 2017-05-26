package jp.s64.java.repoli.android.test.serializer;

import android.support.test.espresso.core.deps.guava.primitives.Bytes;

import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jp.s64.java.repoli.android.serializer.ParcelableSerializer;
import jp.s64.java.repoli.android.test.model.ParcelableModel;
import jp.s64.java.repoli.base.BaseProvider;
import jp.s64.java.repoli.base.BaseRepository;
import jp.s64.java.repoli.core.IRepository;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.preset.DefaultPolicy;
import jp.s64.java.repoli.preset.ForceRequestPolicy;
import jp.s64.java.repoli.preset.SimpleOnMemoryStorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by shuma on 2017/05/22.
 */

public class ParcelableSerializerTest {

    private IRepository repository;
    private SimpleOnMemoryStorage storage;

    @Before
    public void setUp() {
        {
            repository = new BaseRepository() {
            };
            storage = new SimpleOnMemoryStorage();
        }
    }

    @After
    public void shutDown() {
        repository = null;
        storage = null;
    }

    @Ignore
    public void test() {
        final String group = "related_key_for_test";
        BaseParcelableProvider provider = new BaseParcelableProvider() {
        };
        storage.addSerializer(new ParcelableSerializer());

        ParcelableModel model1_org, model2_org;
        {
            {
                model1_org = new ParcelableModel(UUID.randomUUID().toString(), group);
                model2_org = new ParcelableModel(UUID.randomUUID().toString(), group);
            }
            provider.add(model1_org);
            provider.add(model2_org);
        }
        IRepositoryDataContainer<ParcelableModel, Void> model1_ret, model2_ret;
        {   // model1が正しくserialize / deserializeされることを確認し、キャッシュする
            IRepositoryDataContainer<ParcelableModel, Void> ret;
            ret = repository.get(
                    ParcelableModel.createKey(model1_org),
                    storage,
                    new DefaultPolicy(),
                    provider
            );
            assertEquals(ret.getBody().getUuid(), model1_org.getUuid());
            assertEquals(ret.getBody().getGroup(), model1_org.getGroup());
            assertNull(ret.getAttachment());
            assertNotNull(ret.getRequestedAtTimeMillis());
            assertNotNull(ret.getSavedAtTimeMillis());
            model1_ret = ret;
        }
        {   // model1が正しくserialize / deserializeされることを確認し、キャッシュする
            IRepositoryDataContainer<ParcelableModel, Void> ret;
            ret = repository.get(
                    ParcelableModel.createKey(model2_org),
                    storage,
                    new DefaultPolicy(),
                    provider
            );
            assertEquals(ret.getBody().getUuid(), model2_org.getUuid());
            assertEquals(ret.getBody().getGroup(), model2_org.getGroup());
            assertNull(ret.getAttachment());
            assertNotNull(ret.getRequestedAtTimeMillis());
            assertNotNull(ret.getSavedAtTimeMillis());
            model2_ret = ret;
        }
        {   // Providerから外してもキャッシュが効いていることを確認
            provider.remove(model1_org);
            {
                IRepositoryDataContainer<ParcelableModel, Void> ret = repository.get(
                        ParcelableModel.createKey(model1_org),
                        storage,
                        new DefaultPolicy(),
                        provider
                );
                assertEquals(model1_ret.getRequestedAtTimeMillis(), ret.getRequestedAtTimeMillis());
            }
            provider.add(model1_org);
        }
        {   // 強制リロードできることを確認
            IRepositoryDataContainer<ParcelableModel, Void> ret = repository.get(
                    ParcelableModel.createKey(model1_org),
                    storage,
                    new ForceRequestPolicy(),
                    provider
            );
            assertNotEquals(model1_ret.getRequestedAtTimeMillis(), ret.getRequestedAtTimeMillis());
            model1_ret = ret;
        }
        {   // model1を削除し、関連としてmodel2が削除されることを確認
            repository.remove(ParcelableModel.createKey(model1_org), storage, new DefaultPolicy());
            provider.remove(model2_org);
            {
                IRepositoryDataContainer<ParcelableModel, Void> ret = repository.get(
                        ParcelableModel.createKey(model2_org),
                        storage,
                        new DefaultPolicy(),
                        provider
                );
                assertNull(ret.getBody());
                assertNull(ret.getAttachment());
                assertNull(ret.getRequestedAtTimeMillis());
                assertNull(ret.getSavedAtTimeMillis());
            }
        }
    }

    public static class BaseParcelableProvider extends BaseProvider {

        private final ParcelableSerializer serializer = new ParcelableSerializer();
        private final Map<String, ParcelableModel> registered = new HashMap<>();

        public BaseParcelableProvider() {
            super();
            addSerializer(serializer);
        }

        public void add(ParcelableModel model) {
            registered.put(ParcelableModel.createKey(model).getSerialized(), model);
        }

        public void remove(ParcelableModel model) {
            registered.remove(ParcelableModel.createKey(model).getSerialized());
        }

        @Override
        public IRepositoryDataContainer<List<Byte>, List<Byte>> requestBySerializedKey(String serializedKey) {
            ReturningRepositoryDataContainer<List<Byte>, List<Byte>> ret = new ReturningRepositoryDataContainer<>();

            ParcelableModel original = registered.get(serializedKey);
            if (original == null) {
                return ret;
            }

            {
                ret.setBody(Bytes.asList(serializer.serialize(
                        new TypeToken<ParcelableModel>() {
                        },
                        original,
                        Sets.<ISerializer>newHashSet(serializer)
                )));
                ret.setAttachment(new ArrayList<Byte>());
            }
            {
                ret.setRequestedAtTimeMillis(System.currentTimeMillis());
            }

            return ret;
        }

    }

}
