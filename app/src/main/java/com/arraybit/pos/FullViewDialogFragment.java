package com.arraybit.pos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arraybit.modal.BusinessGalleryTran;
import com.squareup.picasso.Picasso;


@SuppressWarnings("ResourceType")
public class FullViewDialogFragment extends DialogFragment {


    BusinessGalleryTran objBusinessGalleryTran;

    public FullViewDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_view_dialog, container, false);

        final ImageView ivFullGalleryImage = (ImageView) view.findViewById(R.id.ivFullGalleryImage);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.getParcelable("BusinessGallery") != null) {
            objBusinessGalleryTran = bundle.getParcelable("BusinessGallery");
            Picasso.with(getActivity()).load(objBusinessGalleryTran.getXL_ImagePhysicalName()).into(ivFullGalleryImage);
        }

        ivFullGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

}
