package jp.s64.java.repoli.realm.obj;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.realm.RealmModel;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/29.
 */

public interface SavingObject<T extends RealmModel, A extends RealmModel> extends IRepositoryDataContainer<T, A>, RealmModel {

    String PRIMARY_FIELD_NAME = "serializedKey";
    String RELATED_FIELD_NAME = "relatedKey";
    ImmutableList<String> REQUIRED_FIELD_NAMES = ImmutableList.of(PRIMARY_FIELD_NAME, RELATED_FIELD_NAME);

    @NotNull
    @NonNull
    String getSerializedKey();

    void setSerializedKey(@NotNull @NonNull String serializedKey);

    @NotNull
    @NonNull
    String getRelatedKey();

    void setRelatedKey(@NotNull @NonNull String relatedKey);

    void setBody(@Nullable @android.support.annotation.Nullable T body);

    void setAttachment(@Nullable @android.support.annotation.Nullable A attachment);

    void setSavedAtTimeMillis(@Nullable @android.support.annotation.Nullable Long savedAtTimeMillis);

    void setRequestedAtTimeMillis(@Nullable @android.support.annotation.Nullable Long requestedAtTimeMillis);

    void setRawBody(@Nullable @android.support.annotation.Nullable Object body);

    void setRawAttachment(@Nullable @android.support.annotation.Nullable Object attachment);

}
