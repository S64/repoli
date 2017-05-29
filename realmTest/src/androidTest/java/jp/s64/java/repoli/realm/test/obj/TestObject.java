package jp.s64.java.repoli.realm.test.obj;

import com.google.common.reflect.TypeToken;

import io.realm.RealmObject;
import io.realm.internal.KeepMember;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.realm.obj.SavingObjectClass;

/**
 * Created by shuma on 2017/05/29.
 */

@KeepMember
public class TestObject extends RealmObject {

    private String myValue;

    public String getMyValue() {
        return myValue;
    }

    public void setMyValue(String myValue) {
        this.myValue = myValue;
    }

    public Key getKey() {
        return new Key();
    }

    @SavingObjectClass(TestObjectSavingClass.class)
    public static class Key implements IDataKey<TestObject, Void> {

        @Override
        public TypeToken<TestObject> getBodyType() {
            return TypeToken.of(TestObject.class);
        }

        @Override
        public TypeToken<Void> getAttachmentType() {
            return TypeToken.of(Void.class);
        }

        @Override
        public String getSerialized() {
            return "serialized_key";
        }

        @Override
        public String getRelatedKey() {
            return "related_key";
        }

    }

}
