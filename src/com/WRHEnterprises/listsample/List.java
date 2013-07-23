package com.WRHEnterprises.listsample;

/*
 *	Programmer: Bill Herman
 * 	Date: 7/22/2013
 *  Description: Mimics Facebook logic for dropdown item in status feed 
 */

import android.os.Bundle;
import android.R.interpolator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class List extends Activity {

	// List of 30 strings with name values
	String[] items;

	// Three part item header layout
	LinearLayout itemHeader;

	// Layout for list of items
	ListView itemList;

	// Toggle to control display of item header
	Boolean display = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Pull in definition of header and list
		itemHeader = (LinearLayout) findViewById(R.id.itemHeader);
		itemList = (ListView) findViewById(R.id.itemList);

		// Pull in values from string xml file array
		Resources res = getResources();
		items = res.getStringArray(R.array.item_array);

		// Link custom adapter
		ItemAdapter itemAdapter = new ItemAdapter(getApplicationContext());
		itemList.setAdapter(itemAdapter);

		// Listen for swipes (press down, move, and lift up)
		itemList.setOnTouchListener(new OnTouchListener() {
			int before = -1;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {

				// Detect finger down and set index where screen pressed
				case MotionEvent.ACTION_DOWN:
					before = ((ListView) v).getFirstVisiblePosition();
					break;

				// Detect finger up and compare current index to previous index
				case MotionEvent.ACTION_UP:
					int now = ((ListView) v).getFirstVisiblePosition();
					if (now < before) {
						MoveDown();
					} else if (now > before) {
						MoveUp();
					}
				}
				return false;
			}
		});

	}

	// Wrote custom adapter because 9 times out of 10 end up using them
	// instead of standard adapter by the time the project is complete
	class ItemAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public ItemAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return items.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_row, null);
				convertView.setId(position);
				holder = new ViewHolder();
				holder.item_entry = (TextView) convertView
						.findViewById(R.id.item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// Pull in item array entry to set string
			String item = items[position];
			holder.item_entry.setText(item);

			return convertView;
		}

		class ViewHolder {
			TextView item_entry;
		}
	}

	// Determine display of header based on swipe up
	private void MoveUp() {

		// Trigger animation only once when swipe direction is up
		if (display) {

			// Move up 100 from current location for 100 milliseconds
			Animation animation = new TranslateAnimation(0, 0, 0, -100);
			animation.setInterpolator(this, interpolator.linear);
			animation.setDuration(100);

			// Move both the header and list in unison
			itemHeader.startAnimation(animation);
			itemList.startAnimation(animation);

			// At end of animation, make the header go away so it does not
			// re-appear
			animation.setAnimationListener(new Animation.AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					itemHeader.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});

			// Change display status so animation is not re-triggered on
			// multiple up swipes
			display = false;

		}
	}

	// Determine display of header based on swipe down
	private void MoveDown() {

		// Trigger animation only once when swipe direction is down
		if (display == false) {

			// Move down 100 from current location for 100 milliseconds
			Animation animation = new TranslateAnimation(0, 0, -100, 0);
			animation.setInterpolator(this, interpolator.linear);
			animation.setDuration(100);

			// Animate header and list at the same time
			itemHeader.startAnimation(animation);
			itemList.startAnimation(animation);

			// Make header visible again
			itemHeader.setVisibility(View.VISIBLE);

			// Toggle display on again
			display = true;
		}
	}

}
