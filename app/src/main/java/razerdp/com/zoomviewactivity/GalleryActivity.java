package razerdp.com.zoomviewactivity;

import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by 大灯泡 on 2016/9/2.
 */

public class GalleryActivity extends BaseScaleElementAnimaActivity<ImageView>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }

    @Override protected ImageView getAnimaedImageView() {
        return (ImageView) findViewById(R.id.large_imageview);
    }

    @Override protected void onLoadingPicture(SimpleTarget targetImageView, String url) {
        Glide.with(this).load(url).into(targetImageView);
    }
}
