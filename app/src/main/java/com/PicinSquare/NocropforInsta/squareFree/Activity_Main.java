package com.PicinSquare.NocropforInsta.squareFree;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.io.IOException;

public class Activity_Main extends AppCompatActivity {

    public static ImageView imageView;
    RelativeLayout SaveLayout;
    private int widthOfScreen;
    private Toolbar mTopToolbar;
    private int widthAndHeight;
    private float scale = 1f;
    private ScaleGestureDetector sgd;
    float halfImageView;
    private float widthAndHeightInDP;
    private int RESULT_LOAD_IMG;
    private static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 0;
    ImageView prefButton;
    int setcout = 3;
    SharedPreferences dialogcout, oksure;
    boolean oksurek;
    SharedPreferences.Editor editor, okEditor;
    int count;

    // MoPub interstitial
    private MoPubInterstitial mInterstitial;


    // interstitial adColony
    private final String APP_ID = "app801b6402e4b741ad8b";
    private final String ZONE_ID = "vza6ef94e4656240dca5";
    private final String TAG1 = "AdColonyBannerDemo";
    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listenerI;
    private AdColonyAdOptions adOptionsI;
    //
    // interstitial adColony over

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // adColony interstitial
        adsShow();
        AdColony.requestInterstitial(getResources().getString(R.string.adcolony_interstitial_id), listenerI, adOptionsI);
        //
        // adColony interstitial over


        // MoPub interstitial
        SdkConfiguration sdkConfigurationInterstitals = new SdkConfiguration.Builder("24534e1901884e398f1253216226017e").build();
        MoPub.initializeSdk(this, sdkConfigurationInterstitals, initSdkListener());

        mInterstitial = new MoPubInterstitial(this, "24534e1901884e398f1253216226017e");
        mInterstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
                moPubInterstitial.show();
                mInterstitial = moPubInterstitial;
                Log.d("TAG", "onInterstitialLoaded: ");
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
                Log.d("TAG", "onInterstitialFailed: ");
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {
                Log.d("TAG", "onInterstitialShown: ");
            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {
                Log.d("TAG", "onInterstitialClicked: ");
            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
                Log.d("TAG", "onInterstitialDismissed: ");

            }
        });
        //
        // MoPub interstitial over




        dialogcout = getPreferences(Context.MODE_PRIVATE);
        editor = dialogcout.edit();

        oksure = getPreferences(Context.MODE_PRIVATE);
        okEditor = oksure.edit();

        oksurek = oksure.getBoolean("okSure", true);

        if (oksurek) {
            count = dialogcout.getInt("counter", 0);
            count++;
            editor.putInt("counter", count);
            editor.commit();
        }

        prefButton =  findViewById(R.id.pref);
        imageView =  findViewById(R.id.imageView1);

        widthAndHeight = getScreenWidth();
        widthAndHeightInDP = convertPixelsToDp(Math.round(widthAndHeight), this);
        halfImageView = widthAndHeightInDP / 2;
        SaveLayout = findViewById(R.id.saveLayout);
        setImage();
        sgd = new ScaleGestureDetector(this, new ScaleListener());
    }

    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
           /* MoPub SDK initialized.
           Check if you should show the consent dialog here, and make your ad requests. */
            }
        };
    }

    private void adsShow() {
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setUserID("unique_user_id")
                .setKeepScreenOn(true);

        AdColony.configure(this, appOptions, "app801b6402e4b741ad8b", "vza6ef94e4656240dca5");

        adOptionsI = new AdColonyAdOptions();

        listenerI = new AdColonyInterstitialListener() {

            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                Activity_Main.this.ad = ad;
                Log.d("TAG1", "onRequestFilled");
                ad.show();

            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
                // Ad request was not filled
                Log.d("TAG1", "onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change

                Log.d("TAG1", "onOpened");
            }

            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);

//                AdColony.requestInterstitial("vza6ef94e4656240dca5", this, adOptionsI);
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring

                AdColony.requestInterstitial("vza6ef94e4656240dca5", this, adOptionsI);
                Log.d("TAG", "onExpiring");
            }
        };

    }

    public void addImageClicked(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure select another image ?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Activity_Main.super.onBackPressed();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void OnBack(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure want to back?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Activity_Main.super.onBackPressed();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void prefClicked(View view) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Toast.makeText(Activity_Main.this, "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        imageView.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    public void setMoreBtn(View moreBtn) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You want to More Apps? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent iapps = new Intent("android.intent.action.VIEW");
                        iapps.setData(Uri.parse("https://play.google.com/store/apps/developer?id=" + getResources().getString(R.string.developer)));
                        startActivity(iapps);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }


    public void pref3Clicked(View view) {
        widthOfScreen = getScreenWidth();
        Bundle ex = getIntent().getExtras();
        Uri imageUri = ex.getParcelable("ImageUri");

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap dstBmp;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        } else {
            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }
        Bitmap blurredBitmap = blur(dstBmp);
        BitmapDrawable ob = new BitmapDrawable(getResources(), blurredBitmap);
        imageView.setBackgroundDrawable(ob);
    }

    public void pref4Clicked(View view) {
        Bitmap origBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Matrix matrix = new Matrix();
        matrix.postRotate(-90.0f);
        Bitmap rotatedBitmap = Bitmap.createBitmap(origBitmap, 0, 0, origBitmap.getWidth(), origBitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(rotatedBitmap);
    }

    public void saveTextViewClicked(View view) {





        if (imageView.getDrawable() == null) {
            Toast.makeText(getApplicationContext(), "Please, First add an image to save.", Toast.LENGTH_SHORT).show();
        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(Activity_Main.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_STORAGE);
                }
            } else {
                startSave();
            }
        }
    }

    public void padding1Clicked(View view) {
        imageView.setPadding(0, 0, 0, 0);
    }

    public void padding2Clicked(View view) {
        imageView.setPadding(40, 40, 40, 40);
    }

    public void padding3Clicked(View view) {
        imageView.setPadding(80, 80, 80, 80);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            Log.i("Scale:", Float.toString(scale));
            int padding = Math.round(halfImageView * (2 + (scale * -1)));
            imageView.setPadding(padding, padding, padding, padding);
            return true;
        }
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float scale = context.getResources().getDisplayMetrics().densityDpi;
        float dp = px / (scale / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    private void setImage() {
        widthOfScreen = getScreenWidth();
        Bundle ex = getIntent().getExtras();
        Uri imageUri = ex.getParcelable("ImageUri");
        RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(widthAndHeight, widthAndHeight);
        imageView.setLayoutParams(newParams);
        imageView.setBackgroundColor(getResources().getColor(R.color.white));
        imageView.setPadding(0, 0, 0, 0);
        imageView.setImageURI(imageUri);
    }

    private static final float BLUR_RADIUS = 25f;

    @SuppressLint("NewApi")
    public Bitmap blur(Bitmap image) {
        if (null == image) return null;
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public boolean onTouchEvent(MotionEvent event) {
        sgd.onTouchEvent(event);
        return true;
    }
    CharSequence options[] = new CharSequence[]{"Same Quality", "Standard", "Normal"};


    public void startSave() {
        SaveLayout.setDrawingCacheEnabled(true);
        final Bitmap bitmap = Bitmap.createBitmap(SaveLayout.getDrawingCache());
        SaveLayout.destroyDrawingCache();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Select your option:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Process_Save saveFile = new Process_Save();
                switch (which) {
                    case 0:

                        mInterstitial.load();
                        saveFile.SaveImage(Activity_Main.this, bitmap, 100);
                        break;
                    case 1:

                        mInterstitial.load();
                        saveFile.SaveImage(Activity_Main.this, bitmap, 80);
                        break;
                    case 2:

                        mInterstitial.load();
                        saveFile.SaveImage(Activity_Main.this, bitmap, 50);
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();


    }


    @Override
    public void onBackPressed() {
        if (count == setcout) {
            count = 0;
            editor.putInt("counter", count);
            editor.commit();
            dialog();
        } else {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure to Exit ?")
                    .setNeutralButton("Rate As", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent new_intent = Intent.createChooser(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())), "Share via");
                            new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(new_intent);
                        }
                    })
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                                dialog.cancel();


                                final ProgressDialog pDialog = new ProgressDialog(Activity_Main.this); //Your Activity.this
                                pDialog.setMessage("Exit soon..");
                                pDialog.setCancelable(false);
                                pDialog.show();


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialog.cancel();

                                            dialog.dismiss();
                                            moveTaskToBack(true);
                                            android.os.Process.killProcess(android.os.Process.myPid());
                                            System.exit(1);

                                    }
                                }, 5000);

                        }
                    })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Activity_Main.super.onBackPressed();
                        }
                    })
                    .show();
        }
    }

    public void dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enjoying " + getResources().getString(R.string.app_name) + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialogYes();
                    }
                })
                .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        dialogNotReally();
                    }
                })
                .show();
    }


    public void dialogNotReally() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Would you mind giving us some feedback?")
                .setPositiveButton("Ok, sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent new_intent = Intent.createChooser(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())), "Share via");
                        new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(new_intent);
                        okEditor.putBoolean("okSure", false);
                        okEditor.commit();
                    }
                })
                .setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                            dialog.cancel();

                            final ProgressDialog pDialog = new ProgressDialog(Activity_Main.this); //Your Activity.this
                            pDialog.setMessage("Exit soon..");
                            pDialog.setCancelable(false);
                            pDialog.show();


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.cancel();

                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);

                                }
                            }, 5000);

                    }
                })
                .show();
    }

    public void dialogYes() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How about a Rating on Play store, then?")
                .setPositiveButton("Ok, sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent new_intent = Intent.createChooser(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())), "Share via");
                        new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(new_intent);
                        okEditor.putBoolean("okSure", false);
                        okEditor.commit();
                    }
                })
                .setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                            dialog.cancel();

                            final ProgressDialog pDialog = new ProgressDialog(Activity_Main.this); //Your Activity.this
                            pDialog.setMessage("Exit soon..");
                            pDialog.setCancelable(false);
                            pDialog.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.cancel();
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);

                                }
                            }, 5000);
                    }
                })
                .show();
    }
}