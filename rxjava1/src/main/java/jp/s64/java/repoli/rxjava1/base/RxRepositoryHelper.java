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

package jp.s64.java.repoli.rxjava1.base;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.rxjava1.core.IRxProvider;
import jp.s64.java.repoli.rxjava1.core.IRxRepository;
import jp.s64.java.repoli.rxjava1.core.IRxStorage;
import rx.Observable;
import rx.functions.Func1;

public class RxRepositoryHelper<TB, AB> implements IRxRepository<TB, AB> {

    @Override
    public <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> get(final IDataKey<T, A> key, final IRxStorage<TB, AB> storage, final IExpirePolicy<TB, AB> policy, final IRxProvider<TB, AB> provider) {
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
                    public Observable<ReturningRepositoryDataContainer<T, A>> call(final ReturningRepositoryDataContainer<T, A> container) {
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
                    public Observable<ReturningRepositoryDataContainer<T, A>> call(final ReturningRepositoryDataContainer<T, A> container) {
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
                .flatMap(new Func1<IRepositoryDataContainer<T, A>, Observable<IRepositoryDataContainer<T, A>>>() {

                    @Override
                    public Observable<IRepositoryDataContainer<T, A>> call(IRepositoryDataContainer<T, A> container) {
                        Observable<IRepositoryDataContainer<T, A>> obs;
                        if (policy.shouldRequest(key, container)) {
                            obs = provider.request(key);
                        } else {
                            obs = Observable.just(container);
                        }
                        return obs
                                //.onErrorResumeNext(Observable.empty())
                                .map(new Func1<IRepositoryDataContainer<T, A>, IRepositoryDataContainer<T, A>>() {
                                    @Override
                                    public ReturningRepositoryDataContainer<T, A> call(IRepositoryDataContainer<T, A> container) {
                                        return new ReturningRepositoryDataContainer<>(container);
                                    }
                                });
                    }

                })
                .flatMap(new Func1<IRepositoryDataContainer<T, A>, Observable<IRepositoryDataContainer<T, A>>>() {
                    @Override
                    public Observable<IRepositoryDataContainer<T, A>> call(IRepositoryDataContainer<T, A> container) {
                        Observable<IRepositoryDataContainer<T, A>> obs;
                        if (policy.shouldSave(key, container)) {
                            obs = storage.saveAsync(key, container);
                        } else {
                            obs = Observable.just(container);
                        }
                        return obs
                                //.onErrorResumeNext(Observable.empty())
                                .map(new Func1<IRepositoryDataContainer<T, A>, IRepositoryDataContainer<T, A>>() {
                                    @Override
                                    public IRepositoryDataContainer<T, A> call(IRepositoryDataContainer<T, A> container) {
                                        return new ReturningRepositoryDataContainer<T, A>(container);
                                    }
                                });
                    }
                });
    }

    @Override
    public <T extends TB, A extends AB> Observable<Integer> remove(final IDataKey<T, A> key, final IRxStorage<TB, AB> storage, final IRemovePolicy<TB, AB> policy) {
        return storage.getAsync(key)
                .map(new Func1<IRepositoryDataContainer<T, A>, ReturningRepositoryDataContainer<T, A>>() {
                    @Override
                    public ReturningRepositoryDataContainer<T, A> call(IRepositoryDataContainer<T, A> org) {
                        return new ReturningRepositoryDataContainer<>(org);
                    }
                })
                .flatMap(new Func1<ReturningRepositoryDataContainer<T, A>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(ReturningRepositoryDataContainer<T, A> container) {
                        return policy.shouldRemoveWithRelatives(key, container) ? storage.removeRelativesAsync(key) : Observable.just(0);
                    }
                })
                .flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer count) {
                        return storage.removeAsync(key);
                    }
                });
    }

}
