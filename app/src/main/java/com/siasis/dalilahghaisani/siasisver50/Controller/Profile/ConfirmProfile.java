package com.siasis.dalilahghaisani.siasisver50.Controller.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.siasis.dalilahghaisani.siasisver50.R;

/**
 * Created by ASUS on 4/5/2015.
 */
public class ConfirmProfile extends DialogFragment {
    public interface ConfirmProfileListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ConfirmProfileListener mListener;
    Context mContext;

    public ConfirmProfile(){
        mContext=getActivity();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ConfirmProfileListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_confirm_profile)
                .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(ConfirmProfile.this);
                    }
                })
                .setNegativeButton(R.string.disagree, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(ConfirmProfile.this);
                    }
                });
        return builder.create();
    }

    public static ConfirmProfile newInstance(){
        ConfirmProfile f = new ConfirmProfile();
        return f;
    }
}
