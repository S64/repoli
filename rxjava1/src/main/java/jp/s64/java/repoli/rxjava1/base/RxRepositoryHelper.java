package jp.s64.java.repoli.rxjava1.base;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.rxjava1.core.IRxProvider;
import jp.s64.java.repoli.rxjava1.core.IRxRepository;
import jp.s64.java.repoli.rxjava1.core.IRxStorage;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by shuma on 2017/05/26.
 */

public class RxRepositoryHelper implements IRxRepository {

    @Override
    public <T, A> Observable<IRepositoryDataContainer<T, A>> get(IDataKey<T, A> key, IRxStorage storage, IExpirePolicy policy, IRxProvider provider) {
        return storage.getAsync(key)
                //.onErrorResumeNext(Observable.empty())
                .map(new Func1<IRepositoryDataContainer<T, A>, ReturningRepositoryDataContainer<T, A>>() {
                    @Override
                    public ReturningRepositoryDataContainer<T, A> call(IRepositoryDataContainer<T, A> org) {
                        //return org != null ? new ReturningRepositoryDataContainer<>(org) : new ReturningRepositoryDataContainer<>();
                        return new ReturningRepositoryDataContainer<>(org);
                    }
                })
                .flatMap(new Func1<ReturningRepositoryDataContainer<T, A>, Observable<ReturningRepositoryDataContainer<T, A>>>() {
                    @Override
                    public Observable<ReturningRepositoryDataContainer<T, A>> call(ReturningRepositoryDataContainer<T, A> container) {
                        Observable<Integer> obs;
                        if (policy.shouldExpire(key, container)) {
                            {
                                container.setBody(null);
                                container.setAttachment(null);
                                container.setSavedAtTimeMillis(null);
                                container.setRequestedAtTimeMillis(null);
                            }
                            obs = storage.removeAsync(key);
                        } else {
                            obs = Observable.just(null);
                        }
                        return obs//.onErrorResumeNext(Observable.empty())
                                .map(new Func1<Integer, ReturningRepositoryDataContainer<T, A>>() {
                                    @Override
                                    public ReturningRepositoryDataContainer<T, A> call(Integer removed) {
                                        return container;
                                    }
                                });
                    }
                })
                .flatMap(new Func1<ReturningRepositoryDataContainer<T, A>, Observable<ReturningRepositoryDataContainer<T, A>>>() {
                    @Override
                    public Observable<ReturningRepositoryDataContainer<T, A>> call(ReturningRepositoryDataContainer<T, A> container) {
                        Observable<Integer> obs;
                        if (policy.shouldExpireWithRelatives(key, container)) {
                            obs = storage.removeRelativesAsync(key);
                        } else {
                            obs = Observable.just(null);
                        }
                        return obs//.onErrorResumeNext(Observable.empty())
                                .map(new Func1<Integer, ReturningRepositoryDataContainer<T, A>>() {
                                    @Override
                                    public ReturningRepositoryDataContainer<T, A> call(Integer removed) {
                                        return container;
                                    }
                                });
                    }
                })
                .flatMap(new Func1<ReturningRepositoryDataContainer<T, A>, Observable<ReturningRepositoryDataContainer<T, A>>>() {

                    @Override
                    public Observable<ReturningRepositoryDataContainer<T, A>> call(ReturningRepositoryDataContainer<T, A> container) {
                        Observable<IRepositoryDataContainer<T, A>> obs;
                        if (policy.shouldRequest(key, container)) {
                            obs = provider.request(key);
                        } else {
                            obs = Observable.just(container);
                        }
                        return obs
                                //.onErrorResumeNext(Observable.empty())
                                .map(new Func1<IRepositoryDataContainer<T, A>, ReturningRepositoryDataContainer<T, A>>() {
                                    @Override
                                    public ReturningRepositoryDataContainer<T, A> call(IRepositoryDataContainer<T, A> container) {
                                        return new ReturningRepositoryDataContainer<>(container);
                                    }
                                });
                    }

                })
                .flatMap(new Func1<ReturningRepositoryDataContainer<T, A>, Observable<ReturningRepositoryDataContainer<T, A>>>() {
                    @Override
                    public Observable<ReturningRepositoryDataContainer<T, A>> call(ReturningRepositoryDataContainer<T, A> container) {
                        Observable<IRepositoryDataContainer<T, A>> obs;
                        if (policy.shouldSave(key, container)) {
                            obs = storage.saveAsync(key, container);
                        } else {
                            obs = Observable.just(container);
                        }
                        return obs
                                //.onErrorResumeNext(Observable.empty())
                                .map(new Func1<IRepositoryDataContainer<T, A>, ReturningRepositoryDataContainer<T, A>>() {
                                    @Override
                                    public ReturningRepositoryDataContainer<T, A> call(IRepositoryDataContainer<T, A> container) {
                                        return new ReturningRepositoryDataContainer<T, A>(container);
                                    }
                                });
                    }
                });
    }

}
