package jp.s64.java.repoli.preset.serializer;

import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.List;

import jp.s64.java.repoli.core.TypeReference;

import static org.junit.Assert.assertTrue;

/**
 * Created by shuma on 2017/05/24.
 */

public class ListSerializerTest {

    @Test
    public void test() {
        TypeReference<List<List<String>>> type = new TypeReference<List<List<String>>>() {
        };
        List<List<String>> org = Lists.<List<String>>newArrayList(
                Lists.<String>newArrayList("hoge")
        );

        ListSerializer serializer = new ListSerializer();

        assertTrue(serializer.canSerialize(type));
    }

}
