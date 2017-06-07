package jp.s64.java.repoli.core;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by shuma on 2017/05/19.
 */

public interface ISerializerUser {

    void addSerializer(@NotNull ISerializer serializer);

    void addSerializer(@NotNull Collection<ISerializer> serializer);

    void removeSerializer(@NotNull ISerializer serializer);

    void removeSerializer(@NotNull Collection<ISerializer> serializer);

    void clearSerializer();

}
