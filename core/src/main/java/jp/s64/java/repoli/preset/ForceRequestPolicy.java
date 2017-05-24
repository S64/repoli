package jp.s64.java.repoli.preset;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/22.
 */

public class ForceRequestPolicy extends DefaultPolicy {

    @Override
    public <T, A> boolean shouldRequest(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        // super.shouldRequest(key, container);
        return true;
    }

}
