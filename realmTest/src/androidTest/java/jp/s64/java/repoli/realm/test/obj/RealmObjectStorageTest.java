package jp.s64.java.repoli.realm.test.obj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.guava.base.Supplier;
import android.support.test.espresso.core.deps.guava.base.Suppliers;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.realm.obj.RealmObjectStorage;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by shuma on 2017/05/29.
 */

public class RealmObjectStorageTest {

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
                                    .modules(new ObjRealmModule())
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
                if (!instance.isClosed() && !instance.isInTransaction()) {
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
        final Supplier<TestObject> orgBody = Suppliers.memoize(new Supplier<TestObject>() {
            @Override
            public TestObject get() {
                TestObject ret;
                instance.beginTransaction();
                {
                    ret = instance.createObject(TestObject.class);
                }
                instance.commitTransaction();
                return ret;
            }
        });
        final Void orgAttachment;
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertTrue(orgBody.get().isManaged());
                    instance.beginTransaction();
                    {
                        orgBody.get().setMyValue("body-value");
                    }
                    instance.commitTransaction();
                }
            });
            orgAttachment = null;
            {
                // no-op
            }
        }
        final ReturningRepositoryDataContainer<TestObject, Void> orgContainer = new ReturningRepositoryDataContainer<>();
        {
            orgContainer.setBody(orgBody.get());
            orgContainer.setAttachment(orgAttachment);
            orgContainer.setRequestedAtTimeMillis(System.currentTimeMillis());
        }
        RealmObjectStorage storage = new RealmObjectStorage() {

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

        {

            TestSubscriber<IRepositoryDataContainer<TestObject, Void>> sub = new TestSubscriber<>();
            storage.getAsync(orgBody.get().getKey()).subscribe(sub);

            sub.awaitTerminalEvent();

            sub.assertNoErrors();
            sub.assertValueCount(1);
            final IRepositoryDataContainer<TestObject, Void> ret = sub.getOnNextEvents().get(0);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertNull(ret.getBody());
                    assertNull(ret.getAttachment());
                }
            });
            assertNull(ret.getSavedAtTimeMillis());
            assertNull(ret.getRequestedAtTimeMillis());
        }

        {

            TestSubscriber<IRepositoryDataContainer<TestObject, Void>> sub = new TestSubscriber<>();
            storage.saveAsync(orgBody.get().getKey(), orgContainer).subscribe(sub);

            sub.awaitTerminalEvent();

            sub.assertNoErrors();
            sub.assertValueCount(1);
            final IRepositoryDataContainer<TestObject, Void> ret = sub.getOnNextEvents().get(0);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertEquals(orgBody.get().getMyValue(), ret.getBody().getMyValue());
                    assertEquals(orgAttachment, ret.getAttachment());
                }
            });
            assertNotEquals(orgContainer.getSavedAtTimeMillis(), ret.getSavedAtTimeMillis());
            assertEquals(orgContainer.getRequestedAtTimeMillis(), ret.getRequestedAtTimeMillis());
        }

        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RealmResults<TestObjectSavingClass> result = instance.where(TestObjectSavingClass.class).findAll();
                    assertEquals(
                            1,
                            result.size()
                    );
                    TestObjectSavingClass ret = result.first();
                    assertEquals(orgBody.get().getKey().getSerialized(), ret.getSerializedKey());
                    assertEquals(orgBody.get().getKey().getRelatedKey(), ret.getRelatedKey());
                }
            });
        }

        {
            TestSubscriber<IRepositoryDataContainer<TestObject, Void>> sub = new TestSubscriber<>();
            storage.getAsync(orgBody.get().getKey()).subscribe(sub);

            sub.awaitTerminalEvent();

            sub.assertNoErrors();
            sub.assertValueCount(1);
            final IRepositoryDataContainer<TestObject, Void> ret = sub.getOnNextEvents().get(0);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertEquals(orgContainer.getBody().getMyValue(), ret.getBody().getMyValue());
                    assertEquals(orgContainer.getAttachment(), ret.getAttachment());
                }
            });
            assertNotEquals(orgContainer.getSavedAtTimeMillis(), ret.getSavedAtTimeMillis());
            assertEquals(orgContainer.getRequestedAtTimeMillis(), ret.getRequestedAtTimeMillis());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.close();
            }
        });
    }

    protected void runOnUiThread(Runnable runnable) {
        InstrumentationRegistry
                .getInstrumentation()
                .runOnMainSync(runnable);
    }

}
