package cadovvl.cadovvl.cadovvl.gd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class CameraActivity extends Activity {
    public static String PACKAGE_NAME;
    private static final String TAG = "CameraActivity";
    Preview preview;
    ImageButton buttonClick;
    Camera camera;
    Activity act;
    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.camera_activity);

        preview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
        preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);

        preview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                camera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });

        buttonClick = (ImageButton) findViewById(R.id.imgCapture);

        buttonClick.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                camera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });

        buttonClick.setOnLongClickListener(new OnLongClickListener(){
            @Override
            public boolean onLongClick(View arg0) {
                camera.autoFocus(new Camera.AutoFocusCallback(){
                    @Override
                    public void onAutoFocus(boolean arg0, Camera arg1) {
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            try{
                camera = Camera.open(0);
                camera.startPreview();
                preview.setCamera(camera);
                setCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_BACK, camera);
            } catch (RuntimeException ex) {
            }
        }
    }

    @Override
    protected void onPause() {
        if(camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    private void resetCam() {
        camera.startPreview();
        preview.setCamera(camera);
        setCameraDisplayOrientation(this,Camera.CameraInfo.CAMERA_FACING_BACK,camera);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            try {
                String url = new SaveImageTask().execute(data).get();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("image_url", url);
                setResult(Activity.RESULT_OK, resultIntent);
            } catch (Exception e) {
                Log.e("Lol", e.getMessage());
            }
            CameraActivity.this.finish();
        }
    };

    private class SaveImageTask extends AsyncTask<byte[], Void, String> {

        @Override
        protected String doInBackground(byte[]... data) {
            FileOutputStream outStream = null;
            String res = null;
            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/.GoodDeeds");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);

                Bitmap img = BitmapFactory.decodeByteArray(data[0],0,data[0].length);

                img = Bitmap.createScaledBitmap(img, 400, 400, false);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                img = Bitmap.createBitmap(img , 0, 0, img .getWidth(), img .getHeight(), matrix, true);

                img.compress(Bitmap.CompressFormat.JPEG, 70, outStream);

                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

                res = uploadToCloudiono(outFile);
                //refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }

    }
    public static String uploadToCloudiono(File outFile) {
        String res = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(outFile);
            Cloudinary cloudinary = new Cloudinary("cloudinary://813966794176628:b6GUT3y0n8VQkfnTDMFx9O2hclA@mcvspace");
            Map UploadResult = cloudinary.uploader().upload(fileInputStream, ObjectUtils.emptyMap());
            res = (String)UploadResult.get("url");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {

        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
