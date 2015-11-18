package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
@SuppressLint("ValidFragment")
public class DetailFragment extends Fragment {

    public int value = 1;
    ImageView ivItemImage;
    TextView txtItemName, txtDescription, txtItemPrice;
    EditText etQuantity;
    Button btnPlus, btnMinus;
    int ItemMasterId;

    ItemJSONParser objItemJSONParser;
    ItemMaster objItemMaster;
    ArrayList<ItemMaster> alItemMaster;


    public DetailFragment(int ItemMasterId) {
        this.ItemMasterId = ItemMasterId;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //ImageView
        ivItemImage = (ImageView) view.findViewById(R.id.ivItemImage);
        //end

        //TextView
        txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        txtItemPrice = (TextView) view.findViewById(R.id.txtItemPrice);
        etQuantity = (EditText) view.findViewById(R.id.etQuantity);
        //end

        //Button
        btnPlus = (Button) view.findViewById(R.id.btnPlus);
        btnMinus = (Button) view.findViewById(R.id.btnMinus);
        //end

//        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
//
//        if (app_bar != null) {
//            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            value = 1;
//        }
//        app_bar.setTitle(getResources().getString(R.string.title_fragment_detail));
//        app_bar.setLogo(R.mipmap.app_logo);

        //setHasOptionsMenu(true);

//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.title_fragment_detail));
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);

        new DetailLoadingTask().execute();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityValue();
                value++;
                String _stringValue = Integer.toString(value);
                etQuantity.setText(_stringValue);
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityValue();
                if (value > 1) {
                    value--;
                }
                String _stringValue = Integer.toString(value); //for converting integer value into string
                etQuantity.setText(_stringValue);
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    private int quantityValue(){
        value = Integer.parseInt(etQuantity.getText().toString());
        return value;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == android.R.id.home) {
//            getActivity().getSupportFragmentManager().popBackStack();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public class DetailLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

            objItemJSONParser = new ItemJSONParser();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            objItemMaster = objItemJSONParser.SelectItemMaster(ItemMasterId);
            return objItemMaster;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            progressDialog.dismiss();

            txtItemName.setText(objItemMaster.getItemName());
            txtDescription.setText(objItemMaster.getShortDescription());
            //txtItemPrice.setText((String.valueOf(objItemMaster.getMRP())));

        }
    }
}
