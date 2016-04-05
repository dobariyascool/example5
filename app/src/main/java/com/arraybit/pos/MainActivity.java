package com.arraybit.pos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        progressView = (ProgressView) findViewById(R.id.progressView);
//
//        if (Service.CheckNet(this)) {
//            new TableSectionLoadingTask().execute();
//        } else {
//            Globals.ShowSnackBar(getCurrentFocus(), getResources().getString(R.string.MsgCheckConnection), this, 1000);
//        }


//        Globals.serverName ="10.0.3.2:3653";
//        Globals.ChangeUrl();
//
//        CoordinatorLayout coordinatorlayout = (CoordinatorLayout)findViewById(R.id.coordinatorlayout);
//
//        if(Service.CheckNet(MainActivity.this)){
//            Globals.ShowSnackBar(coordinatorlayout,"meassage",MainActivity.this,1000);
//        }
//
//        //FloatingActionMenu famRoot =(FloatingActionMenu)findViewById(R.id.famRoot);
//
//        FloatingActionButton fabAdd =(FloatingActionButton)findViewById(R.id.fabAdd);
//        fabAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Globals.ShowSnackBar(v,"meassage",MainActivity.this,1000);
//            }
//        });


//
//        RecyclerView listView = (RecyclerView)findViewById(R.id.lvToDoList);
//        ArrayList<String> alStringFilter = new ArrayList<>();
//        for(int i=0;i<100;i++){
//            alStringFilter.add("Filter "+i);
//        }
//
//        listView.setAdapter(new RecylerViewAdapter(MainActivity.this,alStringFilter));
//        listView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//
//        FloatingActionMenu famRoot =(FloatingActionMenu)findViewById(R.id.famRoot);
//        coordinatorlayout.onNestedScroll(listView,50,50,50,50);


        //final WebView load = (WebView)findViewById(R.id.load);

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
//        String url  = "file:///android_asset/privacy_policy.html";
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
        //       load.loadUrl(url);


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
//        new HotelLoadingTask().execute();
        progressDialog = new ProgressDialog();
        progressDialog.show(getSupportFragmentManager(), "");
        System.out.println("response" + "text");
        String url = "http://10.0.3.2:2293/Service.svc/SelectAllItemMasterByCategoryMasterId/1/0/1/null/null";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET,url,new JSONObject(),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                System.out.println("response" + "text1");
                JSONObject object = jsonObject;
                JSONObject jsonObject1 = object;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });

        queue.add(jsonObjectRequest);


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


//    class TableSectionLoadingTask extends AsyncTask {
//
//        ProgressDialog progressDialog;
//        ArrayList<SectionMaster> alSectionMaster;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
////            progressDialog = new ProgressDialog(getActivity());
////            progressDialog.setMessage(getActivity().getResources().getString(R.string.MsgLoading));
////            progressDialog.setIndeterminate(true);
////            progressDialog.setCancelable(false);
////            progressDialog.show();
//            Service.Url = "http://" + "10.0.3.2:6497" + "/Service.svc/";
//            progressView.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//
//            SectionJSONParser objSectionJSONParser = new SectionJSONParser();
//            alSectionMaster = objSectionJSONParser.SelectAllSectionMaster();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//
//            // progressDialog.dismiss();
//            // progressView.setVisibility(View.GONE);
//            if (alSectionMaster == null) {
//                ///Globals.ShowSnackBar(allTablesFragment, getActivity().getResources().getString(R.string.MsgSelectFail), getActivity(), 1000);
//
//            } else if (alSectionMaster.size() == 0) {
//                //Globals.ShowSnackBar(allTablesFragment, getActivity().getResources().getString(R.string.MsgNoRecord), getActivity(), 1000);
//
//            } else {
//                tableViewPager.setVisibility(View.VISIBLE);
//                tableTabLayout.setVisibility(View.VISIBLE);
//                if (isVacant) {
//                    famRoot.setVisibility(View.GONE);
//                } else {
//                    famRoot.setVisibility(View.VISIBLE);
//                }
//
//                tablePagerAdapter = new TablePagerAdapter(getFragmentManager());
//
//                SectionMaster objSectionMaster = new SectionMaster();
//                objSectionMaster.setSectionMasterId((short) 0);
//                objSectionMaster.setSectionName("All");
//                ArrayList<SectionMaster> alSection = new ArrayList<>();
//                alSection.add(objSectionMaster);
//
//                alSectionMaster.addAll(0, alSection);
//                for (int i = 0; i < alSectionMaster.size(); i++) {
//                    tablePagerAdapter.AddFragment(TableTabFragment.createInstance(alSectionMaster.get(i), isChangeMode, linktoOrderTypeMasterId), alSectionMaster.get(i));
//                }
//                tableViewPager.setAdapter(tablePagerAdapter);
//                tableTabLayout.setupWithViewPager(tableViewPager);
//
//                TableTabFragment tableTabFragment = (TableTabFragment) tablePagerAdapter.GetCurrentFragment(0);
//                if (isVacant) {
//                    tableTabFragment.LoadTableData(String.valueOf(Globals.TableStatus.Vacant.getValue()));
//                } else {
//                    tableTabFragment.LoadTableData(null);
//                }
//
//                tableViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                    @Override
//                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                    }
//
//                    @Override
//                    public void onPageSelected(int position) {
//                        if(famRoot.isMenuButtonHidden()){
//                            famRoot.showMenuButton(true);
//                        }
//                        tableViewPager.setCurrentItem(position);
//                        //load data when tab is change
//                        TableTabFragment tableTabFragment = (TableTabFragment) tablePagerAdapter.GetCurrentFragment(position);
//                        if (isVacant) {
//                            tableTabFragment.LoadTableData(String.valueOf(Globals.TableStatus.Vacant.getValue()));
//                        } else {
//                            tableTabFragment.LoadTableData(null);
//                        }
//                    }
//
//                    @Override
//                    public void onPageScrollStateChanged(int state) {
//
//                    }
//                });
//            }
//        }
//    }
        //537-681-144 537-785-248
    //693-817 709-937 09:31:07.829 09:31:09.073 893 761
    class HotelLoadingTask extends AsyncTask {

        ItemMaster objItemMaster;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog();
            progressDialog.show(getSupportFragmentManager(), "");
            System.out.println("response" + "text");
           // String url = "http://10.0.3.2:2293/Service.svc/SelectAllBusinessMaster";
            Service.Url = "http://10.0.3.2:2293/Service.svc/";
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ItemJSONParser objItemJSONParser = new ItemJSONParser();
            return objItemJSONParser.SelectAllItemMaster(1,1,0,null, Globals.businessMasterId);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            ArrayList<ItemMaster> arrayList = (ArrayList<ItemMaster>) result;
            System.out.println("response" + "text");
        }
    }

}
