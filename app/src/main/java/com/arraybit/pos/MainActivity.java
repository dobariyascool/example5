package com.arraybit.pos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONStringer;

public class MainActivity extends AppCompatActivity {

    static int i = 0;
    LinearLayout layout1;
    JSONStringer stringer;
    ProgressDialog  progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WebView load = (WebView)findViewById(R.id.load);

//        Button btnAdd = (Button)findViewById(R.id.button);
//        int j = i;
//        final ImageView sharedImage = (ImageView) findViewById(R.id.sharedimage);
//        sharedImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //This is where the magic happens.
//                // makeSceneTransitionAnimation takes a context, view,
//                // a name for the target view.
////                ActivityOptions options =
////                        ActivityOptions.
////                                makeSceneTransitionAnimation(MainActivity.this, sharedImage, "sharedImage");
////                Intent intent = new Intent(MainActivity.this, GuestOrderActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                startActivity(intent, options.toBundle());
//
//                Slide slideTransition = new Slide(Gravity.RIGHT);
//                slideTransition.setDuration(500);
//
//                ChangeBounds changeBoundsTransition = new ChangeBounds();
//                changeBoundsTransition.setDuration(500);
//
//                ProfileFragment fragment = new ProfileFragment();
//                fragment.setEnterTransition(slideTransition);
//                fragment.setAllowEnterTransitionOverlap(true);
//                fragment.setAllowReturnTransitionOverlap(true);
//                fragment.setSharedElementEnterTransition(sharedImage);
//
//
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(android.R.id.content, fragment);
//                fragmentTransaction.addToBackStack(null);
//                //fragmentTransaction.addSharedElement(sharedImage,"sharedImage");
//                fragmentTransaction.commit();
//
//            }
//        });
//
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                fragmentTransaction.addSharedElement(sharedImage, getString(R.string.blue_name))
////                fragmentTransaction.add(android.R.id.content,new SignUpFragment());
//////                fragmentTransaction.setCustomAnimations(R.anim.right_in,R.anim.left_out,R.anim.right_in,R.anim.left_out);
////                //fragmentTransaction.add(android.R.id.content, new AllTablesFragment(getActivity(), false, String.valueOf(Globals.OrderType.DineIn.getValue())), getActivity().getResources().getString(R.string.title_fragment_all_tables));
////                fragmentTransaction.addToBackStack(null);
//            }
//        });




        //int j = i;

        RequestQueue queue = Volley.newRequestQueue(this);

//        try{
//
//            stringer = new JSONStringer();
//            stringer.object();
//
//            stringer.key("orderMaster");
//            stringer.object();
//
//            stringer.key("OrderNumber").value(1);
//            stringer.key("linktoCounterMasterId").value(1);
//            stringer.key("linktoTableMasterIds").value(1);
//            stringer.key("linktoWaiterMasterId").value(1);
//            stringer.key("linktoOrderTypeMasterId").value(1);
//            stringer.key("linktoOrderStatusMasterId").value(1);
//            stringer.key("TotalAmount").value(0.00);
//            stringer.key("TotalTax").value(0.00);
//            stringer.key("Discount").value(0.00);
//            stringer.key("ExtraAmount").value(0.00);
//            stringer.key("TotalItemPoint").value(10);
//            stringer.key("TotalDeductedPoint").value(10);
//
//            stringer.endObject();
//
//            stringer.key("lstOrderItemTran");
//            stringer.array();
//
//            for(int i=0;i<3;i++) {
//                stringer.object();
//                stringer.key("ItemMasterId").value(12);
//                stringer.key("Quantity").value(1);
//                stringer.key("SellPrice").value(0.00);
//                stringer.key("Remark").value("remark");
//                stringer.key("lstOrderItemModifierTran");
//                stringer.array();
//                for(int j=0;j<2;j++){
//                    stringer.object();
//                    stringer.key("ItemModifierMasterIds").value(1);
//                    stringer.key("MRP").value("1");
//                    stringer.endObject();
//                }
//                stringer.endArray();
//                stringer.endObject();
//            }
//            stringer.endArray();
//
//            stringer.endObject();
//
//        }catch (Exception e){
//
//        }

//        try {
//
//            stringer = new JSONStringer();
//            stringer.key("lstOrderItemTran");
//            stringer.array();
//
//            for (int i = 0; i < 3; i++) {
//                stringer.object();
//                stringer.key("ItemMasterId").value(12);
//                stringer.key("Quantity").value(1);
//                stringer.key("SellPrice").value(0.00);
//                stringer.key("Remark").value("remark");
//                stringer.key("lstOrderItemModifierTran");
//                stringer.array();
//                for (int j = 0; j < 2; j++) {
//                    stringer.object();
//                    stringer.key("ItemModifierMasterIds").value(1);
//                    stringer.key("MRP").value("1");
//                    stringer.endObject();
//                }
//                stringer.endArray();
//                stringer.endObject();
//            }
//            stringer.endArray();
//
//            stringer.endObject();
//
//        } catch (Exception e) {
//
//        }
//
//        String url = "http://10.0.3.2:3653/Service.svc/InsertMaster";
        //http://api.androidhive.info/volley/string_response.html
        String url  = "file:///android_asset/privacy_policy.html";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                String str = s;
//                load.loadUrl(str);
//            }
//        },new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//

        //queue.add(stringRequest);

        //WebSettings webSettings = load.getSettings();
       // webSettings.setJavaScriptEnabled(true);
        //webSettings.setDatabaseEnabled(true);
        //webSettings.setDomStorageEnabled(true);
        //webSettings.setAppCacheEnabled(true);
        //webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
       // webSettings.setBlockNetworkLoads(true);
        //webSettings.setUseWideViewPort(true);
        //webSettings.setOffscreenPreRaster(true);



//        load.getSettings().setJavaScriptEnabled(true); load.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        load.getSettings().setBlockNetworkLoads(true);
//        load.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        if (Build.VERSION.SDK_INT >= 19) {
//            load.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }
//        else {
//            load.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

//        }
//        progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
//        progressDialog.setIndeterminate(true);
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        //load.setWebViewClient(new webCont());
        load.loadUrl(url);


//        JsonObjectRequest jsonObjectRequest = null;
//        try {
//            jsonObjectRequest = new JsonObjectRequest(1,url,new JSONObject(stringer.toString()),new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//                        JSONObject object = jsonObject;
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                    }
//                });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        queue.add(jsonObjectRequest);


//


//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET,url,new JSONObject(),new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                JSONObject object = jsonObject;
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });

        //queue.add(jsonObjectRequest);


        //WebView web = (WebView) findViewById(R.id.webview);
        //final ImageView imageView = (ImageView)findViewById(R.id.image);

        //Picasso.with(MainActivity.this).load(R.drawable.arraybit_5).into(imageView);
        //Glide.with(MainActivity.this).load(R.drawable.arraybit_5).asGif().into(imageView);
//        Glide.with(MainActivity.this).load(R.drawable.default_image).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                imageView.setImageDrawable(circularBitmapDrawable);
//            }
//        });


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

//    class webCont extends WebViewClient{
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return super.shouldOverrideUrlLoading(view, url);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            progressDialog.dismiss();
//        }
//    }
}
