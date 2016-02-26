package com.arraybit.pos;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
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
import com.arraybit.global.Service;
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemRemarkJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
@SuppressWarnings("unchecked")
public class AddItemQtyDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText etQuantity;
    ArrayList<String> alString, alStringFilter, alStringModifierFilter, alStringModifierRateFilter;
    AppCompatMultiAutoCompleteTextView actRemark;
    TextInputLayout textInputLayout;
    ImageButton ibPlus;
    ArrayAdapter<String> adapter;
    String[] selectedValue;
    boolean isDeleted;
    AddToCartListener objAddToCartListener;
    ItemMaster objItemMaster, objOrderItemTran;
    ArrayList<ItemMaster> alOrderItemModifierTran;
    boolean isDuplicate;
    double totalAmount;


    public AddItemQtyDialogFragment(ItemMaster objItemMaster) {
        this.objItemMaster = objItemMaster;
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
        Globals.EditTextFontTypeFace(etQuantity,getActivity());

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

        Globals.ButtonFontTypeFace(btnNum1,getActivity());
        Globals.ButtonFontTypeFace(btnNum2,getActivity());
        Globals.ButtonFontTypeFace(btnNum3,getActivity());
        Globals.ButtonFontTypeFace(btnNum4,getActivity());
        Globals.ButtonFontTypeFace(btnNum5,getActivity());
        Globals.ButtonFontTypeFace(btnNum6,getActivity());
        Globals.ButtonFontTypeFace(btnNum7,getActivity());
        Globals.ButtonFontTypeFace(btnNum8,getActivity());
        Globals.ButtonFontTypeFace(btnNum9,getActivity());
        Globals.ButtonFontTypeFace(btnOk,getActivity());
        Globals.ButtonFontTypeFace(btnCancel, getActivity());

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

        if (Service.CheckNet(getActivity())) {
            new RemarkLoadingTask().execute();
        } else {
            Globals.ShowSnackBar(getActivity().getCurrentFocus(), getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        objAddToCartListener = (AddToCartListener) getTargetFragment();

        SetTypeFace();

        return view;
    }

    //region Event
    @SuppressLint("SetTextI18n")
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

        } else if (v.getId() == R.id.ibMinus) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            textInputLayout.clearFocus();
            etQuantity.requestFocus();

        } else if (v.getId() == R.id.btnCancel) {
            objAddToCartListener.AddToCart(false, null);
            dismiss();
        } else if (v.getId() == R.id.btnOk) {
            SetOrderItemTran();
            objAddToCartListener.AddToCart(true, objOrderItemTran);
            dismiss();
        } else if (v.getId() == R.id.textInputLayout) {
            textInputLayout.setFocusable(true);
            etQuantity.setFocusable(false);
        } else if (v.getId() == R.id.actRemark) {
            if (actRemark.getText().toString().isEmpty()) {
                SetArrayListAdapter(alString);
            } else {
                if (isDeleted) {
                    if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(",")) {
                        selectedValue = String.valueOf(actRemark.getText().subSequence(0, actRemark.length()) + " ").split(", ");
                    } else if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(" ")) {
                        selectedValue = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
                    } else {
                        selectedValue = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
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

    //region Private Methods and Interface
    private void SetTypeFace(){
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
        actRemark.setTypeface(typeface);
        textInputLayout.setTypeface(typeface);
    }

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
        String str;
        try {
            if (name == null && isDeleted) {
                alStringFilter = new ArrayList<>();
                for (String strSelectedValue : selectedValue) {
                    if (!strSelectedValue.equals("")) {
                        if (alStringFilter.size() == 0) {
                            for (int j = 0; j < alString.size(); j++) {
                                if (!strSelectedValue.equals(alString.get(j))) {
                                    alStringFilter.add(alString.get(j));
                                }
                            }
                        } else {
                            for (int j = 0; j < alStringFilter.size(); j++) {
                                str = strSelectedValue;
                                if (alStringFilter.get(j).equals(str)) {
                                    alStringFilter.remove(j);
                                }
                            }
                        }
                    }
                }
                adapter = new ArrayAdapter<>
                        (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringFilter);
                actRemark.setAdapter(adapter);
            } else {
                if (alStringFilter.size() == 0) {
                    for (int i = 0; i < alString.size(); i++) {
                        if (!alString.get(i).equals(name)) {
                            alStringFilter.add(alString.get(i));
                        }
                    }
                    adapter = new ArrayAdapter<>
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
            adapter = new ArrayAdapter<>
                    (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringFilter);
            actRemark.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetArrayListAdapter(ArrayList<String> alString) {
        alStringFilter = new ArrayList<>();
        alStringModifierFilter = new ArrayList<>();
        alStringModifierRateFilter = new ArrayList<>();
        if (alString != null) {
            adapter = new ArrayAdapter<>
                    (getActivity(), android.R.layout.simple_spinner_dropdown_item, alString);
            actRemark.setAdapter(adapter);
        }
    }

    private void AddNumber(String number) {
        textInputLayout.clearFocus();
        etQuantity.requestFocus();
        etQuantity.setText(number);
    }

    private void SetOrderItemTran() {
        objOrderItemTran = new ItemMaster();
        try {
            if (Globals.alOrderItemTran.size() > 0) {
                CheckDuplicateRemarkAndModifier();
                if (!isDuplicate) {
                    objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                    objOrderItemTran.setItemName(objItemMaster.getItemName());
                    objOrderItemTran.setActualSellPrice(objItemMaster.getSellPrice());
                    objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                    objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                    objOrderItemTran.setRemark(actRemark.getText().toString());
                    totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice();
                    objOrderItemTran.setTotalAmount(totalAmount);
                    alOrderItemModifierTran = new ArrayList<>();
                    objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);

                }
            } else {
                objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                objOrderItemTran.setItemName(objItemMaster.getItemName());
                objOrderItemTran.setActualSellPrice(objItemMaster.getSellPrice());
                objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                objOrderItemTran.setRemark(actRemark.getText().toString());
                totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice();
                objOrderItemTran.setTotalAmount(totalAmount);
                alOrderItemModifierTran = new ArrayList<>();
                objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void CheckDuplicateRemarkAndModifier() {
        String[] strNewRemark, strOldRemark;
        int cnt;
        try {
            for (int i = 0; i < Globals.alOrderItemTran.size(); i++) {
                cnt = 0;
                if (objItemMaster.getItemMasterId() == Globals.alOrderItemTran.get(i).getItemMasterId()) {

                    if (!actRemark.getText().toString().isEmpty() && !Globals.alOrderItemTran.get(i).getRemark().isEmpty()) {

                        if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(",")) {
                            strNewRemark = String.valueOf(actRemark.getText().subSequence(0, actRemark.length()) + " ").split(", ");
                        } else if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(" ")) {
                            strNewRemark = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
                        } else {
                            strNewRemark = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
                        }


                        String listRemark = Globals.alOrderItemTran.get(i).getRemark();
                        if (listRemark.subSequence(listRemark.length() - 1, listRemark.length()).toString().equals(",")) {
                            strOldRemark = String.valueOf(listRemark.subSequence(0, listRemark.length()) + " ").split(", ");
                        } else if (listRemark.subSequence(listRemark.length() - 1, listRemark.length()).toString().equals(" ")) {
                            strOldRemark = listRemark.subSequence(0, listRemark.length()).toString().split(", ");
                        } else {
                            strOldRemark = listRemark.subSequence(0, listRemark.length()).toString().split(", ");
                        }

                        if (strNewRemark.length != 0) {
                            for (String newRemark : strNewRemark) {
                                for (String oldRemark : strOldRemark) {
                                    if (newRemark.equals(oldRemark)) {
                                        cnt = cnt + 1;
                                    }
                                }
                            }
                        }

                        if ((strNewRemark.length != 0 && cnt != 0 && strNewRemark.length == cnt)) {
                            isDuplicate = true;
                            Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                            Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()));
                            Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                            break;
                        }
                    } else {
                        if (actRemark.getText().toString().isEmpty() && Globals.alOrderItemTran.get(i).getRemark().isEmpty()) {
                            isDuplicate = true;
                            Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                            Globals.alOrderItemTran.get(i).setTotalAmount((Globals.alOrderItemTran.get(i).getTotalAmount()) + (Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice()));
                            Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface AddToCartListener {
        void AddToCart(boolean isAddToCart, ItemMaster objOrderItemTran);
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

            if (alString != null && alString.size() != 0) {
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
                        Globals.HideKeyBoard(getActivity(), textInputLayout);
                        UpdateArrayListAdapter((String) parent.getAdapter().getItem(position));
                    }
                });
            }
        }
    }
    //endregion
}

