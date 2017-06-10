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

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

public class DefaultPolicy<TB, AB> implements IExpirePolicy<TB, AB>, IRemovePolicy<TB, AB> {

    @Override
    public <T extends TB, A extends AB> boolean shouldExpire(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return container.getRequestedAtTimeMillis() == null || container.getSavedAtTimeMillis() == null;
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldExpireWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return container.getRequestedAtTimeMillis() == null && container.getSavedAtTimeMillis() != null;
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldRequest(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return shouldExpire(key, container) || (container.getBody() == null && container.getAttachment() == null);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldSave(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return container.getRequestedAtTimeMillis() != null && (container.getBody() != null || container.getAttachment() != null);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldRemoveWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return true;
    }

}
