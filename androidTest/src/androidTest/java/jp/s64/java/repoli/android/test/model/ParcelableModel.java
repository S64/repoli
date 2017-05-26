package jp.s64.java.repoli.android.test.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.reflect.TypeToken;

import jp.s64.java.repoli.core.IDataKey;

/**
 * Created by shuma on 2017/05/22.
 */

@Deprecated
public class ParcelableModel implements Parcelable {

    private String uuid;
    private String group;

    public ParcelableModel() {

    }

    public ParcelableModel(String uuid, String group) {
        this.uuid = uuid;
        this.group = group;
    }

    protected ParcelableModel(Parcel in) {
        uuid = in.readString();
        group = in.readString();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getUuid());
        dest.writeString(getGroup());
    }

    @SuppressWarnings("unused")
    public static final Creator<ParcelableModel> CREATOR = new Creator<ParcelableModel>() {
        @Override
        public ParcelableModel createFromParcel(Parcel in) {
            return new ParcelableModel(in);
        }

        @Override
        public ParcelableModel[] newArray(int size) {
            return new ParcelableModel[size];
        }
    };

    public static IDataKey<ParcelableModel, Void> createKey(final ParcelableModel model) {
        return new IDataKey<ParcelableModel, Void>() {
            @Override
            public TypeToken<ParcelableModel> getBodyType() {
                return new TypeToken<ParcelableModel>() {
                };
            }

            @Override
            public TypeToken<Void> getAttachmentType() {
                return new TypeToken<Void>() {
                };
            }

            @Override
            public String getSerialized() {
                return model.getUuid();
            }

            @Override
            public String getRelatedKey() {
                return model.getGroup();
            }
        };
    }

}
