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

package jp.s64.java.repoli.rxjava2.preset;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.IStorage;
import jp.s64.java.repoli.preset.SimpleOnMemoryStorage;
import jp.s64.java.repoli.rxjava2.base.BaseRxStorage;

public class SimpleRxOnMemoryStorage<TB, AB> extends BaseRxStorage<TB, AB> implements IStorage<TB, AB> {

    private final SimpleOnMemoryStorage<TB, AB> synchronous = new SimpleOnMemoryStorage();

    @Override
    public Single<IRepositoryDataContainer<byte[], byte[]>> getBySerializedKey(String serializedKey) {
        return Single.just(synchronous.getBySerializedKey(serializedKey));
    }

    @Override
    public Single<Integer> removeBySerializedKey(String serializedKey) {
        return Single.just(synchronous.removeBySerializedKey(serializedKey));
    }

    @Override
    public Single<Integer> removeRelativesByRelatedKey(String relatedKey) {
        return Single.just(synchronous.removeRelativesByRelatedKey(relatedKey));
    }

    @Override
    public Completable saveBySerializedKey(final String serializedKey, final String relatedKey, final IRepositoryDataContainer<byte[], byte[]> container) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                synchronous.saveBySerializedKey(serializedKey, relatedKey, container);
            }
        });
    }

    @Override
    public <T extends TB, A extends AB> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key) {
        return synchronous.get(key);
    }

    @Override
    public int remove(IDataKey<?, ?> key) {
        return synchronous.remove(key);
    }

    @Override
    public int removeRelatives(IDataKey<?, ?> key) {
        return synchronous.removeRelatives(key);
    }

    @Override
    public <T extends TB, A extends AB> IRepositoryDataContainer<T, A> save(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return synchronous.save(key, container);
    }

}
