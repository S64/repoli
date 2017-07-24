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

Japanese details is available here: [blog.s64.jp](http://blog.s64.jp/entry/published-repoli-0_0_1)

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

## Apps using Repoli

- [Wantedly Visit](https://play.google.com/store/apps/details?id=com.wantedly.android.visit) & [Intern](https://play.google.com/store/apps/details?id=com.wantedly.android.student)

## License

```
Copyright 2017 Shuma Yoshioka

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
