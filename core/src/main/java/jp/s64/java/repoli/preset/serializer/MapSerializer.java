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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jp.s64.java.repoli.base.SerializerUserHelper;
import jp.s64.java.repoli.core.ISerializer;

public class MapSerializer implements ISerializer {

    public static MapSerializer INSTANCE = newInstance();

    public static MapSerializer newInstance() {
        return new MapSerializer();
    }

    protected MapSerializer() {

    }

    @Override
    public <T> T deserialize(TypeToken<T> type, byte[] serialized, Set<ISerializer> serializers) {
        SerializerUserHelper helper = new SerializerUserHelper();
        {
            helper.addSerializer(serializers);
        }
        TypeToken<?> keyType = type.resolveType(type.getRawType().getTypeParameters()[0]);
        TypeToken<?> valueType = type.resolveType(type.getRawType().getTypeParameters()[1]);

        ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        ObjectInputStream ois = null;

        Map<byte[], byte[]> org = null;
        try {
            ois = new ObjectInputStream(in);
            org = (Map<byte[], byte[]>) ois.readObject();
        } catch (IOException e) {
            throw new MapSerializerException(e);
        } catch (ClassNotFoundException e) {
            throw new MapSerializerException(e);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                in.close();
            } catch (IOException e) {
                throw new MapSerializerException(e);
            }
        }

        Map<Object, Object> dest = null;

        if (org != null) {
            dest = new HashMap<>();
            for (byte[] key : org.keySet()) {
                byte[] value = org.get(key);
                dest.put(
                        helper.deserializeByClass(keyType, key),
                        helper.deserializeByClass(valueType, value)
                );
            }
        }

        return (T) dest;
    }

    @Override
    public <T> byte[] serialize(TypeToken<T> type, Object deserialized, Set<ISerializer> serializers) {
        SerializerUserHelper helper = new SerializerUserHelper();
        {
            helper.addSerializer(serializers);
        }
        TypeToken<?> keyType = type.resolveType(type.getRawType().getTypeParameters()[0]);
        TypeToken<?> valueType = type.resolveType(type.getRawType().getTypeParameters()[1]);

        Map<?, ?> map = (Map<?, ?>) deserialized;

        Map<byte[], byte[]> dest = new HashMap<>();
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            dest.put(
                    helper.serializeByClass(keyType, key),
                    helper.serializeByClass(valueType, value)
            );
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            {
                oos.writeObject(dest);
                oos.flush();
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new MapSerializerException(e);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                out.close();
            } catch (IOException e) {
                throw new MapSerializerException(e);
            }
        }
    }

    @Override
    public boolean canSerialize(TypeToken<?> type) {
        return Map.class.isAssignableFrom(type.getRawType());
    }

    @Override
    public float getPriority() {
        return 2;
    }

    public static class MapSerializerException extends RuntimeException {

        public MapSerializerException(Throwable throwable) {
            super(throwable);
        }

    }

}
