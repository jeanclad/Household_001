package com.dmc.camera.assist;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.util.Log;

public class ZoomControl {
	private final String TAG = "ZoomControl";
	private Camera mCamera;
	static float oldFixScale = 1f;	
	
	public void setCamera(Camera camera){
		mCamera = camera;
	}
	
	public void initOldFixScale(){
		oldFixScale = 1f;
	}
	
	@SuppressLint("DefaultLocale")
	public void setZoomControl(float scale, float maxScale){
		Camera.Parameters parameters = mCamera.getParameters();
		if (parameters.isZoomSupported()) {
			int maxZoom = parameters.getMaxZoom();
			int zoom = parameters.getZoom();
			Log.e(TAG, "maxZoom =" + maxZoom);	
			
			//---	소수점 둘째자리부터 버림 : 동일한 핀치 포인트임에도 불구하고 scale이 소수점 둘째자리부터 흔들림으로 인한 코
			String num = String.format("%.1f", scale);
			float fixScale = Float.parseFloat(num);
			
			//---	이전 이벤트떄의 scale 값과 비교하여 값이 같을때에는 리턴한다.
			if (oldFixScale == fixScale){
				return;
			}else if (oldFixScale < fixScale){
				zoom = zoom + 4;
			}
			else if (oldFixScale > fixScale){
				zoom = zoom - 4;
			}
			
			Log.e(TAG, "oldFixScale = " + oldFixScale + "flxScale = " + fixScale);			
			oldFixScale = fixScale;		
			
			if ((zoom >= maxZoom) || (zoom < 0))
				return;

			Log.e(TAG, "zoom = " + zoom);

			parameters.setZoom(zoom);
			mCamera.setParameters(parameters);

		}
	}
}
