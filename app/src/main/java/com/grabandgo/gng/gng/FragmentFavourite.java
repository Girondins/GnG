package com.grabandgo.gng.gng;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

public class FragmentFavourite extends Fragment implements AdapterView.OnItemLongClickListener {

    private View view;
    private MainActivity mainActivity;
    private LinkedList<Restaurant> ll;
    private ListViewAdapter adapter;

    public FragmentFavourite() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        mainActivity = (MainActivity) getActivity();

        ListView lv = (ListView) view.findViewById(R.id.fragment_list_view_favourites);
        ll = mainActivity.getFavourites();
        adapter = new ListViewAdapter(view.getContext(), R.id.fragment_list_view_favourites, ll);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(this);
        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {

        final Restaurant restaurant = (Restaurant) parent.getItemAtPosition(position);
        view.animate().setDuration(1000).alpha(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        ll.remove(restaurant);
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
        return false;
    }

    private class ListViewAdapter extends ArrayAdapter<Restaurant> {

        public ListViewAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListViewAdapter(Context context, int resource, LinkedList<Restaurant> restaurants) {
            super(context, resource, restaurants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                view = vi.inflate(R.layout.listview_favourite, null);
            }

            Restaurant restaurant = null;

            if(ll.size() != 0) {
                restaurant = (Restaurant) getItem(position);
            }

            if (restaurant != null) {
                TextView restaurantName = (TextView) view.findViewById(R.id.favourite_tv_restaurant);
                TextView restaurantAddress = (TextView) view.findViewById(R.id.favourite_tv_address);
                ImageView restaurantIcon = (ImageView) view.findViewById(R.id.favourite_img_icon);

                restaurantName.setText(restaurant.getName());
                restaurantAddress.setText(restaurant.getAddress());
                byte[] b = restaurant.getLogoImgByte();
                if(b != null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    restaurantIcon.setImageBitmap(bitmap);
                }
            }
            return view;
        }
    }
}
