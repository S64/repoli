package jp.s64.java.repoli.preset;

import java.util.concurrent.TimeUnit;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IExpirePolicy;
import jp.s64.java.repoli.core.IRemovePolicy;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/19.
 */

public class TimeBasedPolicy<TB, AB> implements IExpirePolicy<TB, AB>, IRemovePolicy<TB, AB> {

    private final long expireTimeMillis;

    public TimeBasedPolicy(long time, TimeUnit unit) {
        expireTimeMillis = unit.toMillis(time);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldExpire(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldExpireWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldRemoveWithRelatives(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldRequest(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return isExpired(container);
    }

    @Override
    public <T extends TB, A extends AB> boolean shouldSave(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return !isExpired(container);
    }

    protected <T extends TB, A extends AB> boolean isExpired(IRepositoryDataContainer<T, A> container) {
        Long savedAt = container.getSavedAtTimeMillis();
        return savedAt == null || System.currentTimeMillis() > (savedAt + expireTimeMillis);
    }

}
