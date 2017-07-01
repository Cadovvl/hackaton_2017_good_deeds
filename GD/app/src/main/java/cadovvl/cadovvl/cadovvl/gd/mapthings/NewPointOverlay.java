package cadovvl.cadovvl.cadovvl.gd.mapthings;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.map.GeoCode;
import ru.yandex.yandexmapkit.map.GeoCodeListener;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import ru.yandex.yandexmapkit.utils.ScreenPoint;

/**
 * Stolen somewhere on 01.07.2017.
 */

public class NewPointOverlay extends Overlay /*implements GeoCodeListener*/
{
    private Activity mContext;

    public NewPointOverlay(Activity context, MapController mapController )
    {
        super( mapController );
        mContext = context;
    }

    @Override
    public boolean onSingleTapUp( float x, float y )
    {
        GeoPoint point = getMapController().getGeoPoint( new ScreenPoint(x, y));
        final String message = String.format("Let's create a good deed at [lat=%f and lon %f]", point.getLat(), point.getLat());
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        });
        ;
        /*getMapController().getDownloader().getGeoCode(
                this,
                getMapController().getGeoPoint( new ScreenPoint(x, y) )
        );*/
        return true;
    }

    /*@Override
    public boolean onFinishGeoCode( final GeoCode geoCode )
    {
            if ( geoCode != null )
            {
                getMapController().getMapView().post(
                        () ->
                        {
                            CommentDialog dialog = new CommentDialog();
                            dialog.setListener(
                                    //() -> Toast.makeText( getContext(), dialog.getComment(), Toast.LENGTH_LONG ).show()
                                    () ->
                                    {
                                        MapPoint newPoint = newMapPoint( geoCode, dialog.getComment() );
                                        MapPointBalloonItem newBalloon = new MapPointBalloonItem(
                                                getContext(), geoCode.getGeoPoint()
                                        );
                                        newBalloon.setIndex( newPoint.getIndex() );
                                        newBalloon.setComment( newPoint.getComment() );
                                        OverlayItem newOverlayItem = newOverlayItem( newBalloon );

                                        balloons.addOverlayItem( newOverlayItem );
                                        balloons.getMapController().showBalloon( newBalloon );

                                        mapPointsPresenter.add( newPoint );
                                        mapPointsPresenter.sort(); // optional sorting to keep up with spinner reason
                                        balloonsByPoints.put( newPoint, newBalloon );
                                    }
                            );
                            dialog.show( getChildFragmentManager(), null );
                        }
                );
            }
        return true;
    }*/

    @Override
    public int compareTo( @NonNull Object o )
    {
        return 0;
    }
}
