package com.trimitrasis.finosapps.Views.PosView.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.ContentProvider.CategoryProductProvider;
import com.trimitrasis.finosapps.Views.PosView.Adapter.ListCategoryAdapter;
import com.trimitrasis.finosapps.Views.PosView.Model.ListCategoryModel;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;


/**
 * Created by rahman on 20/06/2017.
 */

@EFragment(R.layout.fragment_column_category)
public class ListCategoryFragment extends Fragment {


    @ViewById
    RecyclerView listCategory;

    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<ListCategoryModel> listCategoryModels;

    @AfterViews
    void afterView(){
        listCategoryModels = new ArrayList<>();
        listCategoryModels.clear();
        initSetTableView();
        initList();
        initCategoryProduct();
    }


    private void initList(){
        ListCategoryAdapter adapter = new ListCategoryAdapter(getActivity(), listCategoryModels);
        listCategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void initSetTableView(){
        listCategory.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        listCategory.setLayoutManager(mLayoutManager);
    }


    private void initCategoryProduct(){

        String idCat = ""; String category = "";
        String URL       = "content://com.trimitrasis.finosapps.ContentProvider.CategoryProduct/categoryproduct";
        Uri uri_         = Uri.parse(URL);
        Cursor c_category   = getActivity().getContentResolver().query(uri_, null, null, null, "_id ASC");

        listCategoryModels.clear();
        listCategoryModels.add(new ListCategoryModel("all", "ALL"));
        if(c_category.moveToFirst()){
            do{
                idCat    = (!c_category.getString(c_category.getColumnIndex(CategoryProductProvider.KEY_ID_CATEGORY)).equals("") ? c_category.getString(c_category.getColumnIndex(CategoryProductProvider.KEY_ID_CATEGORY)) : "");
                category = (!c_category.getString(c_category.getColumnIndex(CategoryProductProvider.KEY_DESCRIPTION)).equals("") ? c_category.getString(c_category.getColumnIndex(CategoryProductProvider.KEY_DESCRIPTION)) : "");
                listCategoryModels.add(new ListCategoryModel(idCat, category));
            }while (c_category.moveToNext());

        }else{
            System.out.println("data tidak ditemukan!");
        }

        c_category.close();
        initList();

    }



}
