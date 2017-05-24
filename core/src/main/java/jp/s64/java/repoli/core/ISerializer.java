package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface ISerializer {

    <T> T deserialize(Class<T> clazz, byte[] serialized);

    <T> byte[] serialize(Class<T> clazz, T deserialized);

    boolean canSerialize(Class<?> clazz);

}
