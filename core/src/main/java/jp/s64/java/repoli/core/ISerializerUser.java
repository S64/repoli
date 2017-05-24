package jp.s64.java.repoli.core;

import java.util.Collection;

/**
 * Created by shuma on 2017/05/19.
 */

public interface ISerializerUser {

    void addSerializer(ISerializer serializer);

    void addSerializer(Collection<ISerializer> serializer);

    void removeSerializer(ISerializer serializer);

    void removeSerializer(Collection<ISerializer> serializer);

    void clearSerializer();

}
