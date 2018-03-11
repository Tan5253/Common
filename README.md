# Common

自己用的工具库。包括一些工具类和自定义View。

## 使用方法：

1、引用

在Project的gradle中加入：
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
在Module的gradle中加入：
```groovy
    dependencies {
        compile 'com.github.like5188:Common:版本号'
    }
```
2、包含的工具类
```java
    AppUtils
    ArrayUtils
    BitmapUtils
    CalendarUtils
    ChineseUtils
    ClickUtils
    CloseableUtils
    CrashHandler
    CsvUtils
    DateUtils
    DialogFragmentUtils             // kotlin，支持BaseActivity和BaseFragment扩展方法，并封装了BaseDialogFragment
    DimensionUtils
    DrawTextUtils                   // kotlin，画文本时可以获取文本的宽、高、baseline等
    FileUtils
    HexUtil
    HighLightUtils
    HttpPostUploadUtils
    ImageLoaderUtils
    ImagePathUtils
    ImageUtils
    InputSoftKeybordUtils
    MD5Utils
    MoneyFormatUtils
    NetWorkUtils
    ObjectSerializeUtils
    ParseUtils
    PhoneUtils
    ReflectUtils
    ResourceUtils
    RxBusTag
    RxJavaUtils
    SenserUtils
    SPUtils                         // kotlin，支持属性委托
    StatusBarUtils
    StorageUtils
    TakePhotoUtils
    TCPClient                       // kotlin，TCP客户端
    UDPClient                       // kotlin，UDP客户端
    TimerUtils
    Verify
    ViewHolder
```
3、包含的自定义View
```java
    bottomNavigationBars            // 底部导航栏
    check                           // 复选框控制器
    chat                            // 自定义的统计图表
    pwdedittext                     // 组合密码框，支付时候使用
    radio                           // 单选框控制器
    roundedimageview                // 圆形ImageView
    toolbar                         // 标题栏
    transformerviewpager            // 带滑动动画的ViewPager
    BadgeView                       // 显示未读消息数量的View
    CircleTextView                  // 圆形TextView
    ExpandView                      // 支持展开收缩的LinearLayout
    InterceptFrameLayout
    LetterListView                  // 类似联系人侧边字母导航栏
    MyGridView
    MyHorizontalViewPager
    MyVerticalDividerGridView
    MyVideoView
    RotateTextView                  // 可以旋转角度的TextView
    SelectableRoundedImageView      // 显示圆角的ImageView
    TimeTextView                    // 验证码专用TextView
```
# License
```xml
    Copyright 2017 like5188
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
