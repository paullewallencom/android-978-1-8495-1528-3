package com.packt.animation.hanoi;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;


public class HanoiActivity extends Activity {
	/** Called when the activity is first created. */
	private static int[] towers = {
		R.id.tower_1,
		R.id.tower_2,
		R.id.tower_3
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView block1 = (TextView) findViewById(R.id.block_1);

	}
	private class BlockMover implements Animation.AnimationListener {
		private int to, from;
		View block;
		protected BlockMover (View block, int from, int to) {
			this.block = block;
			this.to = to;
			this.from = from;
		}
		public void onAnimationRepeat(Animation animation) {}
		public void onAnimationStart(Animation animation) {}
		public void onAnimationEnd(Animation animation) {
			// We will fill this in in a minute
			block.post(new Runnable() {
				public void run() {
					ViewGroup toTower = (ViewGroup) findViewById(towers[to]);
					ViewGroup fromTower = (ViewGroup) block.getParent();
					fromTower.removeView(block);
					fromTower.clearDisappearingChildren();
					toTower.addView(block);
					Animation addAnimation = 
							AnimationUtils.loadAnimation(HanoiActivity.this, 
									R.anim.block_drop);
					block.setAnimation(addAnimation);

				}
			});

		}

		public void move() {
			// We will fill this in in a minute
			Animation removeAnimation = AnimationUtils.loadAnimation(
					HanoiActivity.this, R.anim.block_move_right);
			block.startAnimation(removeAnimation);

		}
	}
	private class TowerPicker implements View.OnClickListener {
		private int towerIndex;
		public TowerPicker (int towerIndex) {
			this.towerIndex = towerIndex;
		}
		public void onClick(View v) {
			// We will fill this in in a minute
			if (fromTower == UNDECIDED) {
				ViewGroup tower = 
						(ViewGroup) findViewById(towers[towerIndex]);
				if (tower.getChildCount()>0) {
					fromTower = towerIndex;
					Animation glowAnimation = 
							AnimationUtils.loadAnimation(
									HanoiActivity.this,
									R.anim.tower_glow
									);
					tower.startAnimation(glowAnimation);

				}
			} else {
				ViewGroup fromTowerView = 
						(ViewGroup) findViewById(towers[fromTower]);
				if (fromTower != towerIndex) {
					ViewGroup toTowerView = 
							(ViewGroup) findViewById(towers[towerIndex]);
					View block = fromTowerView.getChildAt(0);
					View supportingBlock = toTowerView.getChildAt(0);


					if (supportingBlock == null 
							|| supportingBlock.getWidth()>block.getWidth()) {
						(new BlockMover (block,fromTower, towerIndex)).move();
					}
				}
				fromTowerView.clearAnimation();
				fromTower = UNDECIDED;
			}
		}
	}
	private static final int UNDECIDED = -1;
	private int fromTower = UNDECIDED;



}