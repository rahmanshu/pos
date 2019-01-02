package com.trimitrasis.finosapps.Views.PosView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rahman on 04/04/2017.
 */

@EActivity(R.layout.layout_detail_order)
public class DetailOrderActivity extends AppCompatActivity {


    @ViewById
    Toolbar toolbar;

    @ViewById
    EditText textHargaBarang, textQtyBarang, textDiskon;

    @ViewById
    Button buttonBatal, buttonHapus, btnSimpan;

    @Extra
    RingkasanOrderModel ringkasanOrderModel;

    int diskon;

    @AfterViews
    void afterView(){
        customView();
        setViewDetail();
        initTabbar();
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
    }

    private void customView(){
        TextView toolbarView = (TextView) toolbar.findViewById(R.id.headerText);
        toolbarView.setText("Detail Order");
    }

    private void setViewDetail(){
        textHargaBarang.setText(String.valueOf(ringkasanOrderModel.getHargaJual()));
        textQtyBarang.setText(String.valueOf(ringkasanOrderModel.getQty()));
        textDiskon.setText(String.valueOf(ringkasanOrderModel.getDiskon()));
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){

            }
        }
    };

    @Click
    void buttonBatal(){
        finish();
    }


    @Click
    void btnSimpan(){

        double qtyHapusBarang   = Double.parseDouble(textQtyBarang.getText().toString());
        double hargaBarang      = Double.parseDouble(textHargaBarang.getText().toString());
        double diskon           = Double.parseDouble(textDiskon.getText().toString());

        if(diskon == 0.0){
            diskon = 0.0;
        }else{
            diskon = Double.parseDouble(textDiskon.getText().toString());
        }

        if(diskon != 0.0 && diskon > 0){

        }else{

        }

        CreateOrderActivity.updateShopingQty();
        finish();
    }

}
