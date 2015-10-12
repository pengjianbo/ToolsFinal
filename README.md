# ToolsFinal简介
添加一些android开发中常用的一些工具类。将会持续更新，敬请期待。
#下载ToolsFinal
--------
下载这个[JAR](https://github.com/pengjianbo/ToolsFinal/tree/master/downloads/) 或者通过Gradle抓取:

```groovy
compile 'cn.finalteam:toolsfinal:1.0.4'
```
###Android开发常用的工具类
####BitmapUtils 
* drawable bitmap互转
* 获取图片在相册中的方向
* Bitmap比例缩放
* 圆形Bitmap
* Bitmap缩略图
* Bitmap保存到指定路径
* ...

####Logger
* 日志格式化输出
* 日志定位

```java
Logger.init(TAG);

Logger.d(...);
Logger.i(...);
Logger.e(...);
Logger.w(...);
Logger.v(...);
Logger.a(...);
...
```

####StringUtils
* 字符串空判断
* 获取字符串长度
* 字符串UTF8编码
* 字符串HTML转换
* 数据库字符转义
* ...

####ActivityManager
* Activity堆栈
* finish指定Activity
* finish所有Activity
* 退出App

```java
//在BaseActivity中添加
//onCreate
ActivityManager.getActivityManager().addActivity(this);
//onDestory
ActivityManager.getActivityManager().finishActivity(this);

//finish所有页面
ActivityManager.getActivityManager().finishAllActivity(this);
//finish所有页面和kill app
ActivityManager.getActivityManager().appExit(this);
```

####AppCacheUtils
* 数据缓冲/配置信息存储工具类
* 多进程访问
* ...

```java
//存储
AppCacheUtils.get(context).put(key, int);
AppCacheUtils.get(context).put(key, String);
AppCacheUtils.get(context).put(key, char);
AppCacheUtils.get(context).put(key, float);
AppCacheUtils.get(context).put(key, double);
AppCacheUtils.get(context).put(key, boolean);
AppCacheUtils.get(context).put(key, byte[]);
AppCacheUtils.get(context).put(key, Bitmap);
AppCacheUtils.get(context).put(key, JSONObject);
AppCacheUtils.get(context).put(key, Serializable);
...
```
```java
//获取
AppCacheUtils.get(context).getInt(key, intDefault);
AppCacheUtils.get(context).getString(key, stringDefault);
AppCacheUtils.get(context).getChar(key, charDefault);
AppCacheUtils.get(context).getFloat(key, floatDefault);
AppCacheUtils.get(context).getDouble(key, doubleDefault);
AppCacheUtils.get(context).getBoolean(key, booleanDefault);
AppCacheUtils.get(context).getBitmap(key);
AppCacheUtils.get(context).getJSONObject(key);
AppCacheUtils.get(context).getObject(key);
...
```
```java
//清除所有缓冲
AppCacheUtils.get(context).clear();
//移除某个缓存
AppCacheUtils.get(context).remove(key);
``` 

####DateUtils
* 根据String构建一个Date
* 指定format构建
* 根据Date格式化
* 获取Date 年、月、日、时、分、秒、毫秒
* 获取一个月最后一天
* 获取一个月第一天
* 根据Date获取星期
* 时间友好显示
* ...

####DeviceUtils
* 判断是否有内存卡
* 获取本机IP
* 多个内存卡时获取外置内存卡
* 获取内存卡总大小、使用大小和剩余多少
* 判断网络是否可以用
* 判断某个服务是否在运行
* 判断某个进程是否在运行
* 获取IMEI、IMSI、MAC、UUID
* 执行震动
* 获取相机最后一次拍照的图片
* 获取屏幕大小
* copy到剪切板
* 获取所有应用程序
* 判断某个app是否安装了
* dp px转换
* 获取system bar高度
* 输入法隐藏和显示
* 启动某个应用下的Activity
* ...

####FileUtils
* 读取某个文件内容
* 将某个文本写入到指定文件
* 文件移动
* 文件拷贝
* 获取文件扩展名
* 获取URL扩展名
* 获取文件名
* 获取文件大小
* 创建文件夹以及父文件夹
* ...

```java
 FileUtils.readFile(String, String)//read file
 FileUtils.readFileToList(String, String)// read file to string list
 FileUtils.writeFile(String, String, boolean)// write file from String
 FileUtils.writeFile(String, String)// write file from String
 FileUtils.writeFile(String, List, boolean)// write file from String List
 FileUtils.writeFile(String, List)// write file from String List
 FileUtils.writeFile(String, InputStream)// write file
 FileUtils.writeFile(String, InputStream, boolean)// write file
 FileUtils.writeFile(File, InputStream)// write file
 FileUtils.writeFile(File, InputStream, boolean)// write file

 FileUtils.moveFile(File, File)// or FileUtils.moveFile(String, String)
 FileUtils.copyFile(String, String)
 FileUtils.getFileExtension(String)
 FileUtils.getFileName(String)
 FileUtils.getFileNameWithoutExtension(String)
 FileUtils.getFileSize(String)
 FileUtils.deleteFile(String)
 FileUtils.isFileExist(String)
 FileUtils.isFolderExist(String)
 FileUtils.makeFolders(String)
 FileUtils.makeDirs(String)
```

####StorageUtils
* 缓冲目录工具类

####ManifestUtils
* 获取app版本号
* 获取app版本名称
* 获取app渠道名

####ApkUtils
* 安装某个apk
* META-INF获取渠道名称

####JsonFormatUtils
* JSON格式化

####JsonValidator
* JSON合法性验证

####Base64Util
* base64编码/解码

```java
//解码
byte[] srcBytes = Base64Utils.decode(byte[] decodeBytes, flag)
byte[] srcBytes = Base64Utils.decode(String decodeString, flag)
byte[] srcBytes = Base64Utils.decode(byte[] decodeBytes, int offset, int len, int flags)
...
//编码
byte[] decodeBytes = Base64Utils.encode(byte[] src, flag)
byte[] decodeBytes = Base64Utils.encode(String src, flag)
String decodeString = Base64Utils.encodeToString(byte[] src, flag)
byte[] decodeBytes = Base64Utils.encode(byte[] src, int offset, int len, int flags)
...
```

####Des3Coder DesCoder
* 对称加密算法

```java
//加密
Des3Coder.encrypt(byte[] msg, String key)//三层DES加密
DesCoder.encrypt(byte[] msg, String key)
//解密
Des3Coder.decrypt(byte[] msg, String key)
DesCoder.decrypt(byte[] msg, String key)
```
    
####EncodeUtils
* 二进制,字节数组,字符,十六进制,BCD编码转换

```java
//把16进制字符串转换成字节数组
EncodeUtils.hexStringToByte(String hex)
//把字节数组转换成16进制字符串
EncodeUtils.bytesToHexString(byte[] bArray)
//BCD码转为10进制串(阿拉伯数据)
EncodeUtils.bcd2Str(byte[] bytes)
//10进制串转为BCD码
EncodeUtils.str2Bcd(String asc)
//BCD码转ASC码
EncodeUtils.bcd2asc(byte[] bytes)
//将字符串编码成16进制数字
EncodeUtils.encodeHex(String str)
//将16进制数字解码成字符串
EncodeUtils.decodeHex(String bytes)
```

####RSAUtils
* RSA 工具类。提供加密，解密，生成密钥对等方法。

```java
//生成密钥对
KeyPair key = RSAUtils.generateKeyPair()
//生成公钥
RSAPublicKey key = RSAUtils.generateRSAPublicKey(byte[] modulus, byte[] publicExponent)
//生成私钥
RSAPrivateKey key = RSAUtils.generateRSAPrivateKey(byte[] modulus, byte[] privateExponent)
//加密
byte[] result = RSAUtils.encrypt(Key key, byte[] data,PADDING padding)
//公钥加密
byte[] result = RSAUtils.encryptByPublicKey(byte[] publicKey, byte[] data,PADDING padding)
//私钥加密
byte[] result = RSAUtils.encryptByPrivateKey(byte[] publicKey, byte[] privateKey, byte[] data,PADDING padding)
//解密
byte[] result = RSAUtils.decrypt(Key key, byte[] data,PADDING padding)
//公钥解密
byte[] result = RSAUtils.decryptByPublicKey(byte[] publicKey, byte[] data,PADDING padding)
//私钥解密
byte[] result = RSAUtils.decryptByPrivateKey(byte[] publicKey, byte[] privateKey, byte[] data,PADDING padding)
```


License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.