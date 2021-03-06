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

package jp.s64.java.repoli.realm.test.bin;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.facebook.stetho.Stetho;
import com.google.common.reflect.TypeToken;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import jp.s64.java.repoli.core.DataKey;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.realm.RepoliRealmModule;
import jp.s64.java.repoli.realm.bin.BinaryStorageObject;
import jp.s64.java.repoli.realm.bin.RealmBinaryStorage;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class RealmBinaryStorageTest {

    private Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }

    private Realm instance;

    @Before
    public void setUp() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                {
                    Realm.init(getContext());
                    instance = Realm.getInstance(
                            new RealmConfiguration.Builder()
                                    .inMemory()
                                    .modules(new RepoliRealmModule())
                                    .build()
                    );
                }
                {
                    Stetho.initialize(
                            Stetho.newInitializerBuilder(getContext())
                                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(getContext()))
                                    .enableWebKitInspector(
                                            RealmInspectorModulesProvider
                                                    .builder(getContext())
                                                    .build()
                                    )
                                    .build()
                    );
                }
            }
        });
    }

    @After
    public void shutdown() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (!instance.isClosed()) {
                    instance.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.deleteAll();
                        }
                    });
                }
                instance.close();
                instance = null;
            }

        });
    }


    @Test
    public void test() {
        RealmBinaryStorage<String, String> storage = new RealmBinaryStorage<String, String>() {

            @Override
            public Realm getRealmInstance() {
                return instance;
            }

            @Override
            public boolean closeAfter() {
                return false;
            }

            @Override
            public Scheduler getScheduler() {
                return AndroidSchedulers.mainThread();
            }

        };

        final IDataKey<String, String> key = new DataKey<>(
                TypeToken.of(String.class), TypeToken.of(String.class),
                "serialized_key",
                "related_key"
        );

        {
            TestSubscriber<IRepositoryDataContainer<String, String>> sub = new TestSubscriber<>();
            storage.getAsync(key).subscribe(sub);

            sub.awaitTerminalEvent();

            sub.assertNoErrors();
            sub.assertValueCount(1);
            IRepositoryDataContainer<String, String> ret = sub.getOnNextEvents().get(0);

            assertNull(ret.getBody());
            assertNull(ret.getAttachment());
            assertNull(ret.getRequestedAtTimeMillis());
            assertNull(ret.getSavedAtTimeMillis());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    instance.stopWaitForChange();
                }
            });
        }

        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertEquals(
                            0,
                            instance.where(BinaryStorageObject.class).findAll().size()
                    );
                }
            });
        }

        final IRepositoryDataContainer<String, String> org;
        {
            org = new ReturningRepositoryDataContainer<String, String>() {{
                setBody("body");
                setAttachment("attachment");
                setRequestedAtTimeMillis(System.currentTimeMillis());
                setSavedAtTimeMillis(null);
            }};
        }

        Long savedAt;
        {
            TestSubscriber<IRepositoryDataContainer<String, String>> sub = new TestSubscriber<>();
            storage.saveAsync(key, org).subscribe(sub);

            sub.awaitTerminalEvent();

            sub.assertNoErrors();
            sub.assertValueCount(1);
            IRepositoryDataContainer<String, String> ret = sub.getOnNextEvents().get(0);

            assertEquals(org.getBody(), ret.getBody());
            assertEquals(org.getAttachment(), ret.getAttachment());
            assertEquals(org.getRequestedAtTimeMillis(), ret.getRequestedAtTimeMillis());
            assertNotEquals(org.getSavedAtTimeMillis(), ret.getSavedAtTimeMillis());

            savedAt = ret.getSavedAtTimeMillis();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    instance.stopWaitForChange();
                }
            });
        }

        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RealmResults<BinaryStorageObject> result = instance.where(BinaryStorageObject.class).findAll();
                    assertEquals(
                            1,
                            result.size()
                    );
                    BinaryStorageObject ret = result.first();
                    assertEquals(key.getSerialized(), ret.getSerializedKey());
                    assertEquals(key.getRelatedKey(), ret.getRelatedKey());
                }
            });
        }

        {
            TestSubscriber<IRepositoryDataContainer<String, String>> sub = new TestSubscriber<>();
            storage.getAsync(key).subscribe(sub);

            sub.awaitTerminalEvent();

            sub.assertNoErrors();
            sub.assertValueCount(1);
            IRepositoryDataContainer<String, String> ret = sub.getOnNextEvents().get(0);

            assertEquals(org.getBody(), ret.getBody());
            assertEquals(org.getAttachment(), ret.getAttachment());
            assertEquals(org.getRequestedAtTimeMillis(), ret.getRequestedAtTimeMillis());
            assertEquals(savedAt, ret.getSavedAtTimeMillis());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    instance.stopWaitForChange();
                }
            });
        }
    }

    protected void runOnUiThread(Runnable runnable) {
        InstrumentationRegistry
                .getInstrumentation()
                .runOnMainSync(runnable);
    }

}
