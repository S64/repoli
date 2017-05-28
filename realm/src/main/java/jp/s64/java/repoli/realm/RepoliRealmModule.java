package jp.s64.java.repoli.realm;

import io.realm.annotations.RealmModule;

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
