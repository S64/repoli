package jp.s64.java.repoli.core;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuma on 2017/05/24.
 */
public abstract class TypeReference<T> {

    private final Type type;
    private final List<TypeReference<?>> inner = new ArrayList<>();

    protected TypeReference() {
        {
            type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        {
            TypeToken<?> token = TypeToken.of(getType());
            for (TypeVariable<?> inner : token.getRawType().getTypeParameters()) {

            }
        }
    }

    public Type getType() {
        return type;
    }

    public Class<?> getRawType() {
        return TypeToken.of(getType()).getRawType();
    }

    public Class<?> getParameterizedType(int index) {
        return getParameterizedType(getType(), index);
    }

    public static Class<?> getParameterizedType(Type type, int index) {
        TypeToken<?> token = TypeToken.of(type);
        return token
                .resolveType(
                        token.getRawType().getTypeParameters()[index]
                )
                .getRawType();
    }

}
