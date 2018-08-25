package com.example.my.mydiary.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.my.mydiary.R;

import static com.example.my.mydiary.main.handler.MainHandler.DELETE_ONE_DIARY;

/**
 * @author ggz
 * @date 18年8月15日
 */

public class MyDialogFragment extends DialogFragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_test, container, false);

        Bundle bundle = getArguments();
        String msg = bundle.getString("msg", "null");

        TextView textView = mView.findViewById(R.id.tv_dialog_msg);
        textView.setText(msg);

        return mView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String msg = bundle.getString("msg", "null");

        // 设置主题的构造方法
        // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.dialog_title)
                .setMessage(msg)
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return builder.create();
    }
}
