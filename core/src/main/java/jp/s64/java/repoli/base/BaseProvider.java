package jp.s64.java.repoli.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IProvider;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.ISerializerUser;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/22.
 */

public abstract class BaseProvider<TB, AB> implements IProvider<TB, AB>, ISerializerUser {

    private final ProviderHelper helper = new ProviderHelper();

    @Override
    public void addSerializer(@NotNull ISerializer serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(@NotNull ISerializer serializer) {
        helper.removeSerializer(serializer);
    }

    @Override
    public void addSerializer(@NotNull Collection<ISerializer> serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(@NotNull Collection<ISerializer> serializer) {
        helper.removeSerializer(serializer);
    }

    @Override
    public void clearSerializer() {
        helper.clearSerializer();
    }

    @NotNull
    @Override
    public <T extends TB, A extends AB> IRepositoryDataContainer<T, A> request(@NotNull IDataKey<T, A> key) {
        ReturningRepositoryDataContainer<byte[], byte[]> raw;
        {
            ProvidedContainer ret = requestBySerializedKey(key.getSerialized());
            raw = new ReturningRepositoryDataContainer<>();
            {
                raw.setBody(ret.getBody());
                raw.setAttachment(ret.getAttachment());
                raw.setRequestedAtTimeMillis(ret.getRequestedAtTimeMillis());
            }
        }
        return helper.convertBytesToReturning(key, raw);
    }

    @NotNull
    public abstract ProvidedContainer requestBySerializedKey(@NotNull String serializedKey);

    public static class ProvidedContainer {

        @Nullable
        private byte[] body;

        @Nullable
        private byte[] attachment;

        @Nullable
        private Long requestedAtTimeMillis;

        public ProvidedContainer(@Nullable Long requestedAtTimeMillis) {
            setRequestedAtTimeMillis(requestedAtTimeMillis);
        }

        @Nullable
        public byte[] getBody() {
            return body;
        }

        public void setBody(@Nullable byte[] body) {
            this.body = body;
        }

        @Nullable
        public byte[] getAttachment() {
            return attachment;
        }

        public void setAttachment(@Nullable byte[] attachment) {
            this.attachment = attachment;
        }

        @Nullable
        public Long getRequestedAtTimeMillis() {
            return requestedAtTimeMillis;
        }

        public void setRequestedAtTimeMillis(@Nullable Long requestedAtTimeMillis) {
            this.requestedAtTimeMillis = requestedAtTimeMillis;
        }

    }

}
