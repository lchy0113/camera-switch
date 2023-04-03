package com.richgold.cameraactivity;

import android.Manifest;
import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class CameraActivity extends Activity {

    private final static String TAG = CameraActivity.class.getSimpleName();
    private final static int BACK_CAMERA_ID = 0;        /* door camera */
    private final static int FRONT_CAMERA_ID = 1;       /* internal camera */
    private final static int EXTERNAL_CAMERA_ID = 2;    /* external camera */
    private static final int REQUEST_NORMAL = 10000;
    private Button mBtnPreviewCamera0;  // 'prewview0' button
    private Button mBtnPreviewCamera1;  // 'prewview1' button
    private Button mBtnPreviewCamera2;  // 'prewview2' button
    private Button mBtn_takePicture;    // 'TAKE_PICTURE' button
    private Button mBtn_send_ha_cmd;    // 'SEND HA_COMMAND(CALL_ON)' button
    private int mCameraFacing = 0;       // default camera id
    private SurfaceHolder mHolder;
    private SurfaceView mSurfaceView;
    private Camera mCamera;

    protected final SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.d(TAG, "#surfaceCreated() - ");
            try {
                Log.d(TAG, "#surfaceCreated() - " + "Camera.getNumberOfCameras : " + Camera.getNumberOfCameras());
                mCamera = Camera.open(mCameraFacing);
                /**
                 * support mCameraFacing value :
                 * 0 : back camera
                 * 1 : front camera
                 * 2 : external cctv type.
                 */
                if (mCamera != null) {
                    Log.d(TAG, "mCamera : " + mCamera);
                    Camera.Parameters parameters = mCamera.getParameters();
                    //parameters.setPreviewSize(mSurfaceView.getWidth(), mSurfaceView.getHeight());
                    List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
                    // You need to choose the most appropriate previewSize for your app
                    Camera.Size previewSize = previewSizes.get(0);
                    parameters.setPreviewSize(previewSize.width, previewSize.height);
                    Log.d(TAG, "previewSize.width: " + previewSize.width + " previewSize.height: " + previewSize.height);
                    //parameters.set("sensor-type", "tvi-720p");
                    if (mCameraFacing == EXTERNAL_CAMERA_ID)
                        parameters.set("sensor-type", "cvi-720p");
                    /**
                     * support sensor type :
                     * tvi-720p
                     * tvi-1080p
                     * ahd-720p
                     * ahd-1080p
                     * cvi-720p
                     * cvi-1080p
                     * ntsc
                     */
                    mCamera.setParameters(parameters);
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();
                }
            } catch (Exception e) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
                Log.d(TAG, "#surfaceCreated() - " + e.getMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            Log.d(TAG, "#surfaceChanged()");
            refreshCamera();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (mCamera != null) {
                Log.d(TAG, "#surfaceDestroyed() - ");
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } else {
                Log.d(TAG, "#surfaceDestroyed() - camera is null");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mCameraFacing = BACK_CAMERA_ID;
        init();
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "#onCreate() - request permission");
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_NORMAL);
        } else {
            Log.d(TAG, "#onCreate() - granted");
            mSurfaceView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_NORMAL) {
            boolean anyDenied = Arrays.stream(grantResults).anyMatch(r -> r == PackageManager.PERMISSION_DENIED);
            if (!anyDenied) {
                Log.d(TAG, "#onRequestPermissionsResult() - granted");
                mSurfaceView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void init() {
        Log.d(TAG, "#init() - ");
        mSurfaceView = findViewById(R.id.callSurfaceView);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(mSurfaceHolderCallback);

        mBtnPreviewCamera0 = findViewById(R.id.mBtnPreviewCamera0);
        mBtnPreviewCamera0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCameraFacing = BACK_CAMERA_ID;
                // Invalidate the surface view
                mSurfaceView.setVisibility(View.GONE);
                mSurfaceView.setVisibility(View.VISIBLE);
            }
        });

        mBtnPreviewCamera1 = findViewById(R.id.mBtnPreviewCamera1);
        mBtnPreviewCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCameraFacing = FRONT_CAMERA_ID;
                // Invalidate the surface view
                mSurfaceView.setVisibility(View.GONE);
                mSurfaceView.setVisibility(View.VISIBLE);
            }
        });

        mBtnPreviewCamera2 = findViewById(R.id.mBtnPreviewCamera2);
        mBtnPreviewCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCameraFacing = EXTERNAL_CAMERA_ID;
                // Invalidate the surface view
                mSurfaceView.setVisibility(View.GONE);
                mSurfaceView.setVisibility(View.VISIBLE);
            }
        });

        mBtn_takePicture = findViewById(R.id.mBtn_takePicture);
        mBtn_takePicture.setOnClickListener(new View.OnClickListener() {
            Process process;
            @Override
            public void onClick(View arg0) {
                try {
                    BufferedReader br= new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null)  {
                        Log.i("send ha command", line);
                    }
                } catch (Exception e) {
                    e.fillInStackTrace();
                    Log.e("send_ha_command", "un execute command");
                }
            }
        });

        mBtn_send_ha_cmd = findViewById(R.id.mBtn_send_ha_cmd);
        mBtn_send_ha_cmd.setOnClickListener(new View.OnClickListener() {
            Runtime runtime = Runtime.getRuntime();
            Process process;
            String res = "-0-";
            @Override
            public void onClick(View arg0) {
                try {
                    String cmd = "ha_command 1";
                    process = runtime.exec(cmd);
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
/*
                    mCamera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);
*/
                    while ((line = br.readLine()) != null)  {
                        Log.i("send ha command", line);
                    }

                } catch (Exception e) {
                    e.fillInStackTrace();
                    Log.e("send_ha_command", "un execute command");
                }
            }
        });
    }
/*
    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback(){
      @Override
      public void onShutter() {

      }
    };

    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            //TODO Auto-generated method stub
        };
    };

    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback() {
      @Override
      public void onPicturTaken(byte[] arg0, Camera arg1)   {
          Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
      }
    };
*/
    public void refreshCamera() {
        Log.d(TAG, "#refreshCamera() - ");
        if (mHolder.getSurface() == null) return;
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.d(TAG, "#refreshCamera() - " + e.getMessage());
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "#refreshCamera() - " + e.getMessage());
        }
    }
}
