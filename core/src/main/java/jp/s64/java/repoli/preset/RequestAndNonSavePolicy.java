package jp.s64.java.repoli.preset;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/22.
 */

public class RequestAndNonSavePolicy<TB, AB> extends ForceRequestPolicy<TB, AB> {

    @Override
    public <T extends TB, A extends AB> boolean shouldSave(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        //super.shouldSave(key, container);
        return false;
    }

}
