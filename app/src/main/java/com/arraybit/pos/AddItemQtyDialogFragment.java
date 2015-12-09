package com.arraybit.pos;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.arraybit.global.Globals;
import com.arraybit.parser.ItemRemarkJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class AddItemQtyDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText etQuantity;
    ArrayList<String> alString;
    AppCompatMultiAutoCompleteTextView actRemark;
    TextInputLayout textInputLayout;
    ImageButton ibPlus;
    StringBuilder sb;
    ArrayList<String> alStringFilter;
    ArrayAdapter<String> adapter;

    public AddItemQtyDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item_qty_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ibPlus = (ImageButton) view.findViewById(R.id.ibPlus);
        ImageButton ibMinus = (ImageButton) view.findViewById(R.id.ibMinus);

        etQuantity = (EditText) view.findViewById(R.id.etQuantity);

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnOk = (Button) view.findViewById(R.id.btnOk);

        actRemark = (AppCompatMultiAutoCompleteTextView) view.findViewById(R.id.actRemark);

        textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);

        ibPlus.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        textInputLayout.setOnClickListener(this);
        actRemark.setOnClickListener(this);

        new RemarkLoadingTask().execute();

        return view;
    }

    //region Event
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ibPlus) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            textInputLayout.clearFocus();
            etQuantity.requestFocus();
            etQuantity.selectAll();

        } else if (v.getId() == R.id.ibMinus) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            textInputLayout.clearFocus();
            etQuantity.requestFocus();
            etQuantity.selectAll();

        } else if (v.getId() == R.id.btnCancel) {
            dismiss();
        } else if (v.getId() == R.id.btnOk) {
            dismiss();
        } else if (v.getId() == R.id.textInputLayout) {
            textInputLayout.setFocusable(true);
            etQuantity.setFocusable(false);
        } else if (v.getId() == R.id.actRemark) {
            if (actRemark.getText().toString().isEmpty()) {
                SetArrayListAdapter(alString);
            }
            actRemark.showDropDown();
        }
    }
    //endregion

    //region Private Methods
    private int IncrementDecrementValue(int id, int value) {
        if (id == R.id.ibPlus) {
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
    //endregion

    private void UpdateArrayListAdapter(String name) {
        int isRemove = -1;
        if (alStringFilter.size() == 0) {
            for (int i = 0; i < alString.size(); i++) {
                if (!alString.get(i).equals(name)) {
                    alStringFilter.add(alString.get(i));
                }
            }
            adapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringFilter);
            actRemark.setAdapter(adapter);

        } else {
            for (int j = 0; j < alStringFilter.size(); j++) {
                if (alStringFilter.get(j).equals(name)) {
                    isRemove = j;
                }
            }
            if (isRemove != -1) {
                alStringFilter.remove(isRemove);
            }
        }
        adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringFilter);
        actRemark.setAdapter(adapter);
    }

    private void SetArrayListAdapter(ArrayList<String> alString) {
        adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_dropdown_item, alString);
        actRemark.setAdapter(adapter);
    }

    //region Loading Task
    class RemarkLoadingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            ItemRemarkJSONParser objItemRemarkJSONParser = new ItemRemarkJSONParser();
            alString = objItemRemarkJSONParser.SelectAllItemRemarkMaster(Globals.businessMasterId);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (alString == null) {
            } else if (alString.size() == 0) {
            } else {

                alStringFilter = new ArrayList<>();

                actRemark.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                SetArrayListAdapter(alString);
                sb = new StringBuilder();

                actRemark.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL) {
                            //sb = new StringBuilder();
                            //if(!sb.toString().isEmpty())
                            //{
                            // sb.deleteCharAt((int)sb.toString().length()-1);
                            //}
                            if (actRemark.getText().toString().isEmpty()) {
                                SetArrayListAdapter(alString);
                            }
                        }
                        return false;
                    }
                });

                actRemark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        UpdateArrayListAdapter((String) parent.getAdapter().getItem(position));
                    }
                });
            }
        }
    }
    //endregion

}

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(R.layout.fragment_add_item_qty_dialog);
//        builder.setPositiveButton(getResources().getString(R.string.ldLogin), null);
//        builder.setNegativeButton(getResources().getString(R.string.ldCancel), null);
//        builder.setCancelable(false);
//        //setRetainInstance(false);
//
//        final AlertDialog alertDialog = builder.create();
//
//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//
//                Button positive = ((AlertDialog) alertDialog).getButton(DialogInterface.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//
//                Button negative = ((AlertDialog) alertDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dismiss();
//                    }
//                });
//
//            }
//        });
//        return alertDialog;
//    }

