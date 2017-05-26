package jp.s64.java.repoli.base;

import java.util.Collection;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IProvider;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.internal.ByteArrayContainer;
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
    public void addSerializer(Collection<ISerializer> serializer) {
        serializers.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(Collection<ISerializer> serializer) {
        serializers.removeSerializer(serializer);
    }

    @Override
    public void clearSerializer() {
        serializers.clearSerializer();
    }

    @Override
    public <T, A> IRepositoryDataContainer<T, A> request(IDataKey<T, A> key) {
        ReturningRepositoryDataContainer<T, A> ret = new ReturningRepositoryDataContainer<>();
        ByteArrayContainer raw;
        {
            String serialized = key.getSerialized();
            raw = new ByteArrayContainer(requestBySerializedKey(serialized));
        }
        {
            ret.setBody(
                    serializers.deserializeByClass(
                            key.getBodyType(),
                            raw.getBody() != null ? raw.getBody() : new byte[0]
                    )
            );
            ret.setAttachment(
                    serializers.deserializeByClass(
                            key.getAttachmentType(),
                            raw.getAttachment() != null ? raw.getAttachment() : new byte[0]
                    )
            );
        }
        {
            ret.setRequestedAtTimeMillis(raw.getRequestedAtTimeMillis());
            //ret.setSavedAtTimeMillis(raw.getSavedAtTimeMillis());
        }
        return ret;
    }

    public abstract ByteArrayContainer requestBySerializedKey(String serializedKey);

}
