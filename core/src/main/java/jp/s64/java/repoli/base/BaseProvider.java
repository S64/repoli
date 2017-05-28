package jp.s64.java.repoli.base;

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

public abstract class BaseProvider implements IProvider, ISerializerUser {

    private final ProviderHelper helper = new ProviderHelper();

    @Override
    public void addSerializer(ISerializer serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(ISerializer serializer) {
        helper.removeSerializer(serializer);
    }

    @Override
    public void addSerializer(Collection<ISerializer> serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(Collection<ISerializer> serializer) {
        helper.removeSerializer(serializer);
    }

    @Override
    public void clearSerializer() {
        helper.clearSerializer();
    }

    @Override
    public <T, A> IRepositoryDataContainer<T, A> request(IDataKey<T, A> key) {
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

    public abstract ProvidedContainer requestBySerializedKey(String serializedKey);

    public static class ProvidedContainer {

        private byte[] body;
        private byte[] attachment;
        private Long requestedAtTimeMillis;

        public ProvidedContainer(Long requestedAtTimeMillis) {
            setRequestedAtTimeMillis(requestedAtTimeMillis);
        }

        public byte[] getBody() {
            return body;
        }

        public void setBody(byte[] body) {
            this.body = body;
        }

        public byte[] getAttachment() {
            return attachment;
        }

        public void setAttachment(byte[] attachment) {
            this.attachment = attachment;
        }

        public Long getRequestedAtTimeMillis() {
            return requestedAtTimeMillis;
        }

        public void setRequestedAtTimeMillis(Long requestedAtTimeMillis) {
            this.requestedAtTimeMillis = requestedAtTimeMillis;
        }

    }

}
