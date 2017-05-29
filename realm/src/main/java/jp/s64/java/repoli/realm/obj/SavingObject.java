package jp.s64.java.repoli.realm.obj;

import com.google.common.collect.ImmutableList;

import io.realm.RealmModel;
import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/29.
 */

public interface SavingObject<T extends RealmModel, A extends RealmModel> extends IRepositoryDataContainer<T, A>, RealmModel {

    String PRIMARY_FIELD_NAME = "serializedKey";
    String RELATED_FIELD_NAME = "relatedKey";
    ImmutableList<String> REQUIRED_FIELD_NAMES = ImmutableList.of(PRIMARY_FIELD_NAME, RELATED_FIELD_NAME);

    String getSerializedKey();

    void setSerializedKey(String serializedKey);

    String getRelatedKey();

    void setRelatedKey(String relatedKey);

    void setBody(T body);

    void setAttachment(A attachment);

    void setSavedAtTimeMillis(Long savedAtTimeMillis);

    void setRequestedAtTimeMillis(Long requestedAtTimeMillis);

    void setRawBody(Object body);

    void setRawAttachment(Object attachment);

}
