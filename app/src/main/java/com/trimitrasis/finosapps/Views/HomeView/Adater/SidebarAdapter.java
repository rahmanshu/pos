package com.trimitrasis.finosapps.Views.HomeView.Adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.HomeView.SidebarObj;
import com.trimitrasis.finosapps.Views.UtilView.FontUtils;

import java.util.ArrayList;

/**
 * Created by rahman on 03/03/2017.
 */

public class SidebarAdapter extends ArrayAdapter<SidebarObj> {

    ArrayList<SidebarObj> sidebarObjs = new ArrayList<>();
    Context context;
    LayoutInflater mInflater;


    public SidebarAdapter(Context context, ArrayList<SidebarObj> objects) {
        super(context, R.layout.layout_sidebar_content, objects);
        sidebarObjs = objects;
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return sidebarObjs.size();
    }

    @Override
    public SidebarObj getItem(int position){
        return sidebarObjs.get(position);
    }

    private class ViewHolder{
        ImageView iconView;
        TextView labelView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = mInflater.inflate(R.layout.layout_sidebar_content,null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.iconView = (ImageView)view.findViewById(R.id.iconSidebarMenu);
            viewHolder.labelView = (TextView)view.findViewById(R.id.labelMenuSidebar);
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder)view.getTag();
        viewHolder.iconView.setImageDrawable(context.getResources().getDrawable(sidebarObjs.get(position).getResIcon()));
        viewHolder.labelView.setText(sidebarObjs.get(position).getLabel());
        viewHolder.labelView.setTypeface(FontUtils.getHelvetica_Neue_LT(context));

        return view;
    }
}
