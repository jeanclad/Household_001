package com.dmc.camera.assist;

import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity implements OnClickListener {

	private final static String TAG = "MainActivity";

	private Preview mPreview = null;
	private CameraSettings mCameraSettings = null;
	private TestCameraApi mTestCameraApi = null;
	private int mCameraFacing;
	static Context context;
	private Button buttonTakePicture, buttonFlash, buttonPictureSize,
			buttonCameraMode;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		// Hide the window title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		mCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;

		mCameraSettings = new CameraSettings(context);

		// ---test by hkkwon
		mTestCameraApi = new TestCameraApi(mCameraSettings);

		// Create our Preview view and set it as the content of our activity
		mPreview = new Preview(context, mCameraFacing, mCameraSettings);

		setContentView(mPreview);

		LayoutInflater inflator = getLayoutInflater();
		View layout = (View) inflator.inflate(R.layout.main_button, null);

		addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		/*
		ImageButton mainGalleryButton = (ImageButton) findViewById(R.id.main_gallery_button);
		
	
		mainGalleryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTestCameraApi.testSetCameraPictureSize();
				setPreviewPostionChange();
			}
		});
		*/

		OrientationEventListener oel = new OrientationEventListener(this) {
			int mOrientation = OrientationEventListener.ORIENTATION_UNKNOWN;
			int mOrientationCompensation = 0;

			@Override
			public void onOrientationChanged(int orientation) {

				// We keep the last known orientation. So if the user first
				// orient
				// the camera then point the camera to floor or sky, we still
				// have
				// the correct orientation.
				if (orientation == ORIENTATION_UNKNOWN)
					return;

				mOrientation = Util.roundOrientation(orientation, mOrientation);
				// When the screen is unlocked, display rotation may change.
				// Always
				// calculate the up-to-date orientationCompensation.
				int orientationCompensation = mOrientation
						+ Util.getDisplayRotation(context);
				if (mOrientationCompensation != orientationCompensation) {
					mOrientationCompensation = orientationCompensation;
					Util.setOrientationCompensation(mOrientationCompensation);
					Log.e(TAG, "mOrientationCompensation = "
							+ mOrientationCompensation);
				}

			}
		};
		oel.enable();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.buttonTakePicture) {
			mPreview.capture();
		} else if (v.getId() == R.id.buttonFlash) {
			mTestCameraApi.testSetCameraFlashMode();
		} else if (v.getId() == R.id.buttonPictureSize) {
			// --- test by hkkwon
			mTestCameraApi.testSetCameraPictureSize();
		}
	}

	private void setPreviewPostionChange() {
		int margin = 0;

		int width = Util.getPreviewSizeWidth();
		int height = Util.getPreviewSizeHeigth();

		int myRatioSize = Util.getMyRatioSize();

		if ((myRatioSize == Util.PREVIEW_GALAXY_S4_RATIO_4_3)
				|| (myRatioSize == Util.PREVIEW_GALAXY_NOTE2_RATIO_4_3))
			margin = Util.PREVIEW_LEFT_MARGIN;

		/*
		RelativeLayout.LayoutParams layoutParms = new RelativeLayout.LayoutParams(
				width, height); // width, height
		layoutParms.setMargins(margin, 0, 0, 0); // left, // top, // 0,// 0
		mPreview.setLayoutParams(layoutParms);
		*/
	}
}