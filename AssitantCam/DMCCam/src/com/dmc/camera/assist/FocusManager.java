package com.dmc.camera.assist;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;

public class FocusManager {
	private static final String TAG = "FocusManager";
	private Camera mCamera 			= null;

	public FocusManager(){		
	}
	
	public void setCamera(Camera camera){
		mCamera = camera;
		setAutoFocusMode();
	}
	
	private void setAutoFocusMode(){
		// get Camera parameters
		Camera.Parameters params = mCamera.getParameters();

		List<String> focusModes = params.getSupportedFocusModes();
		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
			// set the focus mode
			params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			// set Camera parameters
			mCamera.setParameters(params);
		}
	}
	
	private void setArea(Camera camera, List<Camera.Area> list) {
		Camera.Parameters parameters;
		parameters = camera.getParameters();
		if (parameters.getMaxNumFocusAreas() > 0) {
			parameters.setFocusAreas(list);
		}

		if (parameters.getMaxNumMeteringAreas() > 0) {
			parameters.setMeteringAreas(list);
		}

		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
		camera.setParameters(parameters);
	}
	
	private void setAutoFocusArea(Camera camera, int posX, int posY,
			int focusRange, boolean flag, Point point) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			/** 영역을 지정해서 포커싱을 맞추는 기능은 ICS 이상 버전에서만 지원됩니다. **/
			return;
		}

		if (posX < 0 || posY < 0) {
			setArea(mCamera, null);
			return;
		}

		int touchPointX;
		int touchPointY;
		int endFocusY;
		int startFocusY;

		if (!flag) {
			/** Camera.setDisplayOrientation()을 이용해서 영상을 세로로 보고 있는 경우. **/
			touchPointX = point.y >> 1;
			touchPointY = point.x >> 1;

			startFocusY = posX;
			endFocusY = posY;
		} else {
			/** Camera.setDisplayOrientation()을 이용해서 영상을 가로로 보고 있는 경우. **/
			touchPointX = point.x >> 1;
			touchPointY = point.y >> 1;

			startFocusY = posY;
			endFocusY = point.x - posX;
		}

		float startFocusX = 1000F / (float) touchPointY;
		float endFocusX = 1000F / (float) touchPointX;

		startFocusX = (int) (startFocusX * (float) (startFocusY - touchPointY))
				- focusRange;
		startFocusY = (int) (endFocusX * (float) (endFocusY - touchPointX))
				- focusRange;
		endFocusX = startFocusX + focusRange;
		endFocusY = startFocusY + focusRange;

		if (startFocusX < -1000)
			startFocusX = -1000;

		if (startFocusY < -1000)
			startFocusY = -1000;

		if (endFocusX > 1000) {
			endFocusX = 1000;
		}

		if (endFocusY > 1000) {
			endFocusY = 1000;
		}

		Rect rect = new Rect((int) startFocusX, (int) startFocusY,
				(int) endFocusX, (int) endFocusY);
		ArrayList<Camera.Area> arraylist = new ArrayList<Camera.Area>();
		arraylist.add(new Camera.Area(rect, 1000));

		setArea(camera, arraylist);
	}
	
	public void touchFocus(final int posX, final int posY) {
		// TODO Auto-generated method stub
		
		int width = Util.getPreviewSizeWidth();
		int height = Util.getPreviewSizeHeigth();
		
		setAutoFocusArea(mCamera, posX, posY, 128, true,
				new Point(width, height));

		mCamera.autoFocus(myAutoFocusCallback);
	}
	
	AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
			// TODO Auto-generated method stub}

			float focusDistances[] = new float[3];
			arg1.getParameters().getFocusDistances(focusDistances);
			Log.e(TAG, "Optimal Focus Distance(meters): " + focusDistances[Camera.Parameters.FOCUS_DISTANCE_OPTIMAL_INDEX]);

		}
	};	
}
