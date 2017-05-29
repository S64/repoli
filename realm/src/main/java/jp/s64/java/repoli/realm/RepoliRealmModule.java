package jp.s64.java.repoli.realm;

import io.realm.annotations.RealmModule;
import jp.s64.java.repoli.realm.bin.BinaryStorageObject;

/**
 * Created by shuma on 2017/05/27.
 */

@RealmModule(
        library = true,
        classes = {
                BinaryStorageObject.class
        }
)
public class RepoliRealmModule {
}
