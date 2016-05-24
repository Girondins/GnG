package com.grabandgo.gng.gng;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

public class FragmentOffers extends Fragment implements AdapterView.OnItemLongClickListener{

    private View view;
    private MainActivity mainActivity;
    private LinkedList<Restaurant> ll;
    private ListViewAdapter adapter;

    public FragmentOffers() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_offers, container, false);

        mainActivity = (MainActivity) getActivity();
        ll = new LinkedList<Restaurant>();

        ListView lv = (ListView) view.findViewById(R.id.fragment_offers_listview);
        ll = mainActivity.getRestaurants();
        adapter = new ListViewAdapter(view.getContext(), R.id.fragment_offers_listview, ll);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(this);

        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {

        final Restaurant restaurant = (Restaurant) parent.getItemAtPosition(position);



        return false;
    }

    private class ListViewAdapter extends ArrayAdapter<Restaurant> {

        public ListViewAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListViewAdapter(Context context, int resource, LinkedList<Restaurant> restaurants) {
            super(context, resource, restaurants);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                view = vi.inflate(R.layout.listview_offer, null);
            }

            Restaurant restaurant = null;

            if(ll.size() != 0) {
                restaurant = (Restaurant) getItem(position);
            }

            LinkedList<Offers> offers = restaurant.getOfferList();

            if (offers != null) {
                ImageView imageView = (ImageView)view.findViewById(R.id.offers_iv_icon);
                imageView.setClipToOutline(true);
                TextView restaurantName = (TextView) view.findViewById(R.id.offers_tv_name);

                restaurantName.setText(restaurant.getName());
            }

            return view;
        }
    }
}
