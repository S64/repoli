package jp.s64.java.repoli.preset;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/22.
 */

public class ForceRequestPolicy<TB, AB> extends DefaultPolicy<TB, AB> {

    @Override
    public <T extends TB, A extends AB> boolean shouldRequest(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        // super.shouldRequest(key, container);
        return true;
    }

}
