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

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.internal.KeepMember;
import jp.s64.java.repoli.realm.obj.SavingObject;

@RealmClass
@KeepMember
public class TestObjectSavingClass extends RealmObject implements SavingObject<TestObject, TestObject> {

    @PrimaryKey
    private String serializedKey;

    @Index
    private String relatedKey;

    private TestObject body;
    private TestObject attachment;
    private Long savedAtTimeMillis;
    private Long requestedAtTimeMillis;

    @Override
    public String getSerializedKey() {
        return serializedKey;
    }

    @Override
    public void setSerializedKey(String serializedKey) {
        this.serializedKey = serializedKey;
    }

    @Override
    public String getRelatedKey() {
        return relatedKey;
    }

    @Override
    public void setRelatedKey(String relatedKey) {
        this.relatedKey = relatedKey;
    }

    @Override
    public TestObject getBody() {
        return body;
    }

    @Override
    public void setBody(TestObject body) {
        this.body = body;
    }

    @Override
    public TestObject getAttachment() {
        return attachment;
    }

    @Override
    public void setAttachment(TestObject attachment) {
        this.attachment = attachment;
    }

    @Override
    public Long getSavedAtTimeMillis() {
        return savedAtTimeMillis;
    }

    @Override
    public void setSavedAtTimeMillis(Long savedAtTimeMillis) {
        this.savedAtTimeMillis = savedAtTimeMillis;
    }

    @Override
    public Long getRequestedAtTimeMillis() {
        return requestedAtTimeMillis;
    }

    @Override
    public void setRequestedAtTimeMillis(Long requestedAtTimeMillis) {
        this.requestedAtTimeMillis = requestedAtTimeMillis;
    }

    @Override
    public void setRawBody(Object body) {
        setBody((TestObject) body);
    }

    @Override
    public void setRawAttachment(Object attachment) {
        setAttachment((TestObject) attachment);
    }

}
