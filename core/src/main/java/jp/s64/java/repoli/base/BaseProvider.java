package jp.s64.java.repoli.base;

import com.google.common.primitives.Bytes;

import java.util.List;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IProvider;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/22.
 */

public abstract class BaseProvider implements IProvider {

    private final SerializerUserHelper serializers = new SerializerUserHelper();

    @Override
    public void addSerializer(ISerializer serializer) {
        serializers.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(ISerializer serializer) {
        serializers.removeSerializer(serializer);
    }

    @Override
    public void clearSerializer() {
        serializers.clearSerializer();
    }

    @Override
    public <T, A> IRepositoryDataContainer<T, A> request(IDataKey<T, A> key) {
        ReturningRepositoryDataContainer<T, A> ret = new ReturningRepositoryDataContainer<>();
        ReturningRepositoryDataContainer<List<Byte>, List<Byte>> raw;
        {
            String serialized = key.getSerialized();
            raw = new ReturningRepositoryDataContainer<>(requestBySerializedKey(serialized));
        }
        {
            ret.setBody(
                    serializers.deserializeByClass(
                            key.getBodyClass(),
                            raw.getBody() != null ? Bytes.toArray(raw.getBody()) : new byte[]{}
                    )
            );
            ret.setAttachment(
                    serializers.deserializeByClass(
                            key.getAttachmentClass(),
                            raw.getAttachment() != null ? Bytes.toArray(raw.getAttachment()) : new byte[]{}
                    )
            );
        }
        {
            ret.setRequestedAtTimeMillis(raw.getRequestedAtTimeMillis());
            //ret.setSavedAtTimeMillis(raw.getSavedAtTimeMillis());
        }
        return ret;
    }

    public abstract IRepositoryDataContainer<List<Byte>, List<Byte>> requestBySerializedKey(String serializedKey);

}
