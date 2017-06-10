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

package jp.s64.java.repoli.realm.test.obj;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import io.realm.RealmObject;
import io.realm.internal.KeepMember;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.realm.obj.SavingObjectClass;

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
    public static class Key implements IDataKey<TestObject, TestObject> {

        @NotNull
        @Override
        public TypeToken<TestObject> getBodyType() {
            return TypeToken.of(TestObject.class);
        }

        @NotNull
        @Override
        public TypeToken<TestObject> getAttachmentType() {
            return TypeToken.of(TestObject.class);
        }

        @NotNull
        @Override
        public String getSerialized() {
            return "serialized_key";
        }

        @NotNull
        @Override
        public String getRelatedKey() {
            return "related_key";
        }

    }

}
