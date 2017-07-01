package cadovvl.cadovvl.cadovvl.gd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import cadovvl.cadovvl.cadovvl.gd.mapthings.EventOverlayItem;
import cadovvl.cadovvl.cadovvl.gd.mapthings.NewPointOverlay;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.utils.GeoPoint;

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


        initMap();

    }

    private void initMap() {
        final MapView mapView = (MapView) findViewById(R.id.map);

        mMap = mapView;
        mMapController = mMap.getMapController();
        mOverlayManager = mMapController.getOverlayManager();

        mapView.showBuiltInScreenButtons(true);

        initCampaignsOverlay();
        initNewPointOverlay();

    }

    private void initNewPointOverlay() {
        mNewPointLayer = new NewPointOverlay(this, mMapController);
        mOverlayManager.addOverlay(mNewPointLayer);
    }

    private void initCampaignsOverlay() {
        mCampaignsLayer = new Overlay(mMapController);
        mOverlayManager.addOverlay(mCampaignsLayer);
    }

    @Override
    protected void onResume() {
        loadCampaignsOverlay(mCampaignsLayer);
        super.onResume();
    }

    private void loadCampaignsOverlay(final Overlay overlay) {
        overlay.clearOverlayItems();

        final EventOverlayItem.Builder builder = new EventOverlayItem.Builder();
        StorageClient client = new StorageClientImpl();
        client.find(new SearchParams()
                        .setLat(56.00)
                        .setLon(44.00)
                        .setR(5000000.0)
                ,
                new DeedsConsumer() {
                    @Override
                    public void consume(Deeds deeds) {
                        for (final Deed d: deeds.values()) {
                            builder.setColor(EventOverlayItem.Color.Blue)
                                    .setLocation( new GeoPoint(d.getPos().getLat(), d.getPos().getLon()) );
                            overlay.addOverlayItem(builder.build(MainActivity.this));

                        }
                    }
                }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
