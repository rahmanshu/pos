package com.trimitrasis.finosapps.Views.SettingView;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import com.trimitrasis.finosapps.ActivityResultEventDapur;
import com.trimitrasis.finosapps.ActivityResultEventFootnote;
import com.trimitrasis.finosapps.ActivityResultEventStruk;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.SettingView.Fragment.SettingFootnoteFragment_;
import com.trimitrasis.finosapps.Views.SettingView.Fragment.SettingPrinterDapurFragment_;
import com.trimitrasis.finosapps.Views.SettingView.Fragment.SettingPrinterStrukFragment_;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import de.greenrobot.event.EventBus;

/**
 * Created by rahman on 19/07/2017.
 */

@EActivity(R.layout.layout_pengaturan_activity)
public class SettingActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    ViewPager contentSettingPrint;

    FragmentAdapter fragmentAdapter;

    @ViewById
    TextView buttonTextPrintStruk, buttonTextPrintDapur, buttonTextFootnote;

    @AfterViews
    void afterView(){

        String APP_PATH_SD_CARD = "/imageFinos/";
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD;
        System.out.println("path : " + fullPath);

        initTabbar();
        customView();

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        contentSettingPrint.setAdapter(fragmentAdapter);

    }



    private void initTabbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
    }


    @OptionsItem(android.R.id.home)
    void homeClick(){
        onBackPressed();
        finish();
    }

    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("Setting");
        buttonTextPrintStruk.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonTextPrintDapur.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        buttonTextFootnote.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
        toolbarView.setTypeface(FontUtils.getHelvetica_Neue_LT(this));
    }



    class FragmentAdapter extends FragmentPagerAdapter{

        public FragmentAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            switch (position){
                case 0:
                    return SettingPrinterStrukFragment_.builder().build();
                case 1:
                    return SettingPrinterDapurFragment_.builder().build();
                case 2:
                    return SettingFootnoteFragment_.builder().build();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


    @Click
    void buttonTextPrintStruk(){
        contentSettingPrint.setCurrentItem(0);
        buttonTextPrintDapur.setBackgroundResource(R.color.blue_salesforce);
        buttonTextPrintStruk.setBackgroundResource(R.color.hover_setting);
        buttonTextFootnote.setBackgroundResource(R.color.blue_salesforce);
    }


    @Click
    void buttonTextPrintDapur(){
        contentSettingPrint.setCurrentItem(1);
        buttonTextPrintStruk.setBackgroundResource(R.color.blue_salesforce);
        buttonTextPrintDapur.setBackgroundResource(R.color.hover_setting);
        buttonTextFootnote.setBackgroundResource(R.color.blue_salesforce);
    }

    @Click
    void buttonTextFootnote(){
        contentSettingPrint.setCurrentItem(2);
        buttonTextFootnote.setBackgroundResource(R.color.hover_setting);
        buttonTextPrintDapur.setBackgroundResource(R.color.blue_salesforce);
        buttonTextPrintStruk.setBackgroundResource(R.color.blue_salesforce);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        EventBus.getDefault().post(new ActivityResultEventDapur(requestCode, resultCode, data));
        EventBus.getDefault().post(new ActivityResultEventStruk(requestCode, resultCode, data));
    }







}
