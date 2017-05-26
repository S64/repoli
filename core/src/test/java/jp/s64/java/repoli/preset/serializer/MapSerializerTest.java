package jp.s64.java.repoli.preset.serializer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.s64.java.repoli.core.ISerializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by shuma on 2017/05/26.
 */

public class MapSerializerTest {

    @Test
    public void test() {
        final long id = Long.MAX_VALUE / 2;
        final String value = "string-value";

        TypeToken<Map<Long, List<String>>> type = new TypeToken<Map<Long, List<String>>>() {
        };
        Map<Long, List<String>> deserialized, org = new HashMap<>();
        {
            org.put(id, Lists.newArrayList(
                    value
            ));
        }

        MapSerializer serializer = new MapSerializer();
        Set<ISerializer> serializers = Sets.newHashSet(
                serializer,
                new SerializableSerializer(),
                new ListSerializer()
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

        assertEquals(org.get(id).get(0), deserialized.get(id).get(0));
    }

}
