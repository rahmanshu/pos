package com.trimitrasis.finosapps.Views.UtilView;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by rahman on 03/03/2017.
 */

public class FontUtils {

    public static final String PTC55F_FONT_URL = "fonts/PTC55F.ttf";
    public static final String PTC75F_FONT_URL = "fonts/PTC75F.ttf";
    public static final String PTN57F_FONT_URL = "fonts/PTN57F.ttf";
    public static final String PTN77F_FONT_URL = "fonts/PTN77F.ttf";
    public static final String PTS55F_FONT_URL = "fonts/PTS55F.ttf";
    public static final String PTS56F_FONT_URL = "fonts/PTS56F.ttf";
    public static final String PTS75F_FONT_URL = "fonts/PTS75F.ttf";
    public static final String PTS76F_FONT_URL = "fonts/PTS76F.ttf";
    public static final String Helvetica_Neue_LT_Std_Light_FONT_URL = "fonts/HelveticaNeueLTStd-Lt.otf";
    public static final String Helvetica_Neue_LT_Std_Ltcno_FONT_URL = "fonts/HelveticaNeueLTStd-LtCnO.otf";


    public static Typeface getHelvetica_Neue_LT_cno(Context context) {

        return Typeface.createFromAsset(context.getAssets(), Helvetica_Neue_LT_Std_Ltcno_FONT_URL);

    }

    public static Typeface getHelvetica_Neue_LT(Context context) {

        return Typeface.createFromAsset(context.getAssets(), Helvetica_Neue_LT_Std_Light_FONT_URL);

    }

    public static Typeface getPTC55FFont(Context context){
        return Typeface.createFromAsset(context.getAssets(), PTC55F_FONT_URL);
    }

    public static Typeface getPTC75FFont(Context context){
        return Typeface.createFromAsset(context.getAssets(), PTC75F_FONT_URL);
    }

    public static Typeface getPTN57FFont(Context context){
        return Typeface.createFromAsset(context.getAssets(), PTN57F_FONT_URL);
    }

    public static Typeface getPTN77FFont(Context context){
        return Typeface.createFromAsset(context.getAssets(), PTN77F_FONT_URL);
    }

    // font header toolbar , button, body
    public static Typeface getFontHeaderToolbar(Context context){
        return Typeface.createFromAsset(context.getAssets(), PTS55F_FONT_URL);
    }

    public static Typeface getPTS56FFont(Context context){
        return Typeface.createFromAsset(context.getAssets(), PTS56F_FONT_URL);
    }

    //font number, title label
    public static Typeface getFontTitleLabel(Context context){
        return Typeface.createFromAsset(context.getAssets(), PTS75F_FONT_URL);
    }

    public static Typeface getPTS76FFont(Context context){
        return Typeface.createFromAsset(context.getAssets(), PTS76F_FONT_URL);
    }

}
