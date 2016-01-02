package com.arraybit.pos;

import android.annotation.SuppressLint;
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
import com.arraybit.modal.ItemMaster;
import com.arraybit.parser.ItemJSONParser;
import com.arraybit.parser.ItemRemarkJSONParser;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class AddItemQtyDialogFragment extends DialogFragment implements View.OnClickListener {

    EditText etQuantity;
    ArrayList<String> alString, alStringFilter, alStringModifier, alStringModifierFilter;
    AppCompatMultiAutoCompleteTextView actRemark, actModifier;
    TextInputLayout textInputLayout, textInputLayoutModifier;
    ImageButton ibPlus;
    ArrayAdapter<String> adapter;
    String[] selectedValue, modifierSelectedValue, modifierValue;
    boolean isDeleted;
    AddToCartListener objAddToCartListener;
    ItemMaster objItemMaster, objOrderItemTran;
    ArrayList<ItemMaster> alOrderItemModifierTran, alItemMasterModifier;
    boolean isDuplicate;
    StringBuilder sbModifier;
    double totalModifierAmount, totalAmount;

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
        actModifier = (AppCompatMultiAutoCompleteTextView) view.findViewById(R.id.actModifier);

        textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);
        textInputLayoutModifier = (TextInputLayout) view.findViewById(R.id.textInputLayoutModifier);

        ibPlus.setOnClickListener(this);
        ibMinus.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        textInputLayout.setOnClickListener(this);
        actRemark.setOnClickListener(this);
        actModifier.setOnClickListener(this);

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

        new ModifierLoadingTask().execute();

        objAddToCartListener = (AddToCartListener) getTargetFragment();

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
            textInputLayoutModifier.clearFocus();
            etQuantity.requestFocus();
            //etQuantity.selectAll();

        } else if (v.getId() == R.id.ibMinus) {
            if (etQuantity.getText().toString().equals("")) {
                etQuantity.setText("1");
            } else {
                IncrementDecrementValue(v.getId(), Integer.valueOf(etQuantity.getText().toString()));
            }
            textInputLayout.clearFocus();
            textInputLayoutModifier.clearFocus();
            etQuantity.requestFocus();

        } else if (v.getId() == R.id.btnCancel) {
            objAddToCartListener.AddToCart(false, null);
            dismiss();
        } else if (v.getId() == R.id.btnOk) {
            SetOrderItemModifierTran();
            SetOrderItemTran();
            objAddToCartListener.AddToCart(true, objOrderItemTran);
            dismiss();
        } else if (v.getId() == R.id.textInputLayout) {
            textInputLayout.setFocusable(true);
            textInputLayoutModifier.setFocusable(false);
            etQuantity.setFocusable(false);
        } else if (v.getId() == R.id.textInputLayoutModifier) {
            textInputLayoutModifier.setFocusable(true);
            etQuantity.setFocusable(false);
            textInputLayout.setFocusable(false);
        } else if (v.getId() == R.id.actRemark) {
            if (actRemark.getText().toString().isEmpty()) {
                SetArrayListAdapter(alString, null);
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
                    UpdateArrayListAdapter(null, null, false);
                    isDeleted = false;
                }
            }
            actRemark.showDropDown();
        } else if (v.getId() == R.id.actModifier) {
            if (actModifier.getText().toString().isEmpty()) {
                SetArrayListAdapter(null, alStringModifier);
            } else {
                if (isDeleted) {
                    if (actModifier.getText().subSequence(actModifier.length() - 1, actModifier.length()).toString().equals(",")) {
                        modifierSelectedValue = String.valueOf(actModifier.getText().subSequence(0, actModifier.length()) + " ").split(", ");
                    } else if (actModifier.getText().subSequence(actModifier.length() - 1, actModifier.length()).toString().equals(" ")) {
                        modifierSelectedValue = actModifier.getText().subSequence(0, actModifier.length()).toString().split(", ");
                    } else {
                        modifierSelectedValue = actModifier.getText().subSequence(0, actModifier.length()).toString().split(", ");
                        actModifier.setText(actModifier.getText() + ", ");
                    }
                    UpdateArrayListAdapter(null, null, true);
                    isDeleted = false;
                }

            }
            actModifier.showDropDown();
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

    private void UpdateArrayListAdapter(String name, String modifierName, boolean isModifier) {
        int isRemove = -1;
        String str;
        try {
            if (!isModifier) {
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
            } else {
                if (modifierName == null && isDeleted) {
                    alStringModifierFilter = new ArrayList<>();
                    for (String strSelectedValue : modifierSelectedValue) {
                        if (!strSelectedValue.equals("")) {
                            if (alStringModifierFilter.size() == 0) {
                                for (int j = 0; j < alStringModifier.size(); j++) {
                                    if (!strSelectedValue.equals(alStringModifier.get(j))) {
                                        alStringModifierFilter.add(alStringModifier.get(j));
                                    }
                                }
                            } else {
                                for (int j = 0; j < alStringModifierFilter.size(); j++) {
                                    str = strSelectedValue;
                                    if (alStringModifierFilter.get(j).equals(str)) {
                                        alStringModifierFilter.remove(j);
                                    }
                                }
                            }
                        }
                    }
                    adapter = new ArrayAdapter<>
                            (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringModifierFilter);
                    actModifier.setAdapter(adapter);
                } else {
                    if (alStringModifierFilter.size() == 0) {
                        for (int i = 0; i < alStringModifier.size(); i++) {
                            if (!alStringModifier.get(i).equals(modifierName)) {
                                alStringModifierFilter.add(alStringModifier.get(i));
                            }
                        }
                        adapter = new ArrayAdapter<>
                                (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringModifierFilter);
                        actModifier.setAdapter(adapter);

                    } else {
                        for (int j = 0; j < alStringModifierFilter.size(); j++) {
                            if (alStringModifierFilter.get(j).equals(modifierName)) {
                                isRemove = j;
                            }
                        }
                        if (isRemove != -1) {
                            alStringModifierFilter.remove(isRemove);
                        }
                    }
                }
                adapter = new ArrayAdapter<>
                        (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringModifierFilter);
                actModifier.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetArrayListAdapter(ArrayList<String> alString, ArrayList<String> alStringModifier) {
        alStringFilter = new ArrayList<>();
        alStringModifierFilter = new ArrayList<>();
        if (alString != null) {
            adapter = new ArrayAdapter<>
                    (getActivity(), android.R.layout.simple_spinner_dropdown_item, alString);
            actRemark.setAdapter(adapter);
        } else {
            adapter = new ArrayAdapter<>
                    (getActivity(), android.R.layout.simple_spinner_dropdown_item, alStringModifier);
            actModifier.setAdapter(adapter);
        }
    }

    private void AddNumber(String number) {
        textInputLayout.clearFocus();
        etQuantity.requestFocus();
        etQuantity.setText(number);
    }

    private void SetOrderItemTran() {
        objOrderItemTran = new ItemMaster();
        StringBuilder sb;
        try {
            if (Globals.alOrderItemTran.size() > 0) {
                for (int i = 0; i < Globals.alOrderItemTran.size(); i++) {
                    if (Globals.alOrderItemTran.get(i).getItemModifierIds() != null) {
                        if (objItemMaster.getItemMasterId() == Globals.alOrderItemTran.get(i).getItemMasterId() && (!Globals.alOrderItemTran.get(i).getItemModifierIds().equals("null") && !sbModifier.toString().equals("null"))) {
                            ArrayList<String> alStringOld = new ArrayList<>(Arrays.asList(Globals.alOrderItemTran.get(i).getItemModifierIds().split(",")));
                            ArrayList<String> alStringNew = new ArrayList<>(Arrays.asList(sbModifier.toString().split(",")));

                            if (sbModifier.toString().equals(Globals.alOrderItemTran.get(i).getItemModifierIds())) {
                                isDuplicate = true;
                                CheckDuplicateRemark(i);
                                break;
                            } else if (sbModifier.toString().length() == Globals.alOrderItemTran.get(i).getItemModifierIds().length()) {
                                sb = new StringBuilder();
                                for (int k = 0; k < alStringOld.size(); k++) {
                                    for (int j = 0; j < alStringNew.size(); j++) {
                                        if (alStringNew.get(j).equals(alStringOld.get(k))) {
                                            sb.append(alStringOld.get(k)).append(",");
                                        }
                                    }
                                }
                                if (sb.toString().equals(Globals.alOrderItemTran.get(i).getItemModifierIds())) {
                                    isDuplicate = true;
                                    CheckDuplicateRemark(i);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!isDuplicate) {
                    objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                    objOrderItemTran.setItemName(objItemMaster.getItemName());
                    objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                    objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                    objOrderItemTran.setRemark(actRemark.getText().toString());
                    if (!sbModifier.toString().equals("null")) {
                        objOrderItemTran.setItemModifierIds(sbModifier.toString());
                    }
                    objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);
                    if (alOrderItemModifierTran.size() != 0) {
                        totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice() + alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount();
                        objOrderItemTran.setTotalAmount(totalAmount);
                    }
                }
            } else {
                objOrderItemTran.setItemMasterId(objItemMaster.getItemMasterId());
                objOrderItemTran.setItemName(objItemMaster.getItemName());
                objOrderItemTran.setSellPrice(Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
                objOrderItemTran.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                objOrderItemTran.setRemark(actRemark.getText().toString());
                if (!sbModifier.toString().equals("null")) {
                    objOrderItemTran.setItemModifierIds(sbModifier.toString());
                }
                objOrderItemTran.setAlOrderItemModifierTran(alOrderItemModifierTran);
                if (alOrderItemModifierTran.size() != 0) {
                    totalAmount = Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice() + alOrderItemModifierTran.get(alOrderItemModifierTran.size() - 1).getTotalAmount();
                    objOrderItemTran.setTotalAmount(totalAmount);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void CheckDuplicateRemark(int i) {
        String[] strNewRemark, strOldRemark;
        StringBuilder sb = new StringBuilder();
        try {

            Globals.alOrderItemTran.get(i).setSellPrice(Globals.alOrderItemTran.get(i).getSellPrice() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
            Globals.alOrderItemTran.get(i).setTotalAmount(Globals.alOrderItemTran.get(i).getTotalAmount() + Integer.valueOf(etQuantity.getText().toString()) * objItemMaster.getSellPrice());
            Globals.alOrderItemTran.get(i).setQuantity(Globals.alOrderItemTran.get(i).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));

            if (!actRemark.getText().toString().isEmpty()) {
                if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(",")) {
                    strNewRemark = String.valueOf(actRemark.getText().subSequence(0, actRemark.length()) + " ").split(", ");
                } else if (actRemark.getText().subSequence(actRemark.length() - 1, actRemark.length()).toString().equals(" ")) {
                    strNewRemark = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
                } else {
                    strNewRemark = actRemark.getText().subSequence(0, actRemark.length()).toString().split(", ");
                }

                if (!Globals.alOrderItemTran.get(i).getRemark().isEmpty()) {
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
                                if (!newRemark.equals(oldRemark)) {
                                    sb.append(newRemark).append(",");
                                    break;
                                }
                            }
                        }
                    }
                    if (!listRemark.subSequence(listRemark.length() - 1, listRemark.length()).toString().equals(",")
                            && !listRemark.subSequence(listRemark.length() - 1, listRemark.length()).toString().equals(" ")) {
                        Globals.alOrderItemTran.get(i).setRemark(Globals.alOrderItemTran.get(i).getRemark() + ", " + sb.toString());
                    } else {
                        Globals.alOrderItemTran.get(i).setRemark(Globals.alOrderItemTran.get(i).getRemark() + sb.toString());
                    }

                } else {
                    Globals.alOrderItemTran.get(i).setRemark(actRemark.getText().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetOrderItemModifierTran() {
        ItemMaster objOrderItemModifier;
        alOrderItemModifierTran = new ArrayList<>();
        sbModifier = new StringBuilder();
        try {
            if (!actModifier.getText().toString().isEmpty()) {
                if (actModifier.getText().subSequence(actModifier.length() - 1, actModifier.length()).toString().equals(",")) {
                    modifierValue = String.valueOf(actModifier.getText().subSequence(0, actModifier.length()) + " ").split(", ");
                } else if (actModifier.getText().subSequence(actModifier.length() - 1, actModifier.length()).toString().equals(" ")) {
                    modifierValue = actModifier.getText().subSequence(0, actModifier.length()).toString().split(", ");
                }

                for (String strModifierValue : modifierValue) {
                    for (int j = 0; j < alItemMasterModifier.size(); j++) {
                        if (alItemMasterModifier.get(j).getItemName().equals(strModifierValue)) {
                            objOrderItemModifier = new ItemMaster();
                            objOrderItemModifier.setItemName(alItemMasterModifier.get(j).getItemName());
                            objOrderItemModifier.setItemModifierIds(String.valueOf(alItemMasterModifier.get(j).getItemMasterId()));
                            objOrderItemModifier.setMRP(alItemMasterModifier.get(j).getMRP());
                            totalModifierAmount = totalModifierAmount + alItemMasterModifier.get(j).getMRP();
                            objOrderItemModifier.setTotalAmount(totalModifierAmount);
                            sbModifier.append(alItemMasterModifier.get(j).getItemMasterId()).append(",");
                            alOrderItemModifierTran.add(objOrderItemModifier);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    public interface AddToCartListener {
        void AddToCart(boolean isAddToCart, ItemMaster objOrderItemTran);
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

            if (alString != null && alString.size() != 0) {
                alStringFilter = new ArrayList<>();

                actRemark.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                SetArrayListAdapter(alString, null);

                actRemark.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL) {
                            if (actRemark.getText().toString().isEmpty()) {
                                SetArrayListAdapter(alString, null);
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
                        //textInputLayout.clearFocus();
                        Globals.HideKeyBoard(getActivity(), textInputLayout);
                        UpdateArrayListAdapter((String) parent.getAdapter().getItem(position), null, false);
                    }
                });
            }
        }
    }

    class ModifierLoadingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            ItemJSONParser objItemMasterJSONParser = new ItemJSONParser();
            alItemMasterModifier = objItemMasterJSONParser.SelectAllItemMasterModifier();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            if (alItemMasterModifier != null && alItemMasterModifier.size() != 0) {
                alStringModifier = new ArrayList<>();
                for (int i = 0; i < alItemMasterModifier.size(); i++) {
                    alStringModifier.add(alItemMasterModifier.get(i).getItemName());
                }

                alStringModifierFilter = new ArrayList<>();

                actModifier.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                SetArrayListAdapter(null, alStringModifier);

                actModifier.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL) {
                            if (actModifier.getText().toString().isEmpty()) {
                                SetArrayListAdapter(null, alStringModifier);
                                isDeleted = false;
                            } else {
                                isDeleted = true;
                            }
                        }
                        return false;
                    }
                });

                actModifier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Globals.HideKeyBoard(getActivity(), textInputLayoutModifier);
                        UpdateArrayListAdapter(null, (String) parent.getAdapter().getItem(position), true);
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

