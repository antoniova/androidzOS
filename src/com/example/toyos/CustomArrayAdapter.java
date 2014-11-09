package com.example.toyos;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomArrayAdapter<T> extends ArrayAdapter<T> {
	
	/**
	 * Constructor. There are more constructors that need to be
	 * implemented. We can get away with defining just this one for now.
	 * @param context
	 * @param resource
	 * @param objects
	 */
	CustomArrayAdapter(Context context, int resource, List<T> objects){
		super(context, resource, objects);
	}

	/**
	 * Makes it possible to display text using different colors.
	 * Comments can have a different color from the actual code.
	 * Html.fromHtml();
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){

		View view = super.getView(position, convertView, parent);
		TextView text = (TextView) view;
		
		T item = getItem(position);
	    if (item instanceof CharSequence) {
            //text.setText((CharSequence)item);
	    	text.setText(Html.fromHtml(item.toString()));
        } else {
        	text.setText(item.toString());
        }
	    return view;
	}
	
}
