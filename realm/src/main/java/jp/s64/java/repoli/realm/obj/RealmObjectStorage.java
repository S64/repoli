package jp.s64.java.repoli.realm.obj;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.realm.core.IRealmStorage;
import jp.s64.java.repoli.rxjava1.core.IRxStorage;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by shuma on 2017/05/29.
 */

public abstract class RealmObjectStorage implements IRxStorage, IRealmStorage {

    @Override
    public <T, A> Observable<IRepositoryDataContainer<T, A>> getAsync(final IDataKey<T, A> key) {
        final Supplier<Realm> realm;
        final Class<? extends SavingObject<?, ?>> clazz;
        {
            realm = Suppliers.memoize(new Supplier<Realm>() {
                @Override
                public Realm get() {
                    return getRealmInstance();
                }
            });
            clazz = getOrFailSavingClass(key);
        }

        return Observable
                .<Void>just(null)
                .observeOn(getScheduler())
                .map(new Func1<Void, RealmResults<? extends SavingObject<?, ?>>>() {

                    @Override
                    public RealmResults<? extends SavingObject<?, ?>> call(Void _) {
                        return realm.get()
                                .where(clazz)
                                .equalTo("serializedKey", key.getSerialized())
                                .findAll();
                    }

                })
                .map(new Func1<RealmResults<? extends SavingObject<?, ?>>, IRepositoryDataContainer<T, A>>() {
                    @Override
                    public IRepositoryDataContainer<T, A> call(RealmResults<? extends SavingObject<?, ?>> rows) {
                        ReturningRepositoryDataContainer<T, A> ret = new ReturningRepositoryDataContainer<>();
                        if (rows.size() > 0) {
                            SavingObject<?, ?> obj = rows.first();
                            {
                                ret.setBody((T) obj.getBody());
                                ret.setAttachment((A) obj.getAttachment());
                                ret.setRequestedAtTimeMillis(obj.getRequestedAtTimeMillis());
                                ret.setSavedAtTimeMillis(obj.getSavedAtTimeMillis());
                            }
                        }
                        return ret;
                    }
                })
                .doOnNext(new Action1<IRepositoryDataContainer<T, A>>() {
                    @Override
                    public void call(IRepositoryDataContainer<T, A> _) {
                        if (closeAfter()) realm.get().close();
                    }
                });
    }

    @Override
    public Observable<Integer> removeAsync(final IDataKey<?, ?> key) {
        final Supplier<Realm> realm;
        final Class<? extends SavingObject<?, ?>> clazz;
        {
            realm = Suppliers.memoize(new Supplier<Realm>() {
                @Override
                public Realm get() {
                    return getRealmInstance();
                }
            });
            clazz = getOrFailSavingClass(key);
        }

        return Observable
                .<Void>just(null)
                .observeOn(getScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void _) {
                        realm.get().beginTransaction();
                    }
                })
                .map(new Func1<Void, RealmResults<? extends SavingObject<?, ?>>>() {

                    @Override
                    public RealmResults<? extends SavingObject<?, ?>> call(Void _) {
                        return realm.get()
                                .where(clazz)
                                .equalTo("serializedKey", key.getRelatedKey())
                                .findAll();
                    }

                })
                .map(new Func1<RealmResults<? extends SavingObject<?, ?>>, Integer>() {

                    @Override
                    public Integer call(RealmResults<? extends SavingObject<?, ?>> rows) {
                        int ret = rows.size();
                        {
                            rows.deleteAllFromRealm();
                        }
                        return ret;
                    }

                })
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer _) {
                        realm.get().commitTransaction();
                        if (closeAfter()) realm.get().close();
                    }
                });
    }

    @Override
    public Observable<Integer> removeRelativesAsync(final IDataKey<?, ?> key) {
        final Supplier<Realm> realm;
        final Class<? extends SavingObject<?, ?>> clazz;
        {
            realm = Suppliers.memoize(new Supplier<Realm>() {
                @Override
                public Realm get() {
                    return getRealmInstance();
                }
            });
            clazz = getOrFailSavingClass(key);
        }

        return Observable
                .<Void>just(null)
                .observeOn(getScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void _) {
                        realm.get().beginTransaction();
                    }
                })
                .map(new Func1<Void, RealmResults<? extends SavingObject<?, ?>>>() {

                    @Override
                    public RealmResults<? extends SavingObject<?, ?>> call(Void _) {
                        return realm.get()
                                .where(clazz)
                                .equalTo("relatedKey", key.getRelatedKey())
                                .findAll();
                    }

                })
                .map(new Func1<RealmResults<? extends SavingObject<?, ?>>, Integer>() {

                    @Override
                    public Integer call(RealmResults<? extends SavingObject<?, ?>> rows) {
                        int ret = rows.size();
                        {
                            rows.deleteAllFromRealm();
                        }
                        return ret;
                    }

                })
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer _) {
                        realm.get().commitTransaction();
                        if (closeAfter()) realm.get().close();
                    }
                });
    }

    @Override
    public <T, A> Observable<IRepositoryDataContainer<T, A>> saveAsync(final IDataKey<T, A> key, final IRepositoryDataContainer<T, A> rawContainer) {
        final Supplier<Realm> realm;
        final Class<? extends SavingObject<?, ?>> clazz;
        final ReturningRepositoryDataContainer<T, A> container;
        {
            realm = Suppliers.memoize(new Supplier<Realm>() {
                @Override
                public Realm get() {
                    return getRealmInstance();
                }
            });
            clazz = getOrFailSavingClass(key);
            container = new ReturningRepositoryDataContainer<>(rawContainer);
        }


        return Observable
                .<Void>just(null)
                .observeOn(getScheduler())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void _) {
                        container.setSavedAtTimeMillis(System.currentTimeMillis());
                    }
                })
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void _) {
                        realm.get().beginTransaction();
                    }
                })
                .map(new Func1<Void, SavingObject<?, ?>>() {
                    @Override
                    public SavingObject<?, ?> call(Void _) {
                        SavingObject<?, ?> saving = realm.get().createObject(clazz, key.getSerialized());
                        {
                            //saving.setSerializedKey(key.getSerialized());
                            saving.setRelatedKey(key.getRelatedKey());
                        }
                        {
                            saving.setRawBody(container.getBody());
                            saving.setRawAttachment(container.getAttachment());
                            saving.setRequestedAtTimeMillis(container.getRequestedAtTimeMillis());
                            saving.setSavedAtTimeMillis(container.getSavedAtTimeMillis());
                        }
                        return saving;
                    }
                })
                .doOnNext(new Action1<SavingObject<?, ?>>() {
                    @Override
                    public void call(SavingObject<?, ?> obj) {
                        realm.get().insertOrUpdate(obj);
                    }
                })
                .doOnNext(new Action1<SavingObject<?, ?>>() {
                    @Override
                    public void call(SavingObject<?, ?> obj) {
                        realm.get().commitTransaction();
                        if (closeAfter()) realm.get().close();
                    }
                })
                .map(new Func1<SavingObject<?, ?>, IRepositoryDataContainer<T, A>>() {
                    @Override
                    public IRepositoryDataContainer<T, A> call(SavingObject<?, ?> obj) {
                        return container; // succeed
                    }
                });
    }

    public static Class<? extends SavingObject<?, ?>> getOrFailSavingClass(Class<?> clazz) {
        SavingObjectClass annotated = null;

        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            if (annotation.annotationType().equals(SavingObjectClass.class)) {
                annotated = (SavingObjectClass) annotation;
                break;
            }
        }

        if (annotated == null) {
            throw new RealmObjectStorageException(
                    new IllegalArgumentException(
                            String.format(
                                    "Passed type `%s` is **NOT** declared a `@%s` annotation.",
                                    SavingObjectClass.class.getName(),
                                    clazz.getCanonicalName()
                            )
                    )
            );
        }

        int declaredCount = 0;
        for (Field field : annotated.value().getDeclaredFields()) {
            if (SavingObject.REQUIRED_FIELD_NAMES.contains(field.getName())) {
                declaredCount++;
            }
        }

        if (declaredCount != SavingObject.REQUIRED_FIELD_NAMES.size()) {
            throw new RealmObjectStorageException(
                    new IllegalStateException(
                            String.format(
                                    "`%s` is **NOT** declared required fields. `%s` is require below fields: %s",
                                    annotated.value().getCanonicalName(),
                                    RealmObjectStorage.class.getSimpleName(),
                                    SavingObject.REQUIRED_FIELD_NAMES.toArray()
                            )
                    )
            );
        }

        return annotated.value();
    }

    public static Class<? extends SavingObject<?, ?>> getOrFailSavingClass(IDataKey<?, ?> key) {
        try {
            return getOrFailSavingClass(key.getClass());
        } catch (RealmObjectStorageException e) {
            throw new RealmObjectStorageException(
                    e
            );
        }
    }

    public static class RealmObjectStorageException extends RuntimeException {
        public RealmObjectStorageException(Throwable throwable) {
            super(throwable);
        }
    }

}
