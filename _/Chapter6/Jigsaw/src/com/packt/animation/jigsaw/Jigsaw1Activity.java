package com.packt.animation.jigsaw;


import android.animation.Animator;
import android.animation.ObjectAnimator;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.graphics.drawable.Drawable;

public class Jigsaw1Activity extends Activity {
	/** Called when the activity is first created. */


	private int[] pieceIDs =
		{
			R.id.jigsawTopLeft,
			R.id.jigsawTopRight,
			R.id.jigsawBottomLeft,
			R.id.jigsawBottomRight

		};
	int firstPiece = -1;
	private void changeSiblingsFocus(
			int callingPieceID,
			int otherPieceID,
			float fromFocus,
			float toFocus)
	{
		for (int pieceID: pieceIDs)
		{
			if ( callingPieceID != pieceID
					&& otherPieceID   != pieceID)
			{
				RaisableImageView sibling = 
						(RaisableImageView) findViewById(pieceID);
				ObjectAnimator focusSibling = 
						ObjectAnimator.ofFloat(
								sibling, "Focus",fromFocus,toFocus);
				focusSibling.setDuration(1000);
				focusSibling.start();
			}
		}
	}




	private class PieceSwapper


	implements Animator.AnimatorListener
	{
		private int firstID, secondID;
		public  PieceSwapper(int firstID, int secondID)
		{
			this.firstID=firstID;
			this.secondID=secondID;
		}
		public void onAnimationCancel(Animator animation)
		{
		}
		public void onAnimationRepeat(Animator animation)
		{
		}
		public void onAnimationStart(Animator animation)
		{
		}

		public void onAnimationEnd(Animator animation)
		{
			RaisableImageView first = 
					(RaisableImageView) findViewById(firstID);
			RaisableImageView second = 
					(RaisableImageView) findViewById(secondID);

			{
				Drawable temp = first.getDrawable();
				first.setDrawable(second.getDrawable());
				second.setDrawable(temp);
			}

			ObjectAnimator dropFirst = 
					ObjectAnimator.ofFloat(first, "Depth", 0.3f,0);
			dropFirst.setDuration(1000);
			dropFirst.start();

			ObjectAnimator dropSecond = 
					ObjectAnimator.ofFloat(second, "Depth", 0.3f,0);
			dropSecond.setDuration(1000);
			dropSecond.start();

			firstPiece = -1;
			changeSiblingsFocus(firstID,secondID,0.5f,1);

		}
	}




	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		for (final int pieceID : pieceIDs)
		{
			View piece = findViewById(pieceID);
			piece.setOnClickListener(
					new View.OnClickListener()
					{
						public void onClick(View v)
						{
							if (firstPiece == pieceID) return;
							v.bringToFront();
							ObjectAnimator raiseAnimation = 
									ObjectAnimator.ofFloat(
											v, "Depth", 0,0.3f);
							raiseAnimation.setDuration(1000);
							raiseAnimation.start();
							changeSiblingsFocus(pieceID,firstPiece,1,0.5f);


							if (firstPiece == -1) firstPiece = pieceID;
							else
							{
								raiseAnimation.addListener(
										new PieceSwapper(firstPiece,pieceID));
							}

							View board = findViewById (R.id.jigsawbody);
							Rotate3dAnimation r3d = 
									new Rotate3dAnimation(0,360,300,200,0,false);
							r3d.setDuration(2000);
							board.startAnimation(r3d);


						}

						private void changeSiblingsFocus(int pieceID,
								int firstPiece, int i, float f) {
							// TODO Auto-generated method stub

						}
					});
		}
	}

}
