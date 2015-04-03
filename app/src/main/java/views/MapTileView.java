package views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.m1027519.textroleplayinggame.R;


/**
 * TODO: document your custom view class.
 */
public class MapTileView extends View {
    private Context mContext;
    private Bitmap tile;
    public MapTileView(Context context) {
        super(context);
        mContext = context;
        tile = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.minimap_container_bkgrnd);
    }

    public MapTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapTileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        canvas.drawRect(new RectF(0,0,50,50),paint);
    }


}
