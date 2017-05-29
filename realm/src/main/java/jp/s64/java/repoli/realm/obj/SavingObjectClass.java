package jp.s64.java.repoli.realm.obj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shuma on 2017/05/29.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SavingObjectClass {
    Class<? extends SavingObject<?, ?>> value();
}
