package com.example.jdamusica;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class GuiaAñadirCancionDialog extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_info, null);
        CheckBox mChecbox = mView.findViewById(R.id.checkBox);

        builder.setView(mView).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();

        mChecbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    Log.i("checkBox", "1");
                    storeDialogStatus(true);
                }else{
                    Log.i("checkBox", "2");
                    storeDialogStatus(false);
                }
            }
        });

        return mDialog;
    }

    protected void storeDialogStatus(boolean isChecked){
        Log.i("status", String.valueOf(isChecked));
        SharedPreferences mSharedPreferences = getContext().getSharedPreferences("CheckItem",Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.commit();
    }

}
