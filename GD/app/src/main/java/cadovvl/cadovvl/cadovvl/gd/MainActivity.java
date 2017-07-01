package cadovvl.cadovvl.cadovvl.gd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.io.StringWriter;

import cadovvl.cadovvl.cadovvl.gd.mapthings.NewPointOverlay;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;

import ru.yandex.yandexmapkit.MapView;

public class MainActivity extends AppCompatActivity {

    private final static StorageClient storageClient = new StorageClientImpl();
    private final static ObjectMapper mapper = new ObjectMapper();

    private MapController mMapController;
    private OverlayManager mOverlayManager;
    private MapView mMap;
    private NewPointOverlay mNewPointLayer;
    private static final int PERMISSIONS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View cameraView = findViewById(R.id.shot);

        Intent intent = new Intent(this, CompanyPoster.class);

        startActivityForResult(intent, 123);

        cameraView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(view.getContext(), CameraActivity.class);
                startActivity(cameraIntent);
            }
        }));

        initMap();

    }

    private void initMap() {
        final MapView mapView = (MapView) findViewById(R.id.map);

        mMap = mapView;
        mMapController = mMap.getMapController();
        mOverlayManager = mMapController.getOverlayManager();

        mapView.showBuiltInScreenButtons(true);
        // add listener
        //mMapController.getOverlayManager().getMyLocation().addMyLocationListener(this);

        loadPointsOverlay();
    }

    private void loadPointsOverlay() {
        mNewPointLayer = new NewPointOverlay(this, mMapController);
        mOverlayManager.addOverlay(mNewPointLayer);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
