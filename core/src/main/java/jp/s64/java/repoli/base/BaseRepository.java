package jp.s64.java.repoli.base;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IProvider;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepository;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.IStorage;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/19.
 */

public abstract class BaseRepository implements IRepository {

    @Override
    public <T, A> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key, IStorage storage, IExpirePolicy policy, IProvider provider) {
        ReturningRepositoryDataContainer<T, A> container;
        {
            container = new ReturningRepositoryDataContainer<>(storage.get(key));
        }
        {
            if (policy.shouldExpire(key, container)) {
                {
                    storage.remove(key);
                }
                {
                    container.setBody(null);
                    container.setAttachment(null);
                    container.setSavedAtTimeMillis(null);
                    container.setRequestedAtTimeMillis(null);
                }
            }
            if (policy.shouldExpireWithRelatives(key, container)) {
                storage.removeRelatives(key);
            }
        }
        if (policy.shouldRequest(key, container)) {
            container = new ReturningRepositoryDataContainer<>(provider.request(key));
        }
        if (policy.shouldSave(key, container)) {
            container = new ReturningRepositoryDataContainer<>(storage.save(key, container));
        }
        return container;
    }

    @Override
    public <T, A> int remove(IDataKey<T, A> key, IStorage storage, IRemovePolicy policy) {
        int result = 0;
        ReturningRepositoryDataContainer<T, A> container;
        {
            container = new ReturningRepositoryDataContainer<>(storage.get(key));
        }
        if (policy.shouldRemoveWithRelatives(key, container)) {
            result += storage.removeRelatives(key);
        }
        {
            result += storage.remove(key);
        }
        return result;
    }

}
