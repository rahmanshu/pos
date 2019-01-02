package com.trimitrasis.finosapps.Views.HomeView;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.HomeView.Adater.SliderAdapterPos;
import com.trimitrasis.finosapps.Views.UtilView.Constants;
import com.viewpagerindicator.CirclePageIndicator;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rahman on 03/05/2017.
 */

@EFragment(R.layout.fragment_home_layout)
public class HomeFragment extends Fragment {

    static int i = 0;
    int a = 0;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();


    @ViewById
    RelativeLayout pagerBannerLayout;

    @ViewById
    ViewPager menuViewPager;

    @ViewById
    CirclePageIndicator circleMenuPageIndicator;

    @ViewById
    View mainMenuLayout;

    @ViewById
    View saldoHeader;

    @ViewById
    View ads_Layout;

    TextView saldoHeaderView;
    SliderAdapterPos adapter;
    static MenuButtonListener listener;

    @AfterViews
    void afterView(){

        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 2000, 3000);

        initSliderView();
        bondHomeButton();
        saldoHeaderView = (TextView) saldoHeader.findViewById(R.id.saldoText);
        ((TextView) ads_Layout.findViewById(R.id.runningText)).setSelected(true);
        setSizeImage();
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        listener = (MenuButtonListener)activity;
    }

    public void initializeTimerTask(){

        timerTask = new TimerTask(){
            @Override
            public void run(){
                handler.post(new Runnable(){

                    @Override
                    public void run(){
                        //TODO Auto-generated method stub
                        i++;
                        menuViewPager.setCurrentItem(i % adapter.getCount());
                    }
                });

            }
        };

    }


    private void bondHomeButton(){
        mainMenuLayout.findViewById(R.id.menuStartOfDay).setOnClickListener(onClickListener);
        mainMenuLayout.findViewById(R.id.menuStartOfShift).setOnClickListener(onClickListener);
        mainMenuLayout.findViewById(R.id.menuEndOfShift).setOnClickListener(onClickListener);
        mainMenuLayout.findViewById(R.id.menuEndOfDay).setOnClickListener(onClickListener);
        mainMenuLayout.findViewById(R.id.menuPointOfSales).setOnClickListener(onClickListener);
        mainMenuLayout.findViewById(R.id.menuHistorySales).setOnClickListener(onClickListener);
        mainMenuLayout.findViewById(R.id.menuHoldSales).setOnClickListener(onClickListener);
        mainMenuLayout.findViewById(R.id.menuSinkron).setOnClickListener(onClickListener);
        mainMenuLayout.findViewById(R.id.menuPengaturan).setOnClickListener(onClickListener);
    }

    public interface MenuButtonListener{
        void onClickHome(int idMenu);
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.menuStartOfDay:
                    listener.onClickHome(Constants.START_OF_DAY_MENU);
                    break;
                case R.id.menuStartOfShift:
                    listener.onClickHome(Constants.START_OF_SHIFT_MENU);
                    break;
                case R.id.menuEndOfShift:
                    listener.onClickHome(Constants.END_OF_SHIFT_MENU);
                    break;
                case R.id.menuEndOfDay:
                    listener.onClickHome(Constants.END_OF_DAY_MENU);
                    break;
                case R.id.menuPointOfSales:
                    listener.onClickHome(Constants.POS_MENU);
                    break;
                case R.id.menuHistorySales:
                    listener.onClickHome(Constants.SYNC_TRANS_MENU);
                    break;
                case R.id.menuHoldSales:
                    listener.onClickHome(Constants.HOLD_SALES_MENU);
                    break;
                case R.id.menuSinkron:
                    listener.onClickHome(Constants.SINKRON_MENU);
                    break;
                case R.id.menuPengaturan:
                    listener.onClickHome(Constants.PENGATURAN_MENU);
                    break;
            }
        }
    };

    private void initSliderView(){

        int orientation=this.getResources().getConfiguration().orientation;
        if(orientation==Configuration.ORIENTATION_PORTRAIT){
            adapter = new SliderAdapterPos(getChildFragmentManager(), getContentSliderFragmentsPortrain());
        }else{
            adapter = new SliderAdapterPos(getChildFragmentManager(), getContentSliderFragments());
        }

        menuViewPager.setAdapter(adapter);
        circleMenuPageIndicator.setViewPager(menuViewPager);
    }



    private ArrayList<ContentSliderAdapter> getContentSliderFragmentsPortrain(){
        ArrayList<ContentSliderAdapter> contentSliderFragments = new ArrayList<>();
        contentSliderFragments.add(ContentSliderAdapter_.builder().resImage(R.mipmap.img_banner_finos_0).build());
        contentSliderFragments.add(ContentSliderAdapter_.builder().resImage(R.mipmap.img_banner_finos_1).build());
        return contentSliderFragments;
    }


    private ArrayList<ContentSliderAdapter> getContentSliderFragments(){
        ArrayList<ContentSliderAdapter> contentSliderFragments = new ArrayList<>();
        contentSliderFragments.add(ContentSliderAdapter_.builder().resImage(R.mipmap.img_banner_finos_0).build());
        contentSliderFragments.add(ContentSliderAdapter_.builder().resImage(R.mipmap.img_banner_finos_1).build());
        return contentSliderFragments;
    }


    private void setSizeImage(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height     = displaymetrics.heightPixels;
        int width      = displaymetrics.widthPixels;
        int heightDp   = (int) (height / Resources.getSystem().getDisplayMetrics().density);
        double resultSize = Double.parseDouble(String.valueOf(heightDp)) / 2.3;
        int dimensionInPixel = (int) resultSize;
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());

        int orientation=this.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            pagerBannerLayout.getLayoutParams().width  = ViewGroup.LayoutParams.MATCH_PARENT;
            pagerBannerLayout.getLayoutParams().height = dimensionInDp;
            System.out.println("height  : " + height + "width : " + width);
        }

    }

}
