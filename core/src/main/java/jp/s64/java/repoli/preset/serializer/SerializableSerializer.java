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
import java.io.Serializable;
import java.util.Set;

import jp.s64.java.repoli.core.ISerializer;

public class SerializableSerializer implements ISerializer {

    public static SerializableSerializer INSTANCE = newInstance();

    public static SerializableSerializer newInstance() {
        return new SerializableSerializer();
    }

    protected SerializableSerializer() {

    }

    @Override
    public <T> T deserialize(TypeToken<T> type, byte[] serialized, Set<ISerializer> serializers) {
        ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        ObjectInputStream ois = null;

        T ret = null;
        if (serialized != null && serialized.length > 0) {
            try {
                ois = new ObjectInputStream(in);
                ret = (T) ois.readObject();
            } catch (IOException e) {
                throw new SerializableSerializerException(e);
            } catch (ClassNotFoundException e) {
                throw new SerializableSerializerException(e);
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                    in.close();
                } catch (IOException e) {
                    throw new SerializableSerializerException(e);
                }
            }
        }
        return ret;
    }

    @Override
    public <T> byte[] serialize(TypeToken<T> type, Object deserialized, Set<ISerializer> serializers) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            {
                oos.writeObject(deserialized);
                oos.flush();
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new SerializableSerializerException(e);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                out.close();
            } catch (IOException e) {
                throw new SerializableSerializerException(e);
            }
        }
    }

    @Override
    public boolean canSerialize(TypeToken<?> type) {
        return Serializable.class.isAssignableFrom(type.getRawType());
    }

    @Override
    public float getPriority() {
        return 3;
    }

    public static class SerializableSerializerException extends RuntimeException {

        public SerializableSerializerException(Throwable throwable) {
            super(throwable);
        }

    }

}
