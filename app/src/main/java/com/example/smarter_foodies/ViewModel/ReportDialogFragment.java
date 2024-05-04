package com.example.smarter_foodies.ViewModel;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.smarter_foodies.Model.CRUD_RealTimeDatabaseData;
import com.example.smarter_foodies.Model.report;
import com.example.smarter_foodies.R;

public class ReportDialogFragment extends DialogFragment {
    private CRUD_RealTimeDatabaseData CRUD;
    private String ref, title;
    private Context mContext;

    public ReportDialogFragment(Context context,String ref, String title) {
        this.ref = ref;
        this.title = title;
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_report_layout, container, false);

        System.out.println("im in onCreateView");
        // Initialize your CRUD instance
        CRUD = new CRUD_RealTimeDatabaseData();

        CheckBox boxInappropriate = view.findViewById(R.id.checkBoxOption1);
        CheckBox boxOffensive = view.findViewById(R.id.checkBoxOption2);
        CheckBox boxVerbal = view.findViewById(R.id.checkBoxOption3);
        CheckBox boxHate = view.findViewById(R.id.checkBoxOption4);

        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hate = boxHate.isChecked() ? 1 : 0;
                int inappropriate = boxInappropriate.isChecked() ? 1 : 0;
                int offensive = boxOffensive.isChecked() ? 1 : 0;
                int verbal = boxVerbal.isChecked() ? 1 : 0;

                if (hate + inappropriate + offensive + verbal > 0) {
                    CRUD.sendReport(mContext, hate, inappropriate, offensive, verbal, title, ref);
                    dismiss();
                } else {
                    // Show a message to the user indicating they need to select at least one option
                    // For example, you can show a toast message
                    Toast.makeText(getContext(), "Please select at least one option.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}

