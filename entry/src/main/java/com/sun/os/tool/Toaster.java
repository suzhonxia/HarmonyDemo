package com.sun.os.tool;

import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

public class Toaster {
    public static void showToast(Context context, String msg) {
        ToastDialog toastDialog = new ToastDialog(context);
        toastDialog.setText(msg).setAlignment(LayoutAlignment.CENTER).show();
    }
}
