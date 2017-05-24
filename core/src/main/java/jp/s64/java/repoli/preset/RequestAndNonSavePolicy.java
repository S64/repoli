package jp.s64.java.repoli.preset;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/22.
 */

public class RequestAndNonSavePolicy extends ForceRequestPolicy {

    @Override
    public <T, A> boolean shouldSave(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        //super.shouldSave(key, container);
        return false;
    }

}
