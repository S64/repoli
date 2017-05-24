package jp.s64.java.repoli.android.serializer;

import android.os.Parcel;
import android.os.Parcelable;

import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.TypeReference;

/**
 * Created by shuma on 2017/05/22.
 */

public class ParcelableSerializer implements ISerializer {

    private static final int PARCELABLE_FLAG = 0;
    private static final String PARCELABLE_CREATOR_FIELD_NAME = "CREATOR";

    @Override
    public <T> T deserialize(TypeReference<T> type, byte[] serialized) {
        if (serialized.length < 1) {
            return null;
        }
        Parcel dest = Parcel.obtain();
        {
            dest.unmarshall(serialized, 0, serialized.length);
        }
        {
            dest.setDataPosition(0); // reset
        }
        Parcelable.Creator<T> creator;
        try {
            creator = (Parcelable.Creator<T>) type.getRawType().getDeclaredField(PARCELABLE_CREATOR_FIELD_NAME).get(null);
        } catch (NoSuchFieldException e) {
            throw new ParcelableSerializerException(e);
        } catch (IllegalAccessException e) {
            throw new ParcelableSerializerException(e);
        }
        return creator.createFromParcel(dest);
    }

    @Override
    public <T> byte[] serialize(TypeReference<T> type, T deserialized) {
        if (deserialized == null) {
            return new byte[]{};
        }
        Parcel dest = Parcel.obtain();
        {
            ((Parcelable) deserialized).writeToParcel(dest, PARCELABLE_FLAG);
        }
        {
            dest.setDataPosition(0); // reset
        }
        return dest.marshall();
    }

    @Override
    public boolean canSerialize(TypeReference<?> type) {
        return Parcelable.class.isAssignableFrom(type.getRawType());
    }

    public static class ParcelableSerializerException extends RuntimeException {

        public ParcelableSerializerException(Throwable throwable) {
            super(throwable);
        }

    }

}
