package com.gsl.demo.pendemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/12/5.
 */
public final class AlertManager {

    private static Toast currentToast;

    public static void show(Context context, String message){
        new AlertDialog.Builder(context)
                .setTitle(R.string.alert_title)
                .setMessage(message).show();
    }

    public static void show(Context context, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener){
        show(context, context.getString(R.string.alert_title), message, positiveListener,
                negativeListener);
    }

    public static AlertDialog show(Context context, String title, String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener){
        return new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton(R.string.alert_positive, positiveListener)
                .setNegativeButton(R.string.alert_neutral, negativeListener)
                .setMessage(message).show();
    }

    public static AlertDialog show(Context context, String title, String message, String positiveText, String negativeText, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener){
        return new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton(positiveText, positiveListener)
                .setNegativeButton(negativeText, negativeListener)
                .setMessage(message).show();
    }

    public static void show(Context context, String message, String positiveText, String negativeText, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.alert_title)
                .setPositiveButton(positiveText, positiveListener)
                .setNegativeButton(negativeText, negativeListener)
                .setMessage(message).show();
    }

    public static void show(Context context, String message, DialogInterface.OnClickListener onClickListener){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.alert_title)
                .setPositiveButton(R.string.alert_positive, onClickListener)
                .setMessage(message).show();
    }

    public static void toast(Context context, String message){
        toast(context, message, false);
    }

    public static void toast(Context context, String message, boolean showlong){
        int length;
        if(showlong) {
            length = Toast.LENGTH_LONG;
        } else {
            length = Toast.LENGTH_SHORT;
        }
        // 避免覆盖
        if(currentToast != null){
            currentToast.cancel();
            currentToast = null;
        }
        currentToast = Toast.makeText(context, message, length);
        currentToast.show();
    }


}

