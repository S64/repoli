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

package jp.s64.java.repoli.preset.serializer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import jp.s64.java.repoli.core.ISerializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListSerializerTest {

    @Test
    public void test() {
        TypeToken<List<List<String>>> type = new TypeToken<List<List<String>>>() {
        };
        List<List<String>> deserialized, org = Lists.<List<String>>newArrayList(
                Lists.<String>newArrayList("hoge")
        );

        ListSerializer serializer = new ListSerializer();
        Set<ISerializer> serializers = Sets.newHashSet(
                serializer,
                new SerializableSerializer()
        );

        assertTrue(serializer.canSerialize(type));
        byte[] serialized = serializer.serialize(
                type,
                org,
                serializers
        );
        deserialized = serializer.deserialize(
                type,
                serialized,
                serializers
        );

        assertEquals(org.get(0).get(0), deserialized.get(0).get(0));
    }


}
