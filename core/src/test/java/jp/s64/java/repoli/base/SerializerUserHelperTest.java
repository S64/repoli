package jp.s64.java.repoli.base;

import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.List;

import jp.s64.java.repoli.base.SerializerUserHelper;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.preset.serializer.ListSerializer;
import jp.s64.java.repoli.preset.serializer.SerializableSerializer;
import jp.s64.java.repoli.preset.serializer.VoidSerializer;

import static org.junit.Assert.assertEquals;

/**
 * Created by shuma on 2017/05/26.
 */

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
                    VoidSerializer.INSTANCE
            ));
        }
        assertEquals(3, helper.getSerializers().size());
        List<ISerializer> sorted = Lists.newLinkedList(helper.getSerializers());
        {   // 期待する順序になっていることを確認
            assertEquals(VoidSerializer.class, sorted.get(0).getClass());
            assertEquals(ListSerializer.class, sorted.get(1).getClass());
            assertEquals(SerializableSerializer.class, sorted.get(2).getClass());
        }
    }

}
