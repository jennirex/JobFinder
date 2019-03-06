package jennirex.com.jobfinder.Helpers;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Tupelo on 3/6/2019.
 */

public class ProgressDialogHelper {
    public static ProgressDialogHelper progressDialogHelper = null;
    private ProgressDialog mDialog;

    public static ProgressDialogHelper getInstance() {
        if (progressDialogHelper == null) {
            progressDialogHelper = new ProgressDialogHelper();
        }
        return progressDialogHelper;
    }

    public void showProgressDialog(Context context, String message, boolean cancelable){
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(message);
        mDialog.setCancelable(cancelable);
        mDialog.show();
    }

    public void hideProgressDialog(){
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
