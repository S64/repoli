package jp.s64.java.repoli.preset;

import java.util.concurrent.TimeUnit;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/19.
 */

public class TimeBasedPolicy implements IExpirePolicy, IRemovePolicy {

    private final long expireTimeMillis;

    public TimeBasedPolicy(long time, TimeUnit unit) {
        expireTimeMillis = unit.toMillis(time);
    }

    @Override
    public <T, A> boolean shouldExpire(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T, A> boolean shouldExpireWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T, A> boolean shouldRemoveWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T, A> boolean shouldRequest(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T, A> boolean shouldSave(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return !isExpired(container);
    }

    protected <T, A> boolean isExpired(IRepositoryDataContainer<T, A> container) {
        Long savedAt = container.getSavedAtTimeMillis();
        return savedAt == null || System.currentTimeMillis() > (savedAt + expireTimeMillis);
    }

}
