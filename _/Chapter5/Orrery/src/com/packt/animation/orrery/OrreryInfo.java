package com.packt.animation.orrery;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrreryInfo extends Fragment
{
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState)
	{
		LinearLayout result = 
				new LinearLayout(getActivity());
		result.setOrientation(LinearLayout.VERTICAL);
		result.setLayoutParams(
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));

		TextView info = new TextView(getActivity());
		info.setText("Humans live on Earth, and Moon Mice live on the Moon.  Nothing lives on the Sun, because it's a little too hot.");
		info.setWidth(200);
		result.addView(info);
		ImageView alien = new ImageView(getActivity());
		alien.setImageResource(R.drawable.alien);
		result.addView(alien);


		AlphaAnimation anim = new AlphaAnimation(0,1);
		anim.setDuration(4000);
		anim.setStartOffset(4000);
		anim.setInterpolator(new TeleportInterpolator());
		alien.startAnimation(anim);

		return result;
	}
}
