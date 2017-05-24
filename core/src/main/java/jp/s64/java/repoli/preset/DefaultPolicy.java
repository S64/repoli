package jp.s64.java.repoli.preset;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/22.
 */

public class DefaultPolicy implements IExpirePolicy, IRemovePolicy {

    @Override
    public <T, A> boolean shouldExpire(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return container.getRequestedAtTimeMillis() == null || container.getSavedAtTimeMillis() == null;
    }

    @Override
    public <T, A> boolean shouldExpireWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return container.getRequestedAtTimeMillis() == null && container.getSavedAtTimeMillis() != null;
    }

    @Override
    public <T, A> boolean shouldRequest(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return shouldExpire(key, container) || (container.getBody() == null && container.getAttachment() == null);
    }

    @Override
    public <T, A> boolean shouldSave(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return container.getRequestedAtTimeMillis() != null && (container.getBody() != null || container.getAttachment() != null);
    }

    @Override
    public <T, A> boolean shouldRemoveWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return true;
    }

}
