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

/**
 * Created by shuma on 2017/05/24.
 */

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
                new StringSerializer()
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
