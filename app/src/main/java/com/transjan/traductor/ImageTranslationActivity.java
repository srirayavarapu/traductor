package com.transjan.traductor;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.transjan.traductor.camera.RawCameraCaptureActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageTranslationActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView camera_Imageview;
    ImageView gallery_ImageView;
    ImageView imageview_IV;
    LinearLayout selectedView_LL;
    FrameLayout convertedTextView_FL, image_FL;
    TextView textView_Tv;
    public static int GET_SELECTED_CAMERA = 1;
    public static int GET_SELECTED_GALLERY = 2;
    private ImageView close_IV;
    public String photoFileName = "photo.jpg";
    public final String APP_TAG = "MyCustomApp";
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_translation);
        camera_Imageview = findViewById(R.id.camera_Imageview);
        gallery_ImageView = findViewById(R.id.gallery_ImageView);
        selectedView_LL = findViewById(R.id.selectedView_LL);
        convertedTextView_FL = findViewById(R.id.convertedTextView_FL);
        textView_Tv = findViewById(R.id.textView_Tv);
        image_FL = findViewById(R.id.image_FL);
        imageview_IV = findViewById(R.id.imageView_Iv);
        close_IV = findViewById(R.id.close_IV);

        camera_Imageview.setOnClickListener(this);
        gallery_ImageView.setOnClickListener(this);
        close_IV.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.camera_Imageview) {
            Intent rcca = new Intent(this, RawCameraCaptureActivity.class);
            startActivity(rcca);
        } else if (id == R.id.gallery_ImageView) {
            if (checkGalleryPermission()) {
                openGallery();
            } else {
                requestGalleryPermission();
            }
        } else if (id == R.id.close_IV) {
            if (convertedTextView_FL.getVisibility() == View.VISIBLE || image_FL.getVisibility() == View.VISIBLE) {
                selectedView_LL.setVisibility(View.VISIBLE);
                convertedTextView_FL.setVisibility(View.GONE);
                image_FL.setVisibility(View.GONE);
            }
        }
    }

    public boolean checkGalleryPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkCameraPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(ImageTranslationActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(ImageTranslationActivity.this, android.Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ImageTranslationActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_VIDEO}, GET_SELECTED_GALLERY);
            }
        } else {
            if (ContextCompat.checkSelfPermission(ImageTranslationActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ImageTranslationActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GET_SELECTED_GALLERY);
            }
        }
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(ImageTranslationActivity.this, new String[]
                {
                        CAMERA
                }, GET_SELECTED_CAMERA);
    }

    public void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GET_SELECTED_GALLERY);
    }

    public void openCamera() {
        if (checkCemaraPermission()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = getPhotoFileUri(photoFileName);
            Uri fileProvider = Uri.fromFile(photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, GET_SELECTED_CAMERA);
            }
        }
    }


    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.e(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    private boolean checkCemaraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 111);
            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (camera) {
                        openCamera();
                    }
                }
                break;

            case 2:
                if (grantResults.length > 0) {
                    boolean ReadStoreage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteStoreage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (ReadStoreage && WriteStoreage) {
                        openGallery();
                    }
                }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_SELECTED_CAMERA && resultCode == RESULT_OK) {
            Bitmap image = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            if (image != null) {
                selectedView_LL.setVisibility(View.GONE);
                image_FL.setVisibility(View.VISIBLE);
                convertedTextView_FL.setVisibility(View.GONE);
                imageview_IV.setImageBitmap(image);
                showConvertedText(image);
            }
        }
        if (requestCode == GET_SELECTED_GALLERY && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap image = BitmapFactory.decodeFile(picturePath);
            if (image != null) {
                selectedView_LL.setVisibility(View.GONE);
                image_FL.setVisibility(View.VISIBLE);
                convertedTextView_FL.setVisibility(View.GONE);
                imageview_IV.setImageBitmap(image);
                showConvertedText(image);
            }
        }
    }

    public void showConvertedText(final Bitmap bitmap) {
        if (bitmap != null) {
            final TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            if (txtRecognizer.isOperational()) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray items = txtRecognizer.detect(frame);
                        StringBuilder strBuilder = new StringBuilder();
                        for (int i = 0; i < items.size(); i++) {
                            TextBlock item = (TextBlock) items.valueAt(i);
                            strBuilder.append(item.getValue());
                            strBuilder.append(" ");
                        }
                        if (strBuilder.length() > 0) {
                            selectedView_LL.setVisibility(View.GONE);
                            image_FL.setVisibility(View.GONE);
                            convertedTextView_FL.setVisibility(View.GONE);
//                                textView_Tv.setText(strBuilder.toString());
                            textView_Tv.setVisibility(View.GONE);
                            GetServices.editTextString = strBuilder.toString();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            selectedView_LL.setVisibility(View.VISIBLE);
                            image_FL.setVisibility(View.GONE);
                            convertedTextView_FL.setVisibility(View.GONE);
                            Toast.makeText(ImageTranslationActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1500);
            }
        }
    }

    public Bitmap decodeFile(String path) {
        int orientation;
        try {
            if (path == null) {
                return null;
            }
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 0;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bitmap = bm;

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, bytes);
            ExifInterface exif = new ExifInterface(path);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix m = new Matrix();
            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                return bitmap;
            }
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }


}
