package tz.co.fishhappy.app.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Simon on 01-May-17.
 */

public class Utils {

    /**
     * Display progress dialog box
     *
     * @param progress - the instance created by a view
     * */
    public static void showProgressDialog(ProgressDialog progress, String title, String message){
        if(progress != null){
            if(progress.isShowing()) progress.hide();//Hide if is already displaying

            progress.setIndeterminate(true);
            progress.setTitle(title);
            progress.setMessage(message);
            progress.show();
        }
    }

    /**
     * Hide progress dialog box
     *
     * @param progress - the instance created by a view
     * */
    public static void hideProgressDialog(ProgressDialog progress){
        if(progress != null){
            progress.hide();
        }
    }

    /**
     * Format to money
     *
     * @param data
     * */
    public static String formatToMoney(String data){
        try {
            data = data.replace(",", "");
            double money = Long.valueOf(data);
            return NumberFormat.getNumberInstance(Locale.US).format(money);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * Display toast message
     *
     * @param context - application context
     * @param message - the message to be displayed
     * */
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
