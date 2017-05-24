package jp.s64.java.repoli.core;

import com.google.common.reflect.TypeToken;

import java.util.Set;

/**
 * Created by shuma on 2017/05/19.
 */

public interface ISerializer {

    <T> T deserialize(TypeToken<T> type, byte[] serialized, Set<ISerializer> serializers);

    <T> byte[] serialize(TypeToken<T> type, Object deserialized, Set<ISerializer> serializers);

    boolean canSerialize(TypeToken<?> type);

}
