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

import org.jetbrains.annotations.NotNull;

import io.reactivex.Single;
import jp.s64.java.repoli.base.BaseRepository;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IProvider;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepository;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.IStorage;
import jp.s64.java.repoli.rxjava2.core.IRxProvider;
import jp.s64.java.repoli.rxjava2.core.IRxRepository;
import jp.s64.java.repoli.rxjava2.core.IRxStorage;

public class BaseRxRepository<TB, AB> implements IRxRepository<TB, AB>, IRepository<TB, AB> {

    @NotNull
    private final IRepository repository;

    private final RxRepositoryHelper<TB, AB> helper = new RxRepositoryHelper<>();

    public BaseRxRepository() {
        repository = new BaseRepository() {
        };
    }

    public BaseRxRepository(@NotNull IRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends TB, A extends AB> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key, IStorage<TB, AB> storage, IExpirePolicy<TB, AB> policy, IProvider<TB, AB> provider) {
        return repository.get(key, storage, policy, provider);
    }

    @Override
    public <T extends TB, A extends AB> int remove(IDataKey<T, A> key, IStorage<TB, AB> storage, IRemovePolicy<TB, AB> policy) {
        return repository.remove(key, storage, policy);
    }

    @Override
    public <T extends TB, A extends AB> Single<IRepositoryDataContainer<T, A>> get(IDataKey<T, A> key, IRxStorage<TB, AB> storage, IExpirePolicy<TB, AB> policy, IRxProvider<TB, AB> provider) {
        return helper.get(key, storage, policy, provider);
    }

    @Override
    public <T extends TB, A extends AB> Single<Integer> remove(IDataKey<T, A> key, IRxStorage<TB, AB> storage, IRemovePolicy<TB, AB> policy) {
        return helper.remove(key, storage, policy);
    }

}
