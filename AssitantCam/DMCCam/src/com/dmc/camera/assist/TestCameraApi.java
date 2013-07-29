package com.dmc.camera.assist;

import android.util.Log;

public class TestCameraApi {
	private final static String TAG = "TestCameraApi";
	
	private final String[] flashMode = {"auto", "on", "off" , "red-eye", "torch"};
	
	private final int galaxyS4PictureSizeList[][] = {{4128, 2322}, {3264, 1836}, {2048,1152}, {4128, 3096}, {3264, 2449}};
	private final int galaxyNote2PictureSizeList[][] = {{100, 200}, {200, 300}, {400, 500}, {500, 600}};	
	private int pictureList[];
	
	private static int	flashOrder = 0;	
	private static int	pictureOrder = 0;
	private static int	pictureFianlOrder  = 0;
	
	private CameraSettings mCameraSettings = null;
	
	public TestCameraApi(CameraSettings cameraSettings){
		mCameraSettings = cameraSettings;
	}
	
	public void testSetCameraFlashMode(){			
		if (flashOrder > 4)
			flashOrder = 0;
		
		Log.e(TAG, "FlashMode = " + flashMode[flashOrder]);
		mCameraSettings.setCameraFlashMode(flashMode[flashOrder]);
		flashOrder ++;
	}
	
	public void testSetCameraPictureSize(){
		if (Util.getMyDevice() == Util.DEVICE_GALAXY_S4){
			pictureFianlOrder = galaxyS4PictureSizeList.length -1;
		} else if (Util.getMyDevice() == Util.DEVICE_GALAXY_NOTE2){
			pictureFianlOrder = galaxyNote2PictureSizeList.length -1;
		} else{
			pictureFianlOrder = galaxyS4PictureSizeList.length -1;			
		}
		
		if (pictureOrder > pictureFianlOrder){
			pictureOrder = 0;
		}
		
		if (Util.getMyDevice() == Util.DEVICE_GALAXY_S4){
			pictureList = galaxyS4PictureSizeList[pictureOrder];
		} else if (Util.getMyDevice() == Util.DEVICE_GALAXY_NOTE2){
			pictureList = galaxyNote2PictureSizeList[pictureOrder];
		} else{
			pictureList = galaxyS4PictureSizeList[pictureOrder];
		}
		
		pictureOrder++;
		
		mCameraSettings.setPictureSize(pictureList);
	}
}
