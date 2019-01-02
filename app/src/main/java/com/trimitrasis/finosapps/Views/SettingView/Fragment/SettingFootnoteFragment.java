package com.trimitrasis.finosapps.Views.SettingView.Fragment;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.StrukNoteProvider;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rahman on 10/23/2017.
 */

@EFragment(R.layout.fragment_setting_footnote)
public class SettingFootnoteFragment extends Fragment{


    @ViewById
    Button buttonAddFootnote;

    @ViewById
    TextView textFootnote;

    @ViewById
    TextView labelFootnote;

    Dialog dialogFootnote;
    LayoutInflater inflater;
    EditText note; Button buttonSave; Button buttonCancel;

    @AfterViews
    void afterView(){
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView();
        getFootnote();
    }

    private void customView(){
        labelFootnote.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        textFootnote.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        buttonAddFootnote.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
    }



    @Click
    void buttonAddFootnote(){
        showDialogFootnote();
    }


    private void insertFootNote(String footnoteString){
        int emptyTableBom = getActivity().getContentResolver().delete(StrukNoteProvider.CONTENT_URI, null, null);
        ContentValues values = new ContentValues();
        values.put(StrukNoteProvider.KEY_STRUK_NOTE, footnoteString);
        Uri uri = getActivity().getContentResolver().insert(StrukNoteProvider.CONTENT_URI, values);
    }


    private void getFootnote(){

        String footnote = "";
        String URL = "content://com.trimitrasis.finosapps.Views.ContentProvider.StrukNoteProvider/struknote";
        Uri uri = Uri.parse(URL);
        Cursor c_login = getActivity().getContentResolver().query(uri, null, null, null, "_id");

        if(c_login.moveToFirst()){
            do{
                footnote = (c_login.getString(c_login.getColumnIndex(StrukNoteProvider.KEY_STRUK_NOTE)) != null) ? c_login.getString(c_login.getColumnIndex(StrukNoteProvider.KEY_STRUK_NOTE)) : "";
            } while (c_login.moveToNext());
            c_login.close();

            textFootnote.setVisibility(View.VISIBLE);
            textFootnote.setText(footnote);
        }else{
            textFootnote.setVisibility(View.GONE);
        }

    }



    private void showDialogFootnote(){
        dialogFootnote = new Dialog(getActivity());
        View view = inflater.inflate(R.layout.popup_add_footnote, null);
        declareLayout(view);
        dialogFootnote.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFootnote.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        dialogFootnote.setContentView(view);
        dialogFootnote.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogFootnote.show();
    }


    private void declareLayout(View view){
        note = (EditText) view.findViewById(R.id.note);
        buttonSave = (Button) view.findViewById(R.id.buttonSave);
        buttonCancel = (Button) view.findViewById(R.id.buttonCancel);

        if(!textFootnote.getText().toString().equals("")){
            note.setText(textFootnote.getText().toString());
        }

        buttonSave.setOnClickListener(onClickListener);
        buttonCancel.setOnClickListener(onClickListener);
        buttonSave.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
        buttonCancel.setTypeface(FontUtils.getHelvetica_Neue_LT(getActivity()));
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.buttonSave:{
                    String footnoteString = note.getText().toString();
                    insertFootNote(footnoteString);
                    getFootnote();
                    dialogFootnote.dismiss();
                }break;
                case R.id.buttonCancel:{
                    dialogFootnote.dismiss();
                }break;

            }
        }
    };



}
