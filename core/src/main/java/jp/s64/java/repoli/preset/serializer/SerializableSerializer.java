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

/**
 * Created by shuma on 2017/05/26.
 */

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
    public int compareTo(ISerializer serializer) {
        if (ListSerializer.class.isInstance(serializer) || VoidSerializer.class.isInstance(serializer)) {
            return 1;
        }
        return -1;
    }

    public static class SerializableSerializerException extends RuntimeException {

        public SerializableSerializerException(Throwable throwable) {
            super(throwable);
        }

    }

}
