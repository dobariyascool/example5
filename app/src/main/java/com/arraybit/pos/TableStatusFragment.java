package com.arraybit.pos;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.arraybit.global.Globals;
import com.arraybit.modal.TableMaster;
import com.arraybit.parser.TableJSONParser;
import com.rey.material.widget.Button;

@SuppressWarnings("unchecked")
@SuppressLint("ValidFragment")
public class TableStatusFragment extends DialogFragment implements View.OnClickListener {

    Button btnVacant, btnOccupy, btnBlock, btnDirty;
    TableMaster objTableMaster, objTable;
    UpdateTableStatusListener objUpdateTableStatusListener;

    public TableStatusFragment() {
        // Required empty public constructor
    }


    public TableStatusFragment(TableMaster objTableMaster) {
        // Required empty public constructor
        this.objTableMaster = objTableMaster;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_status, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        btnVacant = (Button) view.findViewById(R.id.btnVacant);
        btnOccupy = (Button) view.findViewById(R.id.btnOccupy);
        btnBlock = (Button) view.findViewById(R.id.btnBlock);
        btnDirty = (Button) view.findViewById(R.id.btnDirty);

        SetButtonVisibility();

        btnVacant.setOnClickListener(this);
        btnOccupy.setOnClickListener(this);
        btnBlock.setOnClickListener(this);
        btnDirty.setOnClickListener(this);

        objUpdateTableStatusListener = (UpdateTableStatusListener) getTargetFragment();

        return view;
    }

    private void SetButtonVisibility() {
        if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Block.toString())) {
            btnBlock.setVisibility(View.GONE);
            btnDirty.setVisibility(View.VISIBLE);
        } else if (objTableMaster.getTableStatus().equals(Globals.TableStatus.Dirty.toString())) {
            btnBlock.setVisibility(View.VISIBLE);
            btnDirty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnVacant) {

            objTable = new TableMaster();
            objTable.setTableMasterId(objTableMaster.getTableMasterId());
            objTable.setTableStatus(Globals.TableStatus.Vacant.toString());
            objTable.setStatusColor(Globals.TableStatusColor.Vacant.getValue());
            objTable.setlinktoTableStatusMasterId((short) Globals.TableStatus.Vacant.getValue());

            new UpdateTableStatusLoadingTask().execute();

        } else if (v.getId() == R.id.btnOccupy) {

            objTable = new TableMaster();
            objTable.setTableMasterId(objTableMaster.getTableMasterId());
            objTable.setTableStatus(Globals.TableStatus.Occupied.toString());
            objTable.setStatusColor(Globals.TableStatusColor.Occupied.getValue());
            objTable.setlinktoTableStatusMasterId((short) Globals.TableStatus.Occupied.getValue());

            new UpdateTableStatusLoadingTask().execute();
        } else if (v.getId() == R.id.btnBlock) {

            objTable = new TableMaster();
            objTable.setTableMasterId(objTableMaster.getTableMasterId());
            objTable.setTableStatus(Globals.TableStatus.Block.toString());
            objTable.setStatusColor(Globals.TableStatusColor.Block.getValue());
            objTable.setlinktoTableStatusMasterId((short) Globals.TableStatus.Block.getValue());

            new UpdateTableStatusLoadingTask().execute();
        } else if (v.getId() == R.id.btnDirty) {

            objTable = new TableMaster();
            objTable.setTableMasterId(objTableMaster.getTableMasterId());
            objTable.setTableStatus(Globals.TableStatus.Dirty.toString());
            objTable.setStatusColor(Globals.TableStatusColor.Dirty.getValue());
            objTable.setlinktoTableStatusMasterId((short) Globals.TableStatus.Dirty.getValue());

            new UpdateTableStatusLoadingTask().execute();
        }

    }

    public interface UpdateTableStatusListener {
        void UpdateTableStatus(boolean flag, TableMaster objTableMaster);
    }

    class UpdateTableStatusLoadingTask extends AsyncTask {

        ProgressDialog progressDialog;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.MsgLoading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            TableJSONParser objTableJSONParser = new TableJSONParser();
            status = objTableJSONParser.UpdateTableStatus(objTable);

            return status;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (status.equals("-1")) {
                objUpdateTableStatusListener.UpdateTableStatus(false, objTable);
            } else if (status.equals("0")) {
                objUpdateTableStatusListener.UpdateTableStatus(true, objTable);
            }
            progressDialog.dismiss();
            getDialog().dismiss();
        }
    }
}
