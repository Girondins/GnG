package com.grabandgo.gng.gng;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.LinkedList;

public class FragmentOffers extends Fragment{

    private View view;
    private MainActivity mainActivity;
    private LinkedList<Restaurant> listRestaurants;
    private LinkedList<Offers> listOffers;
    private ListViewAdapter adapter;

    public FragmentOffers() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_offers, container, false);

        mainActivity = (MainActivity) getActivity();
        listRestaurants = new LinkedList<Restaurant>();
        listOffers = new LinkedList<Offers>();

        listRestaurants = mainActivity.getRestaurants();

        for(int i = 0; i < listRestaurants.size(); i++){
            Restaurant restaurant = listRestaurants.get(i);
            LinkedList<Offers> list = restaurant.getOfferList();
            if(list.size() > 0){
                for(int j = 0; j < list.size(); j++){
                    listOffers.add(list.get(j));
                }
            }
        }

        ListView listView = (ListView) view.findViewById(R.id.fragment_offers_listview);
        adapter = new ListViewAdapter(view.getContext(), R.id.fragment_offers_listview, listOffers);
        listView.setAdapter(adapter);

        return view;
    }

    private class ListViewAdapter extends ArrayAdapter<Offers> {

        public ListViewAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListViewAdapter(Context context, int resource, LinkedList<Offers> offers) {
            super(context, resource, offers);
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

            Offers offers = null;

            if(listOffers.size() != 0) {
                offers = (Offers) getItem(position);
            }

            if (offers != null) {
                ImageView imageView = (ImageView)view.findViewById(R.id.offers_iv_icon);
                byte[] b = offers.getLogoByte();
                if(b != null) {
                    Bitmap icon = BitmapFactory.decodeByteArray(b, 0, b.length);
                    imageView.setImageBitmap(icon);
                    imageView.setClipToOutline(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }

                TextView tvRestaurantName = (TextView) view.findViewById(R.id.offers_tv_name);
                tvRestaurantName.setText(offers.getRestaurant());

                TextView tvRestaurantOffer = (TextView) view.findViewById(R.id.offers_tv_offer_name);
                tvRestaurantOffer.setText(offers.getTitle());

                TextView tvDuration = (TextView) view.findViewById(R.id.offers_tv_duration);
                tvDuration.setText("Gäller: " + offers.getEnd());

                TextView tvWhom = (TextView) view.findViewById(R.id.offers_tv_whom);
                tvWhom.setText(offers.getWhom());
            }

            return view;
        }
    }
}
