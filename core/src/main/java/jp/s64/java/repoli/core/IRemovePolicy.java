package jp.s64.java.repoli.core;

import org.jetbrains.annotations.NotNull;

/**
 * Created by shuma on 2017/05/19.
 */

public interface IRemovePolicy<TB, AB> extends IPolicy {

    <T extends TB, A extends AB> boolean shouldRemoveWithRelatives(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<T, A> container);

}
