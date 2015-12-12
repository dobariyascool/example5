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
    ArrayList<String> alStringFilter;
    ArrayAdapter<String> adapter;
    String[] selectedValue;
    boolean isDeleted;

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

        Button btnNum1 = (Button) view.findViewById(R.id.btnNum1);
        Button btnNum2 = (Button) view.findViewById(R.id.btnNum2);
        Button btnNum3 = (Button) view.findViewById(R.id.btnNum3);
        Button btnNum4 = (Button) view.findViewById(R.id.btnNum4);
        Button btnNum5 = (Button) view.findViewById(R.id.btnNum5);
        Button btnNum6 = (Button) view.findViewById(R.id.btnNum6);
        Button btnNum7 = (Button) view.findViewById(R.id.btnNum7);
        Button btnNum8 = (Button) view.findViewById(R.id.btnNum8);
        Button btnNum9 = (Button) view.findViewById(R.id.btnNum9);

        actRemark = (AppCompatMultiAutoCompleteTextView) view.findViewById(R.id.actRemark);

        textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);

        ibPlus.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        textInputLayout.setOnClickListener(this);
        actRemark.setOnClickListener(this);

        btnNum1.setOnClickListener(this);
        btnNum2.setOnClickListener(this);
        btnNum3.setOnClickListener(this);
        btnNum4.setOnClickListener(this);
        btnNum5.setOnClickListener(this);
        btnNum6.setOnClickListener(this);
        btnNum7.setOnClickListener(this);
        btnNum8.setOnClickListener(this);
        btnNum9.setOnClickListener(this);

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
            //etQuantity.selectAll();

        } else if (v.getId() == R.id.ibMinus) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            textInputLayout.clearFocus();
            etQuantity.requestFocus();
            ///etQuantity.selectAll();

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
            } else {
                if (isDeleted) {
                    if (actRemark.getText().subSequence((int) actRemark.length() - 1, (int) actRemark.length()).toString().equals(",")) {
                        selectedValue = String.valueOf(actRemark.getText().subSequence(0, (int) actRemark.length()) + " ").split(", ");
                    } else if (actRemark.getText().subSequence((int) actRemark.length() - 1, (int) actRemark.length()).toString().equals(" ")) {
                        selectedValue = actRemark.getText().subSequence(0, (int) actRemark.length()).toString().split(", ");
                    } else {
                        selectedValue = actRemark.getText().subSequence(0, (int) actRemark.length()).toString().split(", ");
                        actRemark.setText(actRemark.getText() + ", ");
                    }
                    UpdateArrayListAdapter(null);
                    isDeleted = false;
                }

            }
            actRemark.showDropDown();
        } else if (v.getId() == R.id.btnNum1) {
            AddNumber("1");
        } else if (v.getId() == R.id.btnNum2) {
            AddNumber("2");
        } else if (v.getId() == R.id.btnNum3) {
            AddNumber("3");
        } else if (v.getId() == R.id.btnNum4) {
            AddNumber("4");
        } else if (v.getId() == R.id.btnNum5) {
            AddNumber("5");
        } else if (v.getId() == R.id.btnNum6) {
            AddNumber("6");
        } else if (v.getId() == R.id.btnNum7) {
            AddNumber("7");
        } else if (v.getId() == R.id.btnNum8) {
            AddNumber("8");
        } else if (v.getId() == R.id.btnNum9) {
            AddNumber("9");
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

    private void UpdateArrayListAdapter(String name) {
        int isRemove = -1;
        if (name == null && isDeleted) {
            alStringFilter = new ArrayList<>();
            for (int i = 0; i < selectedValue.length; i++) {
                if (!selectedValue[i].equals("")) {
                    if (alStringFilter.size() == 0) {
                        for (int j = 0; j < alString.size(); j++) {
                            if (!selectedValue[i].equals(alString.get(j))) {
                                alStringFilter.add(alString.get(j));
                            }
                        }
                    } else {
                        for (int j = 0; j < alStringFilter.size(); j++) {
                            String str = selectedValue[i];
                            if (alStringFilter.get(j).equals(str)) {
                                alStringFilter.remove(j);
                            }
                        }
                    }
                }
            }
            adapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringFilter);
            actRemark.setAdapter(adapter);
        } else {
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
    private void AddNumber(String number) {
        textInputLayout.clearFocus();
        etQuantity.requestFocus();
        etQuantity.setText(number);
    }
    //endregion

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

                actRemark.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL) {
                            if (actRemark.getText().toString().isEmpty()) {
                                SetArrayListAdapter(alString);
                                isDeleted = false;
                            } else {
                                isDeleted = true;
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

