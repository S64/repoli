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

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.rxjava2.core.IRxProvider;
import jp.s64.java.repoli.rxjava2.core.IRxRepository;
import jp.s64.java.repoli.rxjava2.core.IRxStorage;

public class RxRepositoryHelper<TB, AB> implements IRxRepository<TB, AB> {

    @Override
    public <T extends TB, A extends AB> Single<IRepositoryDataContainer<T, A>> get(final IDataKey<T, A> key, final IRxStorage<TB, AB> storage, final IExpirePolicy<TB, AB> policy, final IRxProvider<TB, AB> provider) {
        return storage.getAsync(key)
                .map(new Function<IRepositoryDataContainer<T, A>, ReturningRepositoryDataContainer<T, A>>() {
                    @Override
                    public ReturningRepositoryDataContainer<T, A> apply(IRepositoryDataContainer<T, A> org) throws Exception {
                        return new ReturningRepositoryDataContainer<>(org);
                    }
                })
                .flatMap(new Function<ReturningRepositoryDataContainer<T, A>, SingleSource<? extends ReturningRepositoryDataContainer<T, A>>>() {
                    @Override
                    public SingleSource<? extends ReturningRepositoryDataContainer<T, A>> apply(final ReturningRepositoryDataContainer<T, A> container) throws Exception {
                        Single<Integer> obs;
                        if (policy.shouldExpire(key, container)) {
                            {
                                container.setBody(null);
                                container.setAttachment(null);
                                container.setSavedAtTimeMillis(null);
                                container.setRequestedAtTimeMillis(null);
                            }
                            obs = storage.removeAsync(key);
                        } else {
                            obs = Single.just(null);
                        }
                        return obs
                                .map(new Function<Integer, ReturningRepositoryDataContainer<T, A>>() {
                                    @Override
                                    public ReturningRepositoryDataContainer<T, A> apply(Integer removed) throws Exception {
                                        return container;
                                    }
                                });
                    }
                })
                .flatMap(new Function<ReturningRepositoryDataContainer<T, A>, Single<ReturningRepositoryDataContainer<T, A>>>() {
                    @Override
                    public Single<ReturningRepositoryDataContainer<T, A>> apply(final ReturningRepositoryDataContainer<T, A> container) {
                        Single<Integer> obs;
                        if (policy.shouldExpireWithRelatives(key, container)) {
                            obs = storage.removeRelativesAsync(key);
                        } else {
                            obs = Single.just(null);
                        }
                        return obs
                                .map(new Function<Integer, ReturningRepositoryDataContainer<T, A>>() {
                                    @Override
                                    public ReturningRepositoryDataContainer<T, A> apply(Integer removed) {
                                        return container;
                                    }
                                });
                    }
                })
                .flatMap(new Function<IRepositoryDataContainer<T, A>, Single<IRepositoryDataContainer<T, A>>>() {

                    @Override
                    public Single<IRepositoryDataContainer<T, A>> apply(IRepositoryDataContainer<T, A> container) {
                        Single<IRepositoryDataContainer<T, A>> obs;
                        if (policy.shouldRequest(key, container)) {
                            obs = provider.request(key);
                        } else {
                            obs = Single.just(container);
                        }
                        return obs
                                .map(new Function<IRepositoryDataContainer<T, A>, IRepositoryDataContainer<T, A>>() {
                                    @Override
                                    public ReturningRepositoryDataContainer<T, A> apply(IRepositoryDataContainer<T, A> container) {
                                        return new ReturningRepositoryDataContainer<>(container);
                                    }
                                });
                    }

                })
                .flatMap(new Function<IRepositoryDataContainer<T, A>, Single<IRepositoryDataContainer<T, A>>>() {
                    @Override
                    public Single<IRepositoryDataContainer<T, A>> apply(IRepositoryDataContainer<T, A> container) {
                        Single<IRepositoryDataContainer<T, A>> obs;
                        if (policy.shouldSave(key, container)) {
                            obs = storage.saveAsync(key, container);
                        } else {
                            obs = Single.just(container);
                        }
                        return obs
                                .map(new Function<IRepositoryDataContainer<T, A>, IRepositoryDataContainer<T, A>>() {
                                    @Override
                                    public IRepositoryDataContainer<T, A> apply(IRepositoryDataContainer<T, A> container) {
                                        return new ReturningRepositoryDataContainer<T, A>(container);
                                    }
                                });
                    }
                });
    }

    @Override
    public <T extends TB, A extends AB> Single<Integer> remove(final IDataKey<T, A> key, final IRxStorage<TB, AB> storage, final IRemovePolicy<TB, AB> policy) {
        return storage.getAsync(key)
                .map(new Function<IRepositoryDataContainer<T, A>, ReturningRepositoryDataContainer<T, A>>() {
                    @Override
                    public ReturningRepositoryDataContainer<T, A> apply(IRepositoryDataContainer<T, A> org) {
                        return new ReturningRepositoryDataContainer<>(org);
                    }
                })
                .flatMap(new Function<ReturningRepositoryDataContainer<T, A>, Single<Integer>>() {
                    @Override
                    public Single<Integer> apply(ReturningRepositoryDataContainer<T, A> container) {
                        return policy.shouldRemoveWithRelatives(key, container) ? storage.removeRelativesAsync(key) : Single.just(0);
                    }
                })
                .flatMap(new Function<Integer, Single<Integer>>() {
                    @Override
                    public Single<Integer> apply(Integer count) {
                        return storage.removeAsync(key);
                    }
                });
    }

}
