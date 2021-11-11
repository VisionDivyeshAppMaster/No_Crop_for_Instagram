package com.PicinSquare.NocropforInsta.squareFree;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Activity_Select extends AppCompatActivity {

    private int RESULT_LOAD_IMG;
    private final String TAG = "MoPub BannerDemo";
    private TextView Rateus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Rateus = findViewById(R.id.Rateus);

        Rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent new_intent = Intent.createChooser(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())), "Share via");
                new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(new_intent);

            }
        });

        // MoPub banner
        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("b195f8dd8ded45fe847ad89ed1d016da").build();
        MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());

        MoPubView moPubView = (MoPubView) findViewById(R.id.ad_vew);
        moPubView.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da"); // Enter your Ad Unit ID from www.mopub.com
        moPubView.loadAd();

        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(@NonNull MoPubView moPubView) {
                Log.d(TAG, "onBannerLoaded: ");

            }

            @Override
            public void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {
                Log.d(TAG, "onBannerFailed: ");
            }

            @Override
            public void onBannerClicked(MoPubView moPubView) {
                Log.d(TAG, "onBannerClicked: ");
            }

            @Override
            public void onBannerExpanded(MoPubView moPubView) {
                Log.d(TAG, "onBannerExpanded: ");
            }

            @Override
            public void onBannerCollapsed(MoPubView moPubView) {
                Log.d(TAG, "onBannerCollapsed: ");
            }
        });
        //
        // MoPub banner over
    }

    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {

            }
        };
    }

    public void textViewClicked(View view) {
        getPhotoFromGallery();
    }

    private void getPhotoFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            final Uri contentUri;
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
                File cachePath = new File(getApplicationContext().getCacheDir(), "images");
                cachePath.mkdirs(); // don't forget to make the directory
                FileOutputStream stream = new FileOutputStream(cachePath + "/image.jpg"); // overwrites this image every time
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.close();
                File imagePath = new File(this.getCacheDir(), "images");
                File newFile = new File(imagePath, "image.jpg");
                contentUri = FileProvider.getUriForFile(this, "com.PicinSquare.NocropforInsta.squareFree.fileprovider", newFile);
                changeScreen(contentUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void changeScreen(Uri imageUri) {
        Intent i = new Intent(this, Activity_Main.class);
        i.putExtra("ImageUri", imageUri);
        startActivity(i);
    }
}
