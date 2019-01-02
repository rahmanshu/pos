package com.trimitrasis.finosapps.Views.CustomView;
import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.trimitrasis.finosapps.R;

/**
 * Created by rahman on 15/03/2017.
 */

public class ViewEditextNama extends LinearLayout{

    EditText editNama;
    LinearLayout spinAlamat;

    public ViewEditextNama(Context context, String field, boolean visible, boolean read_only, boolean mandatory, String type){
        super(context);
        init(0, field, visible, read_only, mandatory, type);
    }

    private void init(final int index, String field, boolean visible, boolean read_only, boolean mandatory, String type){

        inflate(getContext(), R.layout.layout_item_editext_nama, this);
        this.editNama   = (EditText) findViewById(R.id.editNama);
        this.spinAlamat = (LinearLayout) findViewById(R.id.spinAlamat);

        if(type.equals("edittext")){

            if(!field.equals("")){
                this.editNama.setHint(field);
            }else{
                this.editNama.setHint("");
            }

            if(visible == true){
                this.editNama.setVisibility(VISIBLE);
            }else{
                this.editNama.setVisibility(GONE);
            }

            if(read_only == true){
                this.editNama.setEnabled(true);
            }else{
                this.editNama.setEnabled(false);
            }

        }else if(type.equals("combo")){

            if(visible == true){
                this.spinAlamat.setVisibility(VISIBLE);
            }else{
                this.spinAlamat.setVisibility(GONE);
            }

            if(read_only == true){
                this.spinAlamat.setEnabled(true);
            }else{
                this.spinAlamat.setEnabled(false);
            }
        }

    }


    public EditText getEditNama(){
        return editNama;
    }

    public void setEditNama(String editNama){
        this.editNama.setText(editNama);
    }

}
