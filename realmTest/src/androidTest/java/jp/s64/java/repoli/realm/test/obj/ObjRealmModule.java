package jp.s64.java.repoli.realm.test.obj;

import io.realm.annotations.RealmModule;

/**
 * Created by shuma on 2017/05/29.
 */

@RealmModule(
        library = false,
        classes = {
                TestObject.class,
                TestObjectSavingClass.class
        }
)
public class ObjRealmModule {
}
