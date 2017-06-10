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

package jp.s64.java.repoli.preset;

import java.util.concurrent.TimeUnit;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

public class TimeBasedPolicy<TB, AB> implements IExpirePolicy<TB, AB>, IRemovePolicy<TB, AB> {

    private final long expireTimeMillis;

    public TimeBasedPolicy(long time, TimeUnit unit) {
        expireTimeMillis = unit.toMillis(time);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldExpire(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldExpireWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldRemoveWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldRequest(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldSave(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return !isExpired(container);
    }

    protected <T extends TB, A extends AB> boolean isExpired(IRepositoryDataContainer<T, A> container) {
        Long savedAt = container.getSavedAtTimeMillis();
        return savedAt == null || System.currentTimeMillis() > (savedAt + expireTimeMillis);
    }

}
