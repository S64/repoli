package jp.s64.java.repoli.rxjava1.base;

import java.util.Collection;

import jp.s64.java.repoli.base.ProviderHelper;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.internal.ByteArrayContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.rxjava1.core.IRxProvider;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by shuma on 2017/05/26.
 */

public abstract class BaseRxProvider implements IRxProvider {

    private final ProviderHelper helper = new ProviderHelper();

    @Override
    public void addSerializer(ISerializer serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void addSerializer(Collection<ISerializer> serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(ISerializer serializer) {
        helper.removeSerializer(serializer);
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
    public <T, A> Observable<IRepositoryDataContainer<T, A>> request(final IDataKey<T, A> key) {
        return requestBySerializedKey(key.getSerialized())
                .map(new Func1<ByteArrayContainer, IRepositoryDataContainer<T, A>>() {
                    @Override
                    public IRepositoryDataContainer<T, A> call(ByteArrayContainer bytes) {
                        ReturningRepositoryDataContainer<T, A> ret = helper.convertBytesToReturning(key, bytes);
                        {
                            ret.setRequestedAtTimeMillis(System.currentTimeMillis());
                        }
                        return ret;
                    }
                });
    }

    public abstract Observable<ByteArrayContainer> requestBySerializedKey(String serializedKey);

}
