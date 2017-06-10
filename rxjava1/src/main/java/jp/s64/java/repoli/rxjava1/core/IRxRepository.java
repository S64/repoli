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

package jp.s64.java.repoli.rxjava1.core;

import org.jetbrains.annotations.NotNull;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import rx.Observable;

public interface IRxRepository<TB, AB> {

    @NotNull
    <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> get(@NotNull IDataKey<T, A> key, @NotNull IRxStorage<TB, AB> storage, @NotNull IExpirePolicy<TB, AB> policy, @NotNull IRxProvider<TB, AB> provider);

    @NotNull
    <T extends TB, A extends AB> Observable<Integer> remove(@NotNull IDataKey<T, A> key, @NotNull IRxStorage<TB, AB> storage, @NotNull IRemovePolicy<TB, AB> policy);

}
