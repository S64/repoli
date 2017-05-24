package jp.s64.repoli.rxjava1;

import org.junit.Test;

import java.util.List;

import jp.s64.java.repoli.core.TypeReference;

/**
 * Created by shuma on 2017/05/23.
 */

public class RxJava1ClassTest {

    @Test
    public void test() {
        TypeReference<?> target = new TypeReference<List<List<String>>>() {
        };

        hoge(target);
    }

    public static <T> void hoge(TypeReference<T> desired) {
        TypeReference<?> type = desired.getParameterizedType(0);
        System.out.println(type);

        System.out.println();

        System.out.println(String.class.isAssignableFrom(type.getRawType()));
        System.out.println(Integer.class.isAssignableFrom(type.getRawType()));
        System.out.println(List.class.isAssignableFrom(type.getRawType()));
        System.out.println(Object.class.isAssignableFrom(type.getRawType()));

        //

        System.out.println();

        TypeReference<?> hoge = type.getParameterizedType(0);
        System.out.println(hoge);

        System.out.println();

        System.out.println(String.class.isAssignableFrom(hoge.getRawType()));
        System.out.println(Integer.class.isAssignableFrom(hoge.getRawType()));
        System.out.println(List.class.isAssignableFrom(hoge.getRawType()));
        System.out.println(Object.class.isAssignableFrom(hoge.getRawType()));
    }

}
