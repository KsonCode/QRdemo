package com.bwie.qrdemo;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;

public class QRActivity extends AppCompatActivity implements View.OnClickListener {

    private Button scanBtn, productBtn;
    private EditText scanTv;
    private int REQUEST_CODE = 1;//扫描请求吗

    private ImageView scanIv;
    private int REQUEST_IMAGE =2;//调用相册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        initView();
        initData();

    }

    private void initData() {

        /**
         * 打开闪光灯
         */
        CodeUtils.isLightEnable(true);

        scanImg();
    }

    /**
     * 选择图库
     */
    private void scanImg() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     *
     */
    private void initView() {
        scanBtn = findViewById(R.id.btn_toscan);
        productBtn = findViewById(R.id.btn_product);
        scanTv = findViewById(R.id.et_scan);
        scanIv = findViewById(R.id.iv_scan);
        scanBtn.setOnClickListener(this);//this是什么意思？this是对象（当前actiivty的实例对象）


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //扫描二维码，调用zxinglibray的CaptureActivity类
            case R.id.btn_toscan:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            //生成二维码
            case R.id.btn_product:
                String data = scanTv.getText().toString();
//                if (data!=null&&data.length()>0){
//
//                }


                //java有23种设计模式：单例模式，建造者，工厂模式
                if (!TextUtils.isEmpty(data)) {//
                    //内存对象
                    Bitmap mBitmap = CodeUtils.createImage(data, 100, 100, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                    scanIv.setImageBitmap(mBitmap);
                }else{
//                    Toast.makeText()
                }
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/**
 * 处理二维码扫描结果
 */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();//获取bundle对像
                if (bundle == null) {//
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                //搜uri如何转换成sdcard路径（图片的绝对路径）
                String path = "";
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片


                    CodeUtils.analyzeBitmap(path, new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(QRActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(QRActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });

                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
