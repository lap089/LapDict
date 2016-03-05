package com.lapdict.lap089.lapdict;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 5/23/2015.
 */
public class CustomArrayAdapter extends ArrayAdapter {

        private static final int RESOURCE = R.layout.customrows;
        private LayoutInflater inflater;
        Setting[] objects;
        static class ViewHolder {
            TextView name;
            ImageView image;
        }

        public CustomArrayAdapter(Context context, Setting[] objects)
        {
            super(context, RESOURCE, objects);
            inflater = LayoutInflater.from(context);
            this.objects=objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;

            if ( convertView == null ) {
                // inflate a new view and setup the view holder for future use
                convertView = inflater.inflate( RESOURCE, null );

                holder = new ViewHolder();
                holder.name =
                        (TextView) convertView.findViewById(R.id.name);
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag( holder );
            }  else {
                // view already defined, retrieve view holder
                holder = (ViewHolder) convertView.getTag();
            }

            Setting cat= objects[position];
            if ( cat == null ) {
                Log.e("Check", "Invalid category for position: " + position );
            }
            holder.name.setText( cat.getName() );
            holder.image.setImageDrawable(cat.getImage());

            return convertView;
        }
    }
