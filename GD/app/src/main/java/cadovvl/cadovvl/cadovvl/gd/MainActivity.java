package cadovvl.cadovvl.cadovvl.gd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import cadovvl.cadovvl.cadovvl.gd.mapthings.EventOverlayItem;
import cadovvl.cadovvl.cadovvl.gd.mapthings.NewPointOverlay;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
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

    private void processException(Exception e) {
        final String message = String.format("Something really bad happens: %s", e.getMessage());
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


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
        // add listener
        //mMapController.getOverlayManager().getMyLocation().addMyLocationListener(this);

        initNewPointOverlay();
        initCampaignsOverlay();
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
        try {
            loadCampaignsOverlay(mCampaignsLayer);
        } catch (Exception e) {
            processException(e);
        }
            super.onResume();
    }

    private final EventOverlayItem.Color getColorByStatus(final Deed.Status status) {
        if (status.equals(Deed.Status.created)) {
            return EventOverlayItem.Color.Red;
        } else if (status.equals(Deed.Status.processing)) {
            return EventOverlayItem.Color.Blue;
        } else if (status.equals(Deed.Status.done)) {
            return EventOverlayItem.Color.Green;
        }
        return EventOverlayItem.Color.Yellow;
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
                            builder.setColor(getColorByStatus(d.getStatus()))
                                    .setLocation( new GeoPoint(d.getPos().getLat(), d.getPos().getLon()));
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
