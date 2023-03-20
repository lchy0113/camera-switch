package com.richgold.cameraactivity;

import android.Manifest;
import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

public class CameraActivity extends Activity {

    private final static String TAG = CameraActivity.class.getSimpleName();
    private final static int BACK_CAMERA_ID = 0;    /* door camera */
    private final static int FRONT_CAMERA_ID = 1; /* internal camera */
    private static final int REQUEST_NORMAL = 10000;
    private Button mBtnChangeFacing;  // '카메라 전환' 버튼
    private int mCameraFacing;       // 전면 or 후면 카메라 상태 저장
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
                 * 2 : tp2860 external cctv type.
                 */
                if (mCamera != null) {
                    Log.d(TAG, "mCamera : " + mCamera);
                    Camera.Parameters parameters = mCamera.getParameters();
                    //parameters.setPreviewSize(mSurfaceView.getWidth(), mSurfaceView.getHeight());
                    List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
                    // You need to choose the most appropriate previewSize for your app
                    Camera.Size previewSize = previewSizes.get(0);
                    parameters.setPreviewSize(previewSize.width, previewSize.height);
                    //parameters.set("sensor-type", "tvi-720p");
                    parameters.set("sensor-type", "pal");
                    /**
                     * support sensor type :
                     * tvi-720p
                     * tvi-1080p
                     * ahd-720p
                     * ahd-1080p
                     * cvi-720p
                     * cvi-1080p
                     * ntsc
                     * pal
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
/*
        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            Log.d(TAG, "#surfaceChanged()");
            refreshCamera();
        }
 */
        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            Log.d(TAG, "#surfacechanged()");
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
        mBtnChangeFacing = findViewById(R.id.button_change_facing);
        mBtnChangeFacing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCameraFacing = (mCameraFacing == FRONT_CAMERA_ID) ? BACK_CAMERA_ID : FRONT_CAMERA_ID;
                // Invalidate the surface view
                mSurfaceView.setVisibility(View.GONE);
                mSurfaceView.setVisibility(View.VISIBLE);
            }
        });
    }

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
