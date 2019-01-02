package com.trimitrasis.finosapps.Views.HomeView;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.trimitrasis.finosapps.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rahman on 03/05/2017.
 */

@EFragment(R.layout.fragment_slider_layout)
public class ContentSliderAdapter extends Fragment {

    @FragmentArg
    int resImage;

    @ViewById
    ImageView contentSlider;

    @AfterViews
    void afterViews(){
        setSizeImage();
    }

    private void setSizeImage(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height           = displaymetrics.heightPixels;
        int heightDp         = (int) (height / Resources.getSystem().getDisplayMetrics().density);
        double resultSize    = Double.parseDouble(String.valueOf(heightDp)) / 2.4;
        int dimensionInPixel = (int) resultSize;
        int dimensionInDp    = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());

        int orientation = this.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            contentSlider.getLayoutParams().width  = ViewGroup.LayoutParams.MATCH_PARENT;
            contentSlider.getLayoutParams().height = dimensionInDp;
        }

        contentSlider.setImageDrawable(getResources().getDrawable(resImage));
    }


}
