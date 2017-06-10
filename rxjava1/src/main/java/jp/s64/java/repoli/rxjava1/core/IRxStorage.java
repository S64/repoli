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
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import rx.Observable;

public interface IRxStorage<TB, AB> {

    @NotNull
    <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> getAsync(@NotNull IDataKey<T, A> key);

    @NotNull
    Observable<Integer> removeAsync(@NotNull IDataKey<?, ?> key);

    @NotNull
    Observable<Integer> removeRelativesAsync(@NotNull IDataKey<?, ?> key);

    @NotNull
    <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> saveAsync(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<T, A> container);

}
