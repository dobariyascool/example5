package com.arraybit.pos;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ToggleButton;

import com.arraybit.adapter.DiscountAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.modal.DiscountMaster;
import com.arraybit.parser.DiscountJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class AddDiscountDialogFragment extends DialogFragment implements View.OnClickListener {


    ArrayList<DiscountMaster> alDiscountMaster;
    ArrayList<String> alStringDiscount, alStringDiscountTitle;
    ArrayList<Boolean> alIsPercentage;
    AppCompatAutoCompleteTextView actDiscount;
    EditText etDiscount;
    StringBuilder sbDiscount = new StringBuilder();
    boolean isDotClick = false;
    String strDiscount;
    ToggleButton tbPercentage, tbRupee;
    DiscountSelectionListener objDiscountSelectionListener;
    DiscountMaster objDiscountMaster;
    double totalAmount, discount;
    boolean isPercentage;

    public AddDiscountDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_discount_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        etDiscount = (EditText) view.findViewById(R.id.etDiscount);

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
        Button btnNum0 = (Button) view.findViewById(R.id.btnNum0);
        Button btnDot = (Button) view.findViewById(R.id.btnDot);
        ImageButton btnClear = (ImageButton) view.findViewById(R.id.btnClear);

        actDiscount = (AppCompatAutoCompleteTextView) view.findViewById(R.id.actDiscount);

        tbPercentage = (ToggleButton) view.findViewById(R.id.tbPercentage);
        tbRupee = (ToggleButton) view.findViewById(R.id.tbRupee);

        totalAmount = getArguments().getDouble("TotalAmount", 0);
        discount = getArguments().getDouble("Discount", 0);
        isPercentage = getArguments().getBoolean("isPercentage", false);

        actDiscount.setOnClickListener(this);
        etDiscount.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnNum1.setOnClickListener(this);
        btnNum2.setOnClickListener(this);
        btnNum3.setOnClickListener(this);
        btnNum4.setOnClickListener(this);
        btnNum5.setOnClickListener(this);
        btnNum6.setOnClickListener(this);
        btnNum7.setOnClickListener(this);
        btnNum8.setOnClickListener(this);
        btnNum9.setOnClickListener(this);
        btnNum0.setOnClickListener(this);
        btnDot.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        tbPercentage.setOnClickListener(this);
        tbRupee.setOnClickListener(this);

        if (Service.CheckNet(getActivity())) {
            new ModifierLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(getActivity().getCurrentFocus(), getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        if(discount!=0)
        {
            etDiscount.setText(String.valueOf((int) discount));
       etDiscount.setSelection(etDiscount.getText().length());
            if(isPercentage)
            {
                tbPercentage.setChecked(true);
                tbRupee.setChecked(false);
            }
            else
            {
                tbPercentage.setChecked(false);
                tbRupee.setChecked(true);
            }
        }

        etDiscount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ClearFocus(v);
                }
            }
        });

        objDiscountSelectionListener = (DiscountSelectionListener) getTargetFragment();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.actDiscount) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            tbPercentage.setChecked(false);
            tbRupee.setChecked(false);
            actDiscount.showDropDown();
        } else if (v.getId() == R.id.btnCancel) {
            Globals.HideKeyBoard(getActivity(), v);
            getDialog().dismiss();
        } else if (v.getId() == R.id.btnOk) {
            Globals.HideKeyBoard(getActivity(), v);
            if (CheckValidation()) {
                getDialog().dismiss();
                if (actDiscount.getText().toString().isEmpty() && (!etDiscount.getText().toString().isEmpty())) {
                    objDiscountMaster = new DiscountMaster();
                    objDiscountMaster.setIsPercentage(tbPercentage.isChecked());
                    if (tbRupee.isChecked()) {
                        strDiscount = etDiscount.getText().toString().trim().substring(0, 1);
                        if (strDiscount.equals("0")) {
                            strDiscount = etDiscount.getText().toString().trim().substring(1, etDiscount.getText().length());
                            if(!strDiscount.equals("") && strDiscount!=null) {
                                objDiscountMaster.setDiscount(Double.valueOf(strDiscount));
                            }
                            else {
                                objDiscountMaster.setDiscount(Double.valueOf(etDiscount.getText().toString().trim()));
                            }
                        } else {
                            objDiscountMaster.setDiscount(Double.valueOf(etDiscount.getText().toString().trim()));
                        }

                    } else {
                        objDiscountMaster.setDiscount(Double.valueOf(etDiscount.getText().toString().trim()));
                    }
                    objDiscountSelectionListener.DiscountCount(objDiscountMaster);
                } else {
                    objDiscountSelectionListener.DiscountCount(objDiscountMaster);
                }
            }
        } else if (v.getId() == R.id.btnNum0) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            SetDiscount(0, null, false);
        } else if (v.getId() == R.id.btnNum1) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            SetDiscount(1, null, false);
        } else if (v.getId() == R.id.btnNum2) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            SetDiscount(2, null, false);
        } else if (v.getId() == R.id.btnNum3) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            SetDiscount(3, null, false);
        } else if (v.getId() == R.id.btnNum4) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            SetDiscount(4, null, false);
        } else if (v.getId() == R.id.btnNum5) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            SetDiscount(5, null, false);
        } else if (v.getId() == R.id.btnNum6) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            SetDiscount(6, null, false);
        } else if (v.getId() == R.id.btnNum7) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            SetDiscount(7, null, false);
        } else if (v.getId() == R.id.btnNum8) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            SetDiscount(8, null, false);
        } else if (v.getId() == R.id.btnNum9) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            SetDiscount(9, null, false);
        } else if (v.getId() == R.id.btnDot) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            if (!isDotClick) {
                SetDiscount(0, ".", false);
                isDotClick = true;
            }
        } else if (v.getId() == R.id.btnClear) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            if (!etDiscount.getText().toString().equals("") && !etDiscount.getText().toString().isEmpty()) {
                SetDiscount(0, null, true);
            }
            if (!sbDiscount.toString().contains(".")) {
                isDotClick = false;
            }
        } else if (v.getId() == R.id.tbPercentage) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            if (tbRupee.isChecked()) {
                tbRupee.setChecked(false);
            }
            tbPercentage.setChecked(true);
        } else if (v.getId() == R.id.tbRupee) {
            ClearFocus(v);
            Globals.HideKeyBoard(getActivity(), v);
            if (tbPercentage.isChecked()) {
                tbPercentage.setChecked(false);
            }
            tbRupee.setChecked(true);
        }
    }

    //region Private Methods and Interface
    private void ClearFocus(View view) {
        if (view.getId() == R.id.actDiscount) {
            sbDiscount = new StringBuilder();
            etDiscount.clearError();
            etDiscount.setText("");
        } else if (view.getId() == R.id.etDiscount) {
            sbDiscount = new StringBuilder();
            etDiscount.setText("");
            actDiscount.clearFocus();
            actDiscount.setText("");
            actDiscount.setError(null);
            actDiscount.setHint(getActivity().getResources().getString(R.string.ddfDiscount));
        } else {
            etDiscount.clearFocus();
            etDiscount.clearError();
            actDiscount.clearFocus();
            actDiscount.setText("");
            actDiscount.setError(null);
            actDiscount.setHint(getActivity().getResources().getString(R.string.ddfDiscount));
        }
    }

    private void SetDiscount(int values, String strDot, boolean isClear) {
        if (!sbDiscount.toString().isEmpty()) {
            if (isClear) {
                sbDiscount.delete(sbDiscount.length() - 1, sbDiscount.length());
                etDiscount.setText(sbDiscount.toString());
            } else {
                if (strDot != null) {
                    sbDiscount.append(strDot);
                } else {
                    sbDiscount.append(values);
                }
                etDiscount.setText(sbDiscount.toString());
            }
        } else {
            if (strDot != null) {
                sbDiscount.append(strDot);
            } else {
                sbDiscount.append(values);
            }
            etDiscount.setText(sbDiscount.toString());
        }
    }

    private boolean CheckValidation() {
        if (etDiscount.getText().toString().isEmpty() && actDiscount.getText().toString().isEmpty()) {
            etDiscount.setError(getActivity().getResources().getString(R.string.ddfError));
            return false;
        } else if ((!tbPercentage.isChecked()) && (!tbRupee.isChecked()) && actDiscount.getText().toString().isEmpty() && (!etDiscount.getText().toString().isEmpty())) {
            etDiscount.setError(getActivity().getResources().getString(R.string.ddfErrorDiscountType));
            return false;
        } else if (tbPercentage.isChecked() &&
                (sbDiscount.toString().length() == 3 && !sbDiscount.toString().equals("100")
                        || sbDiscount.toString().length() > 4 && !sbDiscount.toString().equals("100") && sbDiscount.toString().contains("."))) {
            etDiscount.setError(getActivity().getResources().getString(R.string.ddfErrorDiscount));
            return false;
        }else if(tbRupee.isChecked() && !etDiscount.getText().toString().isEmpty()
                 && Double.valueOf(etDiscount.getText().toString()) >= totalAmount){
            etDiscount.setError(getActivity().getResources().getString(R.string.ddfErrorDiscount));
            return false;
        }
        return true;
    }

    interface DiscountSelectionListener {
        void DiscountCount(DiscountMaster objDiscountMaster);
    }
    //endregion

    //region LoadingTask
    class ModifierLoadingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            DiscountJSONParser objDiscountJSONParser = new DiscountJSONParser();
            alDiscountMaster = objDiscountJSONParser.SelectAllDiscountMaster(Globals.businessMasterId);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (alDiscountMaster != null && alDiscountMaster.size() != 0) {
                alStringDiscount = new ArrayList<>();
                alStringDiscountTitle = new ArrayList<>();
                alIsPercentage = new ArrayList<>();
                for (int i = 0; i < alDiscountMaster.size(); i++) {
                    alStringDiscount.add(String.valueOf(alDiscountMaster.get(i).getDiscount()));
                    alStringDiscountTitle.add(String.valueOf(alDiscountMaster.get(i).getDiscountTitle()));
                    alIsPercentage.add(alDiscountMaster.get(i).getIsPercentage());
                }
                actDiscount.setAdapter(new DiscountAdapter(getActivity(), alStringDiscountTitle, alStringDiscount, alIsPercentage));
                actDiscount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        objDiscountMaster = new DiscountMaster();
                        objDiscountMaster.setIsPercentage(alIsPercentage.get(position));
                        objDiscountMaster.setDiscount(Double.valueOf(alStringDiscount.get(position)));
                    }
                });
            }
        }
    }
    //endregion
}
