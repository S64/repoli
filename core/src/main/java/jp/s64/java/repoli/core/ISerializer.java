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

package jp.s64.java.repoli.core;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Set;

public interface ISerializer {

    Comparator<ISerializer> COMPARATOR = new SerializerComparator();

    @Nullable
    <T> T deserialize(@NotNull TypeToken<T> type, @Nullable byte[] serialized, @NotNull Set<ISerializer> serializers);

    @Nullable
    <T> byte[] serialize(@NotNull TypeToken<T> type, @Nullable Object deserialized, @NotNull Set<ISerializer> serializers);

    boolean canSerialize(@NotNull TypeToken<?> type);

    float getPriority();

    class SerializerComparator implements Comparator<ISerializer> {

        @Override
        public int compare(ISerializer o1, ISerializer o2) {
            return Float.compare(o1.getPriority(), o2.getPriority());
        }

    }

}
