package it.gfagnani.disneyquiz;

import android.content.Context;

import com.shashank.sony.fancytoastlib.FancyToast;

public class Utilities {
    protected static void showToast (Context context, String message, int toastType) {
        FancyToast.makeText(context,
                message,
                FancyToast.LENGTH_LONG,
                toastType,
                false).show();
    }


}
