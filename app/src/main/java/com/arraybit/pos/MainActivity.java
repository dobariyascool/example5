package com.arraybit.pos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //WebView web = (WebView) findViewById(R.id.webview);
        final ImageView imageView = (ImageView)findViewById(R.id.image);

        //Picasso.with(MainActivity.this).load(R.drawable.arraybit_5).into(imageView);
        //Glide.with(MainActivity.this).load(R.drawable.arraybit_5).asGif().into(imageView);
        Glide.with(MainActivity.this).load(R.drawable.vada_paav).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });


        //Picasso.with(MainActivity.this).load(R.drawable.arraybit_5).into(imageView);

//        WebView web = (WebView) findViewById(R.id.webview);
//        web.setBackgroundColor(Color.TRANSPARENT); //for gif without background
//        web.loadUrl("file:///android_asset/gifImage.html");

//        layout1 = (LinearLayout) findViewById(R.id.layout1);
//
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//
//        Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(getResources(), R.drawable.mainbackground), displayMetrics.widthPixels, displayMetrics.heightPixels);
//        layout1.setBackground(new BitmapDrawable(getResources(), bitmap));
//
//        Button btn=(Button)findViewById(R.id.btnAdd);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "btn click", Toast.LENGTH_LONG).show();
//            }
//        });


//        new CountDownTimer(100000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                System.out.println("seconds remaining: " + millisUntilFinished / 1000);
//            }
//
//            public void onFinish() {
//                System.out.println("done!");
//                Toast.makeText(MainActivity.this,"Task is done",Toast.LENGTH_LONG).show();
//            }
//        }.start();

       //DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mainbackground);
//        Matrix matrix = new Matrix();
//        matrix.postScale(0.5f, 0.5f);
//
//        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap,0,(bitmap.getHeight() - bitmap.getWidth()) / 2,displayMetrics.widthPixels,displayMetrics.heightPixels,matrix,false);
//        layout1.setBackground(new BitmapDrawable(getResources(),croppedBitmap));
//
//
//
//        BitmapFactory.Options options=new BitmapFactory.Options();
//        options.inSampleSize=1;
//        options.inScaled=true;
//        options.inDensity=displayMetrics.densityDpi;
//        options.inTargetDensity=displayMetrics.widthPixels*options.inSampleSize;

//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mainbackground, options);
//        //Bitmap b = Bitmap.createScaledBitmap(bitmap,displayMetrics.widthPixels,displayMetrics.heightPixels,false);
//        Drawable d = new BitmapDrawable(getResources(), bitmap);
//        layout1.setBackground(d);

//
//          layout1 = (LinearLayout) findViewById(R.id.layout1);
////
////
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//
//
//        Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.mainbackground);
////        System.out.println("width " + displayMetrics.widthPixels);
////        System.out.println("height " + displayMetrics.heightPixels);
//        Bitmap b = Bitmap.createScaledBitmap(original,original.getScaledWidth(displayMetrics.densityDpi),original.getScaledHeight(displayMetrics.densityDpi), false);
//        Drawable d = new BitmapDrawable(getResources(), b);


//        Matrix m = new Matrix();
//
//        m.setRectToRect(new RectF(0, 0, original.getWidth(), original.getHeight()), new RectF(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels), Matrix.ScaleToFit.CENTER);
//        Bitmap b1 = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), m, true);
//        Drawable d = new BitmapDrawable(getResources(), b1);

//        Matrix matrix=new Matrix();
//        matrix.preScale(displayMetrics.xdpi, displayMetrics.ydpi);
//        Bitmap b1 = Bitmap.createBitmap(original,0,0,displayMetrics.widthPixels,displayMetrics.heightPixels,matrix,false);
//        Drawable d = new BitmapDrawable(getResources(),b1);
//        layout1.setBackground(d);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
