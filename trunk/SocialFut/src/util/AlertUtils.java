package util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class AlertUtils
{

    private Context context;

    public AlertUtils(Context context)
    {
        this.context = context;
    }

    public AlertDialog getAlertDialog(String title, String message, OnClickListener positiveButton,
            OnClickListener negativeButton)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (title != null)
        {
            builder.setTitle(title);
        }
        if (message != null)
        {
            builder.setMessage(message);
        }

        if (negativeButton == null)
        {
            builder.setPositiveButton(Constants.OK, positiveButton);
        }
        else
        {
            builder.setPositiveButton(Constants.YES, positiveButton);
            builder.setNegativeButton(Constants.NO, negativeButton);
        }

        return builder.create();
    }

}
