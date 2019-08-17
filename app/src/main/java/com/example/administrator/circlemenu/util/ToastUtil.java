package com.example.administrator.circlemenu.util;

import android.content.Context;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 快速更新弹出吐司
 */

public class ToastUtil {
    static Toast toast = null;
    private static Field mTN;
    private static Object mObj;
    private static Method showMethod;
    private static Method hideMethod;

    public static void show(Context ctx, String msg) {
        if (toast == null) {
            toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }


}
