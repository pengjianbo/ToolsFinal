package cn.finalteam.toolsfinal.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.finalteam.toolsfinal.AppCacheUtils;
import cn.finalteam.toolsfinal.coder.DES3Coder;
import cn.finalteam.toolsfinal.coder.HexCoder;
import cn.finalteam.toolsfinal.logger.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.setDebug(BuildConfig.DEBUG);
        Logger.getDefaultLogger().i("Default Logger info");
        ILogger.i("Custom Logger info");

        String src = "test";
        String pwd = "123456";
        byte[] encrypt = DES3Coder.encryptMode(src.getBytes(), pwd);
        ILogger.d("encrypt=" + HexCoder.encodeHexString(encrypt));
        byte[] decrypt = DES3Coder.decryptMode(encrypt, pwd);
        ILogger.d("decrypt=" + new String(decrypt));

        AppCacheUtils.getInstance(this).put("myKey", "myValue");

        ILogger.e(new RuntimeException("test"));
    }
}
