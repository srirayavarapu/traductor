package com.transjan.traductor.imgFromGallery;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.transjan.traductor.GetServices;
import com.transjan.traductor.MainActivity;
import com.transjan.traductor.R;

import java.util.ArrayList;
import java.util.List;

public class ImageFromGalleryActivity extends AppCompatActivity {

    Bitmap bitmap;
    public static int GET_SELECTED_GALLERY = 2;
    ImageView imageView;
    ImageButton imgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_from_gallery);

        imageView = findViewById(R.id.selected_gallery_img);
        imgBtn = findViewById(R.id.open_gallery_btn);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, GET_SELECTED_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                imgBtn.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(image);
                convertImage(image);
            }
        }
    }

    private void convertImage(Bitmap image) {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (textRecognizer.isOperational()) {
            Frame frame = new Frame.Builder().setBitmap(image).build();


            SparseArray items = textRecognizer.detect(frame);
            List<TextBlock> blocks = new ArrayList<>();

            TextBlock myItem = null;
            for (int i = 0; i < items.size(); ++i) {
                myItem = (TextBlock) items.valueAt(i);

                //Add All TextBlocks to the `blocks` List
                blocks.add(myItem);

            }
            //END OF DETECTING TEXT

            //The Color of the Rectangle to Draw on top of Text
            Paint rectPaint = new Paint();
            rectPaint.setColor(Color.WHITE);
            rectPaint.setStyle(Paint.Style.STROKE);
            rectPaint.setStrokeWidth(4.0f);

            //Create the Canvas object,
            //Which ever way you do image that is ScreenShot for example, you
            //need the views Height and Width to draw recatngles
            //because the API detects the position of Text on the View
            //So Dimesnions are important for Draw method to draw at that Text
            //Location
            Bitmap tempBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(tempBitmap);
            canvas.drawBitmap(image, 0, 0, null);

            //Loop through each `Block`
            for (TextBlock textBlock : blocks) {
                //Get the Rectangle/boundingBox of the word
                RectF rect = new RectF(textBlock.getBoundingBox());
                rectPaint.setColor(Color.BLACK);

                //Finally Draw Rectangle/boundingBox around word
                canvas.drawRect(rect, rectPaint);

                //Set image to the `View`
                imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
                GetServices.editTextData = textBlock;
                Intent goToMain = new Intent(this, MainActivity.class);
                startActivity(goToMain);
                finish();
//                List<? extends Text> textLines = textBlock.getComponents();

                //loop Through each `Line`
//                for (Text currentLine : textLines)
//                {
//                    List<? extends Text>  words = currentLine.getComponents();
//
//                    //Loop through each `Word`
//                    for (Text currentword : words)
//                    {
//
//                    }
//
//                }
            }

        }
    }


}

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Uri selectedImage = data.getData();
//        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//        cursor.moveToFirst();
//        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//        String picturePath = cursor.getString(columnIndex);
//        cursor.close();
//        Bitmap image = BitmapFactory.decodeFile(picturePath);
//    }
