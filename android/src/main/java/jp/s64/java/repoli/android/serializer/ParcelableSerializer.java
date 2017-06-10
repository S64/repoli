/*
 * Copyright (C) 2017 Shuma Yoshioka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.s64.java.repoli.android.serializer;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.reflect.TypeToken;

import java.util.Set;

import jp.s64.java.repoli.core.ISerializer;

@Deprecated
public class ParcelableSerializer implements ISerializer {

    private static final int PARCELABLE_FLAG = 0;
    private static final String PARCELABLE_CREATOR_FIELD_NAME = "CREATOR";

    @Override
    public <T> T deserialize(TypeToken<T> type, byte[] serialized, Set<ISerializer> serializers) {
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
    public <T> byte[] serialize(TypeToken<T> type, Object deserialized, Set<ISerializer> serializers) {
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
    public boolean canSerialize(TypeToken<?> type) {
        return Parcelable.class.isAssignableFrom(type.getRawType());
    }

    @Override
    public float getPriority() {
        return 4;
    }

    public static class ParcelableSerializerException extends RuntimeException {

        public ParcelableSerializerException(Throwable throwable) {
            super(throwable);
        }

    }

}
