package com.dmc.camera.assist;

import java.util.Arrays;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;

public class CameraSettings extends Preview{
	private final static String TAG = "CameraSettings";
	private static Camera mCamera = null;
	private static SurfaceHolder mHolder = null;

	public CameraSettings(Context context) {
		super(context);
	}
	
	public void setCamera(Camera camera, SurfaceHolder holder){
		mCamera = camera;
		mHolder = holder;
	}
	
	/* Parameters
	 * flash mode : String
		"off"
		"auto"
		"on"
		"red-eye"
		"torch"
	* Returns : boolean
	* 	true : 플래시값 적용성공
	* 	flase : 플래시가 없는 디바이
	*/
	public boolean setCameraFlashMode(String flashMode) {

		Log.e(TAG, "setCameraFlashMode =" + flashMode);

		Parameters parameters = mCamera.getParameters();
		if (parameters.getSupportedFlashModes() == null){
			return false;
		}
		
		parameters.setFlashMode(flashMode);	
		mCamera.setParameters(parameters);

		return true;
	}
	
	/* Returns : String
		"off"
		"auto"
		"on"
		"red-eye"
		"torch"
	*/	
	public static String getCameraFlashMode(){
		Parameters parameters = mCamera.getParameters();
		String currentFlashMode = parameters.getFlashMode();
		return currentFlashMode;
	}
	
	public boolean setPictureSize(int[] pictureSize){
		int[] emptyArray = {0, 0};
		int[] previewSize = Util.getMatchedPreviewSizeFromPictureSize(Util.DEVICE_GALAXY_S4, pictureSize);
		
		if (Arrays.equals(previewSize, emptyArray)){
			return false;
		}
		
		int previewWidth = previewSize[0];
		int previewHeight = previewSize[1];
		
		int myDevice = Util.getMyDevice();
		int myRatio = Util.getPreviewRatio(myDevice, previewWidth, previewHeight);
		
		Log.e(TAG, "setPreviewSize Width =" + previewWidth + " " + "height =  " + previewHeight);
		Log.e(TAG, "setPictureSize Width =" + pictureSize[0] + " " + "height =  " + pictureSize[1]);
		
		if (myRatio == Util.PREVIEW_GALAXY_S4_RATIO_16_9){
			setZOrderOnTop(false);	
		} else if (myRatio == Util.PREVIEW_GALAXY_S4_RATIO_4_3){
			setZOrderOnTop(true);			
		} else if (myRatio == Util.PREVIEW_GALAXY_NOTE2_RATIO_16_9){
			setZOrderOnTop(false);
		} else if (myRatio == Util.PREVIEW_GALAXY_NOTE2_RATIO_4_3){
			setZOrderOnTop(true);
		} else {			
			setZOrderOnTop(false);
		}
		
		Util.setPreviewSize(previewWidth, previewHeight);
		
		Camera.Parameters parameters = mCamera.getParameters();
		
		//---	저장사진 사이즈 변경
		parameters.setPreviewSize(previewWidth, previewHeight);
		//---	프리뷰 사이즈 변경
		parameters.setPictureSize(pictureSize[0], pictureSize[1]);
		
		mCamera.setParameters(parameters);	
	
		mHolder.setFixedSize(previewWidth, previewHeight);
		
		return true;
	}
}
