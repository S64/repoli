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

package jp.s64.java.repoli.preset.serializer;

import com.google.common.reflect.TypeToken;

import java.util.Set;

import jp.s64.java.repoli.core.ISerializer;

public class VoidSerializer implements ISerializer {

    public static VoidSerializer INSTANCE = newInstance();

    public static VoidSerializer newInstance() {
        return new VoidSerializer();
    }

    protected VoidSerializer() {

    }

    @Override
    public <T> T deserialize(TypeToken<T> type, byte[] serialized, Set<ISerializer> serializers) {
        return null;
    }

    @Override
    public <T> byte[] serialize(TypeToken<T> type, Object deserialized, Set<ISerializer> serializers) {
        return new byte[0];
    }

    @Override
    public boolean canSerialize(TypeToken<?> type) {
        return Void.class.isAssignableFrom(type.getRawType());
    }

    @Override
    public float getPriority() {
        return 0;
    }

}
