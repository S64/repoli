package jp.s64.java.repoli.preset.serializer;

import com.google.common.primitives.Bytes;
import com.google.common.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.s64.java.repoli.base.SerializerUserHelper;
import jp.s64.java.repoli.core.ISerializer;

/**
 * Created by shuma on 2017/05/24.
 */

public class ListSerializer implements ISerializer {

    public static ListSerializer INSTANCE = newInstance();

    public static ListSerializer newInstance() {
        return new ListSerializer();
    }

    protected ListSerializer() {

    }

    @Override
    public <T> T deserialize(TypeToken<T> type, byte[] serialized, Set<ISerializer> serializers) {
        SerializerUserHelper helper = new SerializerUserHelper();
        {
            helper.addSerializer(serializers);
        }
        TypeToken<?> innerType = type.resolveType(type.getRawType().getTypeParameters()[0]);

        ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        ObjectInputStream ois = null;

        List<List<Byte>> org = null;
        try {
            ois = new ObjectInputStream(in);
            org = (List<List<Byte>>) ois.readObject();
        } catch (IOException e) {
            throw new ListSerializerException(e);
        } catch (ClassNotFoundException e) {
            throw new ListSerializerException(e);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                in.close();
            } catch (IOException e) {
                // squash
            }
        }

        List<Object> dest = null;

        if (org != null) {
            dest = new ArrayList<>();
            for (List<Byte> item : org) {
                Object deserialized = helper.deserializeByClass(innerType, Bytes.toArray(item));
                dest.add(deserialized);
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
        List<List<Byte>> dest = new LinkedList<>();

        List<?> list = (List<?>) deserialized;
        TypeToken<?> innerType = type.resolveType(type.getRawType().getTypeParameters()[0]);

        for (Object item : list) {
            byte[] serialized = helper.serializeByClass(innerType, item);
            dest.add(Bytes.asList(serialized));
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
            throw new ListSerializerException(e);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                out.close();
            } catch (IOException e) {
                // squash
            }
        }
    }

    @Override
    public boolean canSerialize(TypeToken<?> type) {
        return List.class.isAssignableFrom(type.getRawType());
    }

    public static class ListSerializerException extends RuntimeException {

        public ListSerializerException(Throwable throwable) {
            super(throwable);
        }

    }

}
