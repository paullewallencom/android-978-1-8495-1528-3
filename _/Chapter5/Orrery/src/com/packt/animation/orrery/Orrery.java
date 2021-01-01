package com.packt.animation.orrery;
import android.app.FragmentTransaction;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.animation.TypeEvaluator;

public class Orrery extends Activity {
	/** Called when the activity is first created. */

	private class OrreryEvaluator implements TypeEvaluator
	{
		public Object evaluate(
				float fraction,
				Object start,
				Object end)
		{
			OrreryDrawable.SolarSystemData startSolarSystemData = 
					(OrreryDrawable.SolarSystemData) start;
			OrreryDrawable.SolarSystemData endSolarSystemData = 
					(OrreryDrawable.SolarSystemData) end;

			OrreryDrawable.SolarSystemData result = 
					new OrreryDrawable.SolarSystemData();
			result.rotationEarth = 
					startSolarSystemData.rotationEarth
					+ fraction
					* ( endSolarSystemData.rotationEarth 
							- startSolarSystemData.rotationEarth);
			result.rotationMoon = 
					startSolarSystemData.rotationMoon 
					+ fraction 
					* ( endSolarSystemData.rotationMoon 
							- startSolarSystemData.rotationMoon);

			return result;
		}
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		int id;
		ImageView orrery = (ImageView) findViewById(R.id.orrery);
		OrreryDrawable myOrreryDrawable = OrreryDrawable.Create();
		orrery.setImageDrawable(myOrreryDrawable);  
		OrreryEvaluator orreryEvaluator = new OrreryEvaluator(); 

		OrreryDrawable.SolarSystemData startSolarSystemData = 
				new OrreryDrawable.SolarSystemData();
		startSolarSystemData.rotationEarth = 0;
		startSolarSystemData.rotationMoon = 0;

		OrreryDrawable.SolarSystemData endSolarSystemData = 
				new OrreryDrawable.SolarSystemData();
		endSolarSystemData.rotationEarth = (float) (2*Math.PI);
		endSolarSystemData.rotationMoon = (float) (2*Math.PI*13);


		Keyframe startFrame = 
				Keyframe.ofObject(0, startSolarSystemData);
		Keyframe endFrame = 
				Keyframe.ofObject(1, endSolarSystemData);
		PropertyValuesHolder solarSystemFrames = 
				PropertyValuesHolder.ofKeyframe(
						"SolarSystemData", 
						startFrame, 
						endFrame);
		solarSystemFrames.setEvaluator(orreryEvaluator);


		ObjectAnimator orreryAnimator = 
				ObjectAnimator.ofPropertyValuesHolder(
						myOrreryDrawable,
						solarSystemFrames);


		ValueAnimator.setFrameDelay(100);
		orreryAnimator.setDuration(60000);
		orreryAnimator.setInterpolator(new LinearInterpolator());
		orreryAnimator.setRepeatCount(ValueAnimator.INFINITE);
		orreryAnimator.setRepeatMode(ValueAnimator.RESTART);
		orreryAnimator.start();
		FragmentTransaction ft = 
				getFragmentManager().beginTransaction();
		ft.setCustomAnimations(
				R.animator.fade_in,
				android.R.animator.fade_out);
		ft.add(R.id.main, new OrreryInfo());
		ft.commit();

	}


}


