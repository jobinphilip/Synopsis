package com.synopsis.androidapp.synopsis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 7/15/2016.
 */
public class BasicInfoClass extends Activity {
    ImageView selfyImageview;
    Button selfyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_info);
        //   selfyImageview=(ImageView)findViewById(R.id.selfyImageView);
        //   selfyImageview.setImageResource(R.drawable.ic_menu_camera);

    }


    public void submitbasicinfofn(View view) {
        startActivity(new Intent(getApplicationContext(), Dash_board.class));
    }


    ////////////////////////choose from gallery or from camera option//////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");


           //Convert to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent I = new Intent(this, Camera_activity.class);
            I.putExtra("image",byteArray);
            startActivity(I);
        }
    }

    public void photo_actions(View v) {
        Log.d("jobin","in photo actions");
        final AlertDialog alertDialog = new AlertDialog.Builder(BasicInfoClass.this).create();
        alertDialog.setTitle("Title");
        alertDialog.setMessage("Message");
        alertDialog.setButton("camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int CAMERA_PIC_REQUEST = 0;
                Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, CAMERA_PIC_REQUEST);


            }

        });


        alertDialog.setButton2("gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(),ImageUpload.class));
                    }


                }
        );
        alertDialog.show();

    }
    ///////////////////////////////////////////////////////////////////////////////////////


}