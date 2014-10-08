package it.moondroid.chatbot;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import it.moondroid.chatbot.R;

/**
 * Created by Marco on 04/10/2014.
 */
public class BrainLoggerDialog extends DialogFragment {

    private ScrollView myScrollView;
    private TextView tv;
    private boolean shown = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            myScrollView = (ScrollView)inflater.inflate(R.layout.dialog_brain_log, null, false);

            tv = (TextView) myScrollView
                    .findViewById(R.id.textViewWithScroll);

            if(savedInstanceState!=null){
                String text = savedInstanceState.getString("log");
                tv.setText(text!=null? text : "");
            }else {
                loadLog();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setView(myScrollView)
                    .setTitle("Loading Brain")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @TargetApi(11)
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }

                    });


            setCancelable(false);
            // Create the AlertDialog object and return it
            return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
        int dialogHeight = (int) getResources().getDimension(R.dimen.dialog_logger_height);

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("log", tv.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        //stop the dialog from being dismissed on rotation, due to a bug with the compatibility library
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (shown) return;

        super.show(manager, tag);
        shown = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        shown = false;
        super.onDismiss(dialog);
    }

    public void addLine(String text) {
        if (tv != null) {
            tv.append(text+"\n");
            myScrollView.fullScroll(View.FOCUS_DOWN);
        }
    }

    private void loadLog(){
        ArrayList<String> logs = (ArrayList<String>) BrainLogger.getInstance().getLogs().clone();
        for(String log : logs){
            addLine(log);
        }
    }
}
