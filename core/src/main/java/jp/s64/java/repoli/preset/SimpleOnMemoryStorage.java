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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import jp.s64.java.repoli.base.BaseStorage;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;

public class SimpleOnMemoryStorage<TB, BA> extends BaseStorage<TB, BA> {

    private final Map<String, UUID> byKey = new HashMap<>();
    private final Map<String, Set<UUID>> byRelatedKey = new HashMap<>();

    private final Map<UUID, IRepositoryDataContainer<byte[], byte[]>> db = new HashMap<>();

    @Override
    public IRepositoryDataContainer<byte[], byte[]> getBySerializedKey(String serializedKey) {
        UUID uuid = byKey.get(serializedKey);
        IRepositoryDataContainer<byte[], byte[]> item = uuid != null ? db.get(uuid) : null;
        return item != null ? new ReturningRepositoryDataContainer<>(item) : new ReturningRepositoryDataContainer<byte[], byte[]>();
    }

    @Override
    public int removeBySerializedKey(String serializedKey) {
        if (byKey.containsKey(serializedKey)) {
            UUID uuid = byKey.get(serializedKey);
            return removeByInternalUuid(uuid, false);
        } else {
            return 0;
        }
    }

    protected int removeByInternalUuid(UUID uuid, boolean isRelatedRemoving) {
        int result;
        {
            result = db.containsKey(uuid) ? 1 : 0;
        }
        db.remove(uuid);
        if (!isRelatedRemoving) {
            for (String itrRelatedKey : byRelatedKey.keySet()) {
                Set<UUID> keys = byRelatedKey.get(itrRelatedKey);
                {
                    keys.remove(uuid);
                }
                byRelatedKey.put(itrRelatedKey, keys);
            }
        }
        return result;
    }

    @Override
    public int removeRelativesByRelatedKey(String relatedKey) {
        int result = 0;
        if (byRelatedKey.containsKey(relatedKey)) {
            Set<UUID> removes = new HashSet<>();
            Set<UUID> keys = byRelatedKey.get(relatedKey);
            for (UUID key : keys) {
                result += removeByInternalUuid(key, true);
                removes.add(key);
            }
            keys.removeAll(removes);
            byRelatedKey.put(relatedKey, keys);
        }
        return result;
    }

    @Override
    public void saveBySerializedKey(String serializedKey, String relatedKey, IRepositoryDataContainer<byte[], byte[]> container) {
        UUID oldId, newId;
        {
            newId = UUID.randomUUID();
            oldId = byKey.containsKey(serializedKey) ? byKey.get(serializedKey) : null;
        }
        for (String itrRelatedKey : byRelatedKey.keySet()) {
            Set<UUID> keys = byRelatedKey.get(itrRelatedKey);
            {
                keys.remove(oldId);
            }
            byRelatedKey.put(itrRelatedKey, keys);
        }
        if (!byRelatedKey.containsKey(relatedKey)) {
            byRelatedKey.put(relatedKey, new HashSet<UUID>());
        }
        {
            db.put(newId, container);
        }
        {
            byKey.put(serializedKey, newId);
            byRelatedKey.get(relatedKey).add(newId);
        }
    }

}
