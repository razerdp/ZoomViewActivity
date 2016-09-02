package razerdp.com.zoomviewactivity;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

/**
 * Created by 大灯泡 on 2016/9/2.
 */

public class ImageRect {
    private RectF rect;

    public ImageRect(ImageView imageview) {
        rect = new RectF();
        if (imageview != null) {
            Rect drawableRect = imageview.getDrawable().getBounds();
            Matrix imgMatrix = imageview.getImageMatrix();
            float[] matrixValues = new float[9];
            imgMatrix.getValues(matrixValues);
            rect.left = matrixValues[2];
            rect.top = matrixValues[5];
            rect.right = rect.left + drawableRect.width() * matrixValues[0];
            rect.bottom = rect.top + drawableRect.height() * matrixValues[0];
        }
    }

    public RectF getImageRect() {
        return rect;
    }
}
