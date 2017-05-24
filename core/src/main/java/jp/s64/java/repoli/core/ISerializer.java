package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface ISerializer {

    <T> T deserialize(TypeReference<T> type, byte[] serialized);

    <T> byte[] serialize(TypeReference<T> type, T deserialized);

    boolean canSerialize(TypeReference<?> type);

}
