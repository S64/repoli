package jp.s64.java.repoli.realm.core;

import io.realm.Realm;
import jp.s64.java.repoli.rxjava1.core.IRxStorage;
import rx.Scheduler;

/**
 * Created by shuma on 2017/05/28.
 */

public interface IRealmStorage extends IRxStorage {

    Realm getRealmInstance();

    boolean closeAfter();

    Scheduler getScheduler();

}
