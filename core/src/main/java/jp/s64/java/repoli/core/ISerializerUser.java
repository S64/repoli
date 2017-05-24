package jp.s64.java.repoli.core;

/**
 * Created by shuma on 2017/05/19.
 */

public interface ISerializerUser {

    void addSerializer(ISerializer serializer);

    void removeSerializer(ISerializer serializer);

    void clearSerializer();

}
