package jp.s64.java.repoli.preset;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/22.
 */

public class DefaultPolicy<TB, AB> implements IExpirePolicy<TB, AB>, IRemovePolicy<TB, AB> {

    @Override
    public <T extends TB, A extends AB> boolean shouldExpire(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return container.getRequestedAtTimeMillis() == null || container.getSavedAtTimeMillis() == null;
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldExpireWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return container.getRequestedAtTimeMillis() == null && container.getSavedAtTimeMillis() != null;
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldRequest(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return shouldExpire(key, container) || (container.getBody() == null && container.getAttachment() == null);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldSave(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return container.getRequestedAtTimeMillis() != null && (container.getBody() != null || container.getAttachment() != null);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldRemoveWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return true;
    }

}
