package com.dmc.camera.assist;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.WindowManager;

public class Util {
	private final static String TAG				= "Util";
	private static	Camera	mCamera;
	
	private	final static String	MODEL_NAME_GALAXY_S4		=	"SHV-E300";
	private	final static String	MODEL_NAME_GALAXY_NOTE2	=	"SHV-E250";
	
	public final static int DEVICE_GALAXY_S4					= 0x01;
	public final static int DEVICE_GALAXY_NOTE2					= 0x02;
	
	public final static int PREVIEW_GALAXY_S4_RATIO_16_9		= 0x01;
	public final static int PREVIEW_GALAXY_S4_RATIO_4_3		= 0x02;
	public final static int PREVIEW_GALAXY_NOTE2_RATIO_16_9	= 0x03;
	public final static int PREVIEW_GALAXY_NOTE2_RATIO_4_3	= 0x04;	
	
	
	private static int galaxyS4PreviewTotalSize[][] = {{1920, 1080}, {1440, 1080}};
	private static int galaxyS4PreviewSize_16_9[] = {1920, 1080};
	private static int galaxyS4PreviewSize_4_3[] = {1440, 1080};	
	
	private static int galaxyNote2PreviewTotalSize[][] = {{100, 200}, {200, 300}, {300, 400}, {300, 400}};
	private static int galaxyNote2PreviewSize_16_9[] = {100, 200};
	private static int galaxyNote2PreviewSize_4_3[] = {300, 400};
	
	private static int galaxyS4PictureSize_16_9[][] = {{4128, 2322}, {3264, 1836}, {2048,1152}};
	private static int galaxyS4PictureSize_4_3[][] = {{4128, 3096}, {3264, 2448}};
	
	private static int galaxyNote2PictureSize_16_9[][] = {{100, 200}, {200, 300}};
	private static int galaxyNote2PictureSize_4_3[][] = {{400, 500}, {500, 600}};	
	
	private static int mPreviewWidth = 0;
	private static int mPreviewHeigth = 0;
	private static int mRatioSize = 0;
	
	private static int mOrientationCompensation = 0;
	private static final int ORIENTATION_HYSTERESIS = 5;
	
	public final static int	ORIENTATION_COMPENSATAION_VERTICAL = 1;
	public final static int	ORIENTATION_COMPENSATAION_HORIZONTAL = 91;
	public final static int	ORIENTATION_COMPENSATAION_R_HORIZONTAL = 271;	
	
	public final static int PREVIEW_LEFT_MARGIN	= 240;
	
	private Util(){	
	}
	
	public static void setCamera(Camera camera){
		mCamera = camera;
	}
	
	public static int getMyDevice(){
        String	modelFullName = Build.MODEL;	
        int		myDevice;
        
        Log.e(TAG, "modelName is :"+Build.MODEL);
        Log.e(TAG, "device is :"+Build.DEVICE);
        Log.e(TAG, "ProductName is :"+Build.PRODUCT);
        
        if (MODEL_NAME_GALAXY_S4.matches(modelFullName)){
        	myDevice = DEVICE_GALAXY_S4;
        } else if (MODEL_NAME_GALAXY_NOTE2.matches(modelFullName)){
        	myDevice = DEVICE_GALAXY_NOTE2;
        } else{
        	myDevice = DEVICE_GALAXY_S4;
        }
        
        Log.e(TAG, "myDevice is " + myDevice);
        
        return myDevice;
	}
	
	public static int[][] getSupportedPreviewSize(int device){
		if (device == DEVICE_GALAXY_S4)
			return galaxyS4PreviewTotalSize;
		else if (device == DEVICE_GALAXY_NOTE2)
			return galaxyNote2PreviewTotalSize;
		else
			return galaxyS4PreviewTotalSize;
	}
	
	public static int[][] getSupportedPictureSize(int device, int ratio){
		if (device == DEVICE_GALAXY_S4){
			if (ratio == PREVIEW_GALAXY_S4_RATIO_16_9){
				return galaxyS4PictureSize_16_9;
			}
			else if (ratio == PREVIEW_GALAXY_S4_RATIO_4_3){
				return galaxyS4PictureSize_4_3;
			}
			else
				return galaxyNote2PictureSize_16_9;
		} else {
			if (ratio == PREVIEW_GALAXY_NOTE2_RATIO_16_9){
				return galaxyNote2PictureSize_16_9;
			}
			else if (ratio == PREVIEW_GALAXY_NOTE2_RATIO_4_3){
				return galaxyNote2PictureSize_4_3;
			}
			else
				return galaxyNote2PictureSize_16_9;
		}
	}
	
	public static int[] getMatchedPreviewSizeFromPictureSize(int device, int[] pictureSize){
		int[]	retVal = {0,0};
		
		if (device == DEVICE_GALAXY_S4){
			for (int i = 0; i < galaxyS4PictureSize_16_9.length; i++) {
				if (Arrays.equals(pictureSize, galaxyS4PictureSize_16_9[i])){
					retVal = galaxyS4PreviewSize_16_9;
					return retVal;
				}
			}
			
			for (int i = 0; i < galaxyS4PictureSize_4_3.length; i++) {
				if (Arrays.equals(pictureSize, galaxyS4PictureSize_4_3[i])){
					retVal = galaxyS4PreviewSize_4_3;
					return retVal;
				}
			}
				
		} else if (device == DEVICE_GALAXY_NOTE2){
			for (int i = 0; i < galaxyNote2PictureSize_16_9.length; i++) {
				if (Arrays.equals(pictureSize, galaxyNote2PictureSize_16_9[i])){
					retVal = galaxyNote2PreviewSize_16_9;
					return retVal;
				}
			}
			
			for (int i = 0; i < galaxyNote2PictureSize_4_3.length; i++) {
				if (Arrays.equals(pictureSize, galaxyNote2PictureSize_4_3[i])){
					retVal = galaxyNote2PreviewSize_4_3;
					return retVal;
				}
			}
		} else {
			for (int i = 0; i < galaxyS4PictureSize_16_9.length; i++) {
				if (Arrays.equals(pictureSize, galaxyS4PictureSize_16_9[i])){
					retVal = galaxyS4PreviewSize_16_9;
					return retVal;
				}
			}
			
			for (int i = 0; i < galaxyS4PictureSize_4_3.length; i++) {
				if (Arrays.equals(pictureSize, galaxyS4PictureSize_4_3[i])){
					retVal = galaxyS4PreviewSize_4_3;
					return retVal;
				}
			}
		}
		return retVal;
	}
	
	public static int getPreviewRatio(int myDevice, int width, int height){
		double ratio_G4_16_9 =(double)  16 / 9;
		double ration_G4_4_3 = (double) 4 / 3;
		double ratio_N2_16_9 = (double) 16 / 9;
		double ration_N2_4_3 = (double) 4 / 3;
		double ratio = (double) width / height;
		int	myRatio = 0;
		
		if (myDevice == DEVICE_GALAXY_S4){
			for (int i=0 ; i<galaxyS4PreviewSize_16_9.length ; i++){
				if (ratio == ratio_G4_16_9){
					myRatio = PREVIEW_GALAXY_S4_RATIO_16_9;
					mRatioSize = myRatio;
					return myRatio;
				}
			}
			for (int i=0 ; i<galaxyS4PreviewSize_4_3.length ; i++){
				if (ratio == ration_G4_4_3){
					myRatio = PREVIEW_GALAXY_S4_RATIO_4_3;
					mRatioSize = myRatio;
					return myRatio;
				}
			}
		} else if (myDevice == DEVICE_GALAXY_NOTE2){
			for (int i=0 ; i<galaxyNote2PreviewSize_16_9.length ; i++){
				if (ratio == ratio_N2_16_9){
					myRatio = PREVIEW_GALAXY_NOTE2_RATIO_16_9;
					mRatioSize = myRatio;
					return myRatio;
				}
			}
			for (int i=0 ; i<galaxyNote2PreviewSize_4_3.length ; i++){
				if (ratio == ration_N2_4_3){				
					myRatio = PREVIEW_GALAXY_NOTE2_RATIO_4_3;
					mRatioSize = myRatio;
					return myRatio;					
				}
			}
		} else {
			for (int i=0 ; i<galaxyS4PreviewSize_16_9.length ; i++){
				if (ratio == ratio_G4_16_9) {
					myRatio = PREVIEW_GALAXY_S4_RATIO_16_9;
					mRatioSize = myRatio;
					return myRatio;					
				}
			}
			for (int i=0 ; i<galaxyS4PreviewSize_4_3.length ; i++){
				if (ratio == ration_G4_4_3) {
					myRatio = PREVIEW_GALAXY_S4_RATIO_4_3;
					mRatioSize = myRatio;
					return myRatio;					
				}
			}
		}

		return myRatio;
	}
	
	public static int getMyRatioSize(){
		return mRatioSize;
	}
	
	public static Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratico and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspecmt ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}
	
	public static float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);

		return FloatMath.sqrt(x * x + y * y);
	}
	
	/**
	 * Android에서 지원되는 사진 Size 리스트를 반환한다.
	 * 
	 * @return
	 */
	public static List<Camera.Size> getSupportedPictureSizes() {
	    if (mCamera == null) {
	        return null;
	    }
	 
	    List<Camera.Size> pictureSizes = mCamera.getParameters().getSupportedPictureSizes();
	             
	    checkSupportedPictureSizeAtPreviewSize(pictureSizes);
	     
	    return pictureSizes;
	}

	 
	private static void checkSupportedPictureSizeAtPreviewSize(List<Camera.Size> pictureSizes) {
	    List<Camera.Size> previewSizes = mCamera.getParameters().getSupportedPreviewSizes();
	    Camera.Size pictureSize;
	    Camera.Size previewSize;
	    double  pictureRatio = 0;
	    double  previewRatio = 0;
	    final double aspectTolerance = 0.05;
	    boolean isUsablePicture = false;
	     
	    for (int indexOfPicture = pictureSizes.size() - 1; indexOfPicture >= 0; --indexOfPicture) {
	        pictureSize = pictureSizes.get(indexOfPicture);
	        pictureRatio = (double) pictureSize.width / (double) pictureSize.height;
	        isUsablePicture = false;
	         
	        for (int indexOfPreview = previewSizes.size() - 1; indexOfPreview >= 0; --indexOfPreview) {
	            previewSize = previewSizes.get(indexOfPreview);
	             
	            previewRatio = (double) previewSize.width / (double) previewSize.height;
	             
	            if (Math.abs(pictureRatio - previewRatio) < aspectTolerance) {
	                isUsablePicture = true;
	                break;
	            }
	        }
	         
	        if (isUsablePicture == false) {
	            pictureSizes.remove(indexOfPicture);
	            Log.e(TAG, "remove picture size : " + pictureSize.width + ", " + pictureSize.height);
	        }
	    }
	}
	
	
	public static void setPreviewSize(int width, int height){
		mPreviewWidth = width;
		mPreviewHeigth = height;
	}
	
	public static int getPreviewSizeWidth(){
		return mPreviewWidth;
	}

	public static int getPreviewSizeHeigth(){
		return mPreviewHeigth;
	}
	

	public static int getDisplayRotation(Context context) {
		int rotation = -1;

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		rotation = display.getRotation();

		return rotation;
	}

	public static int roundOrientation(int orientation, int orientationHistory) {
		boolean changeOrientation = false;
		if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
			changeOrientation = true;
		} else {
			int dist = Math.abs(orientation - orientationHistory);
			dist = Math.min(dist, 360 - dist);
			changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
		}
		if (changeOrientation) {
			return ((orientation + 45) / 90 * 90) % 360;
		}
		return orientationHistory;
	}
	
	public static void setOrientationCompensation(int orientationCompensation){
		mOrientationCompensation = orientationCompensation;
	}
	
	public static int getOrientationCompensation(){
		return mOrientationCompensation;
	}
	
    public static float dpToPx(float dp, Context context){
        float px =  TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, dp,
                    context.getResources().getDisplayMetrics());
        return px;
    }
	
	public static Bitmap byteArrayToBitmap(byte[] byteArray){
		Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		
		return bitmap;
	}
	
	public static byte[] bitmapToByteArray(Bitmap bitmap){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		bitmap.compress(CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}
	
	// 비트맵 이미지 돌리기
	public static Bitmap bitmapRotate(Bitmap bitmap, int degrees) {
		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth() / 2,
					(float) bitmap.getHeight() / 2);

			try {
				Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), m, true);
				if (bitmap != converted) {
					bitmap.recycle();
					bitmap = converted;
				}
			} catch (OutOfMemoryError ex) {
				// 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
			}
		}
		return bitmap;
	}
}
