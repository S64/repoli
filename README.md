# Repoli
Policy-based cache container for Java, Android

## Modules

Repoli is supported some platforms. You can use below modules:

- `core`
- `rxjava1`
- `realm`

And, Below modules is now under development. (not published)

- `android`
  - Currently you **can** use Repoli on Android without this module!
- `orma3`
- `rxjava2`

## Usages

Add following lines to your buildscripts.

```groovy
buildscript {
    ext {
        repoli_version = '0.0.1'
    }
}
```

```groovy
repositories {
    maven {
        url "http://dl.bintray.com/s64/maven"
    }
}

dependencies {
    compile "jp.s64.java.repoli:core:${repoli_version}"
    // compile "jp.s64.java.repoli:rxjava1:${repoli_version}"
    // compile "jp.s64.java.repoli:realm:${repoli_version}"
}
```

TODO(@S64): Write example usages. (Rewrite [ParcelableSerializerTest.java](https://github.com/S64/repoli/blob/master/androidTest/src/androidTest/java/jp/s64/java/repoli/android/test/serializer/ParcelableSerializerTest.java))

## Library Design

TODO(@S64): Paste uml here.
