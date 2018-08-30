# meijue-ui library

## Build and Release

### 编译 `meijue-ui` library

此功能完整地编译代码，且生成 aar 文件以及 javadoc/sources 等包文件。

```bash
./gradlew :meijue-ui:build
```

需要上一级目录的 `keystore.properties` 文件就绪，且对应的 keystore 文件也有效。`keystore.properties` 的一个实例如下：

```properties
keyAlias=your-name
keyPassword=your-password
storeFile=/Users/hz/android/publish/my-2018-release.keystore
storePassword=your-password
```


### 发布并复制 aar 文件到 ...

```bash
./gradlew :meijue-ui:copyAar
```


### 发布到 lcoal maven repository

```bash
gradle :meijue-ui:install
```


### 发布到 bintray/jcenter

```bash
gradle :meijue-ui:bintrayUpload
```

注意每次发布之前应该保证build正确无误。bintrayUpload也会自动调用build任务。
此外，版本号注意至少要增量 build 号，或者 minor 号。


### 版本号

增加版本号，准备发布到 jcenter：

```bash
gradle :meijue-ui:incRelease
```

这个功能将会增加 build 号码，即从 `1.0.5` 增量到 `1.0.6`。

#### 增加 Minor 版本号

```bash
gradle :meijue-ui:incVersionsMinor
```

这个功能会增加 minor 号码，并清零 build 号码，即从 `1.0.9` 增量为 `1.1.0`。


## Features

### ToolbarAnimActivity class

abstract class to support toolbar icon and text animations at startup.

Usage: replace base class name `AppCompatActivity` with `ToolbarAnimActivity`. such as:

```kotlin
class MyActivity: ToolbarAnimActivity() {
}
```


### Extensions

#### BottomNavigationView


