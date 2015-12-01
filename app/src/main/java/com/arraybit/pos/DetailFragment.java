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
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

@SuppressWarnings("unchecked")
@SuppressLint("ValidFragment")
public class DetailFragment extends Fragment implements View.OnClickListener {

    ImageView ivItemImage;
    TextView txtItemName, txtDescription, txtItemPrice;
    EditText etQuantity;
    ImageButton btnPlus, btnMinus;
    int ItemMasterId;

    ItemJSONParser objItemJSONParser;
    ItemMaster objItemMaster;

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
        btnPlus = (ImageButton) view.findViewById(R.id.btnPlus);
        btnMinus = (ImageButton) view.findViewById(R.id.btnMinus);
        //end

        btnMinus.setOnClickListener(this);
        btnPlus.setOnClickListener(this);

        setHasOptionsMenu(true);

        new DetailLoadingTask().execute();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private int IncrementDecrementValue(int id, int value) {
        if (id == R.id.btnPlus) {
            value++;
            etQuantity.setText(String.valueOf(value));
        } else {
            if (value > 1) {
                value--;
            }
            etQuantity.setText(String.valueOf(value));
        }
        return value;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlus) {
            IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
        }
        if (v.getId() == R.id.btnMinus) {
            IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
        }
    }

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
            if (!objItemMaster.getImageName().equals("null")) {
                Picasso.with(ivItemImage.getContext()).load(objItemMaster.getImageName()).into(ivItemImage);
            } else {
                ivItemImage.setImageResource(R.drawable.vada_paav);
            }
        }
    }
}
