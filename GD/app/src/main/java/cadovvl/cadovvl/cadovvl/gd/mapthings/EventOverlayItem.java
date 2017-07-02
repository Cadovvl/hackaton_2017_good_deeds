package cadovvl.cadovvl.cadovvl.gd.mapthings;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.List;

import cadovvl.cadovvl.cadovvl.gd.Deed;
import cadovvl.cadovvl.cadovvl.gd.DeedConsumer;
import cadovvl.cadovvl.cadovvl.gd.Deeds;
import cadovvl.cadovvl.cadovvl.gd.DeedsConsumer;
import cadovvl.cadovvl.cadovvl.gd.R;
import cadovvl.cadovvl.cadovvl.gd.SearchParams;
import cadovvl.cadovvl.cadovvl.gd.StorageClient;
import cadovvl.cadovvl.cadovvl.gd.StorageClientImpl;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class EventOverlayItem extends OverlayItem {

    public enum Color {Red, Blue, Yellow, Green};

    private EventOverlayItem(GeoPoint geoPoint, Drawable drawable) {
        super(geoPoint, drawable);
    }

    public static class Builder {
        private GeoPoint mPoint;
        private Color mColor;
        private OnBalloonListener mListener;
        private String mText;

        public Builder setLocation(GeoPoint point) {
            mPoint = point;
            return this;
        }

        public Builder setColor(Color color) {
            mColor = color;
            return this;
        }

        public Builder setOnBalloonListener(OnBalloonListener listener) {
            mListener = listener;
            return this;
        }

        public Builder setText(String text) {
            mText = text;
            return this;
        }

        public OverlayItem build(Context context) {
            Drawable markeredPin = getMarkeredPin(context.getResources(), mColor);
            OverlayItem overlayItem = new EventOverlayItem(mPoint, markeredPin);
            BalloonItem balloon = new BalloonItem(context, overlayItem.getGeoPoint());

            if (mText != null) {
                balloon.setText(mText);
            }
            balloon.setOnBalloonListener(mListener);
            overlayItem.setBalloonItem(balloon);
            return overlayItem;
        }

        private Drawable getMarkeredPin(Resources resources, Color color) {
        /*Rect bounds = new Rect();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(android.graphics.Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(resources.getDimension(R.dimen.ame_default_cluster_text_size) );
        paint.setFakeBoldText(true);*/

            Bitmap bitmap = BitmapFactory.decodeResource(resources,
                    getPointMarker(color)).copy(Bitmap.Config.ARGB_8888, true);

        /*String textMarker = Integer.toString(id);
        paint.getTextBounds(textMarker, 0, textMarker.length(), bounds);
        float x = bitmap.getWidth() / 2.0f;
        float y = (bitmap.getHeight() - bounds.height()) / 4.0f - bounds.top;*/

        /*Canvas canvas = new Canvas(bitmap);
        canvas.drawText(textMarker, x, y, paint);*/
            BitmapDrawable bidra = new BitmapDrawable(resources, bitmap);
            return bidra;
        }

        private int getPointMarker(Color color){
            switch (color) {
                default:
                    return R.drawable.map_white;
                case Yellow:
                    return R.drawable.map_marker_yellow;
                case Red:
                    return R.drawable.map_marker_red;
                case Green:
                    return R.drawable.map_marker_green;
                case Blue:
                    return R.drawable.map_marker_blue;
            }
        }
    }
}