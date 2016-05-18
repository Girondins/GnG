package com.grabandgo.gng.gng;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<DrawerCategory> drawerCategory = new ArrayList<DrawerCategory>();
    private ArrayList<ArrayList<DrawerSubCategory>> drawersubCategory = new ArrayList<ArrayList<DrawerSubCategory>>();
    private ArrayList<Integer> drawerSubCategoryCount = new ArrayList<Integer>();
    private ExpandableListView expandableListView;
    private int lastExpandedGroupPosition;

    private DrawerSubCategory singleChild = new DrawerSubCategory();

    public ExpandableListViewAdapter(Context context, ArrayList<DrawerCategory> drawerCategory, ArrayList<ArrayList<DrawerSubCategory>> subCategory, ArrayList<Integer> drawerSubCategoryCount, ExpandableListView expandableListView) {
        layoutInflater = LayoutInflater.from(context);
        this.drawerCategory = drawerCategory;
        this.drawersubCategory = subCategory;
        this.drawerSubCategoryCount = drawerSubCategoryCount;
        this.expandableListView = expandableListView;
        lastExpandedGroupPosition = 0;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

        if (groupPosition != lastExpandedGroupPosition) {
            expandableListView.collapseGroup(lastExpandedGroupPosition);
        }

        super.onGroupExpanded(groupPosition);
        lastExpandedGroupPosition = groupPosition;
    }

    @Override
    public int getGroupCount() {
        return drawerCategory.size();
    }

    @Override
    public int getChildrenCount(int index) {
        return (drawerSubCategoryCount.get(index));
    }

    @Override
    public Object getGroup(int index) {
        return drawerCategory.get(index).getCategory();
    }

    @Override
    public DrawerSubCategory getChild(int sub, int subIndex) {
        ArrayList<DrawerSubCategory> tempList = new ArrayList<DrawerSubCategory>();
        tempList = drawersubCategory.get(sub);
        return tempList.get(subIndex);
    }

    @Override
    public long getGroupId(int index) {
        return index;
    }

    @Override
    public long getChildId(int i, int j) {
        return j;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.drawer_category, viewGroup, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.category_name);
        textView.setText(getGroup(i).toString());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.drawer_subcategory, viewGroup, false);
        }

        singleChild = getChild(i, i1);
        TextView childSubCategoryName = (TextView) view.findViewById(R.id.subcategory_name);
        childSubCategoryName.setText(singleChild.getSubCategory());

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.subCat_checkbox);
        checkBox.setContentDescription(singleChild.getSubCategory());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
}
