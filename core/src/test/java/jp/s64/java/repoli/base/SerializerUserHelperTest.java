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

package jp.s64.java.repoli.base;

import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.List;

import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.preset.serializer.ListSerializer;
import jp.s64.java.repoli.preset.serializer.MapSerializer;
import jp.s64.java.repoli.preset.serializer.SerializableSerializer;
import jp.s64.java.repoli.preset.serializer.VoidSerializer;

import static org.junit.Assert.assertEquals;

public class SerializerUserHelperTest {

    @Test
    public void test() {
        SerializerUserHelper helper = new SerializerUserHelper();
        {   // 一旦クリアする
            helper.clearSerializer();
            assertEquals(0, helper.getSerializers().size());
        }
        {   // でたらめな優先順位でserializerを入れる
            helper.addSerializer(Lists.newArrayList(
                    ListSerializer.INSTANCE,
                    SerializableSerializer.INSTANCE,
                    VoidSerializer.INSTANCE,
                    MapSerializer.INSTANCE
            ));
        }
        assertEquals(4, helper.getSerializers().size());
        List<ISerializer> sorted = Lists.newLinkedList(helper.getSerializers());
        {   // 期待する順序になっていることを確認
            int i = -1;
            assertEquals(VoidSerializer.class, sorted.get(++i).getClass());
            assertEquals(ListSerializer.class, sorted.get(++i).getClass());
            assertEquals(MapSerializer.class, sorted.get(++i).getClass());
            assertEquals(SerializableSerializer.class, sorted.get(++i).getClass());
        }
    }

}
