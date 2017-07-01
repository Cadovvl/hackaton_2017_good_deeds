package cadovvl.cadovvl.cadovvl.gd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.io.StringWriter;

import cadovvl.cadovvl.cadovvl.gd.mapthings.EventOverlayItem;
import cadovvl.cadovvl.cadovvl.gd.mapthings.NewPointOverlay;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;

public class MainActivity extends AppCompatActivity {

    private final static StorageClient storageClient = new StorageClientImpl();
    private final static ObjectMapper mapper = new ObjectMapper();

    private MapController mMapController;
    private OverlayManager mOverlayManager;
    private MapView mMap;
    private NewPointOverlay mNewPointLayer;
    private Overlay mCampaignsLayer;
    private static final int PERMISSIONS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent intent = new Intent(this, CompanyPoster.class);

        startActivityForResult(intent, 123);


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

        initNewPointOverlay();
        loadCampaignsOverlay();
    }

    private void initNewPointOverlay() {
        mNewPointLayer = new NewPointOverlay(this, mMapController);
        mOverlayManager.addOverlay(mNewPointLayer);
    }

    private void loadCampaignsOverlay() {
        mCampaignsLayer = new Overlay(mMapController);
        try {
            EventOverlayItem.placeItemsTest(mCampaignsLayer, this);
        } catch (Exception e) {
            Log.d("TAG", e.getLocalizedMessage());
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
