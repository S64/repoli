package jp.s64.java.repoli.realm.core;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;
import jp.s64.java.repoli.rxjava1.core.IRxStorage;
import rx.Scheduler;

/**
 * Created by shuma on 2017/05/28.
 */

public interface IRealmStorage<TB, TA> extends IRxStorage<TB, TA> {

    @NotNull
    @NonNull
    Realm getRealmInstance();

    boolean closeAfter();

    @NotNull
    @NonNull
    Scheduler getScheduler();

}
