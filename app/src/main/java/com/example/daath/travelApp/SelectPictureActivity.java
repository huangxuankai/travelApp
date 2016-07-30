package com.example.daath.travelApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.daath.travelApp.customClass.ActivityCollector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SelectPictureActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int TAKE_PHOTO = 1;     //从拍照获得
    public static final int PICK_PHOTO = 2;     //从相册获得
    public static final int CROP_PHOTO = 3;     //对相片进行裁剪
    /* 头像名称 */
    public static final String PHOTO_FILE_NAME = "temp_avatar.jpg";
    private Uri imageUri;


    private ImageView backArrow;
    private Button takePhoto, pickPhoto, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        ActivityCollector.addActivity(this);
        initView();

    }

    public void initView() {
        backArrow = (ImageView) findViewById(R.id.selectPicture_backArrow);
        takePhoto = (Button) findViewById(R.id.selectPicture_takePhoto);
        pickPhoto = (Button) findViewById(R.id.selectPicture_pickPhoto);
        cancel = (Button) findViewById(R.id.selectPicture_cancel);
        backArrow.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        pickPhoto.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public static void anotherActionStart(Context context) {
        Intent intent = new Intent(context, SelectPictureActivity.class);
        context.startActivity(intent);
    }


    /**
     * 从相册获取
     */
    public void fromGallery() {
        // 激活系统图库，选择一张图片
        File outputImage = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PICK_PHOTO);
    }


    /**
     * 从相机获取
     */
    public void fromCamera() {
        File outputImage = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            imageUri = Uri.fromFile(outputImage);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 剪切图片
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, CROP_PHOTO);
    }

    /**
     * 判断是否有SD卡
     * @return
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectPicture_backArrow:
                ActivityCollector.removeActivity(this);
                finish();
                break;
            case R.id.selectPicture_takePhoto:
                fromCamera();
                break;
            case R.id.selectPicture_pickPhoto:
                fromGallery();
                break;
            case R.id.selectPicture_cancel:
                ActivityCollector.removeActivity(this);
                finish();
                break;
            default: break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Tag_onActivityResult", "onActivityResult");
        switch (requestCode) {
            case TAKE_PHOTO:
                if (hasSdcard()) {
                    if (resultCode == RESULT_OK) crop(imageUri);
                } else {
                    Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                }
                break;
            case PICK_PHOTO:
                if (data != null) {
                    imageUri = data.getData();
                    crop(imageUri);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = data.getParcelableExtra("data");    //获取剪切好的图片
                        ImageUploadActivity.anotherActionStart(this, bitmap);
//                        boolean delete = tempFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default: break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
