package razerdp.com.zoomviewactivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    public final static String[] imageThumbUrls = new String[] {
        "http://img0.imgtn.bdimg.com/it/u=174933758,4215677132&fm=21&gp=0.jpg",
        "http://img3.imgtn.bdimg.com/it/u=2489272570,1320005951&fm=21&gp=0.jpg",
        "http://img4.imgtn.bdimg.com/it/u=886577445,4037697725&fm=21&gp=0.jpg",
        "http://img1.imgtn.bdimg.com/it/u=223451437,1717713198&fm=21&gp=0.jpg",
        "http://img1.imgtn.bdimg.com/it/u=1291991673,1011525160&fm=21&gp=0.jpg",
        "http://img5.imgtn.bdimg.com/it/u=3109169726,2912642473&fm=21&gp=0.jpg",
        "http://img0.imgtn.bdimg.com/it/u=1570388022,3818490538&fm=21&gp=0.jpg",
        "http://img3.imgtn.bdimg.com/it/u=995253922,519230807&fm=21&gp=0.jpg",
        "http://img1.imgtn.bdimg.com/it/u=2196321947,3072185274&fm=21&gp=0.jpg",
        "http://img1.imgtn.bdimg.com/it/u=3241888575,3721340726&fm=21&gp=0.jpg",
        "http://img4.imgtn.bdimg.com/it/u=3989728472,2285970198&fm=21&gp=0.jpg",
        "http://img3.imgtn.bdimg.com/it/u=3345569329,251003876&fm=21&gp=0.jpg",
        "http://img1.imgtn.bdimg.com/it/u=1693679285,1236253130&fm=21&gp=0.jpg",
        "http://img4.imgtn.bdimg.com/it/u=548380399,1633182297&fm=21&gp=0.jpg",
        "http://img3.imgtn.bdimg.com/it/u=4048514484,4278139063&fm=21&gp=0.jpg",
        "http://img3.imgtn.bdimg.com/it/u=1671943955,3320345726&fm=21&gp=0.jpg",
        "http://img5.imgtn.bdimg.com/it/u=1209254764,1138673101&fm=21&gp=0.jpg",
        "http://img3.imgtn.bdimg.com/it/u=1675837036,1868624213&fm=21&gp=0.jpg",
        "http://img2.imgtn.bdimg.com/it/u=1155180725,1896054369&fm=21&gp=0.jpg",
        "http://img3.imgtn.bdimg.com/it/u=2273157982,216262949&fm=21&gp=0.jpg",
        "http://img0.imgtn.bdimg.com/it/u=3765951855,4291926133&fm=21&gp=0.jpg",
        "http://img1.imgtn.bdimg.com/it/u=3024454297,685457010&fm=21&gp=0.jpg",
        "http://img5.imgtn.bdimg.com/it/u=1490459227,3201555812&fm=21&gp=0.jpg",
        "http://img4.imgtn.bdimg.com/it/u=2333591480,3865559244&fm=21&gp=0.jpg",
        "http://img3.imgtn.bdimg.com/it/u=1928647624,2441315433&fm=21&gp=0.jpg",
        "http://img1.imgtn.bdimg.com/it/u=3548933321,1229645136&fm=21&gp=0.jpg",
        "http://img0.imgtn.bdimg.com/it/u=1977596414,2408269982&fm=21&gp=0.jpg",
        "http://img2.imgtn.bdimg.com/it/u=3595906845,2921183295&fm=21&gp=0.jpg",
        "http://img2.imgtn.bdimg.com/it/u=2723778700,4131168062&fm=21&gp=0.jpg",
        "http://img5.imgtn.bdimg.com/it/u=2397618454,1789270489&fm=21&gp=0.jpg",
        "http://img3.imgtn.bdimg.com/it/u=491049708,1747659465&fm=21&gp=0.jpg",
        "http://img1.imgtn.bdimg.com/it/u=630621970,2875108731&fm=21&gp=0.jpg",
        "http://img0.imgtn.bdimg.com/it/u=2284368585,3465236287&fm=21&gp=0.jpg",
        "http://img3.imgtn.bdimg.com/it/u=164449162,2080920875&fm=21&gp=0.jpg",
        "http://img5.imgtn.bdimg.com/it/u=3734366599,2068439888&fm=21&gp=0.jpg"
    };
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(new InnerAdapter());
    }

    class InnerAdapter extends BaseAdapter {

        @Override public int getCount() {
            return imageThumbUrls.length;
        }

        @Override public String getItem(int position) {
            return imageThumbUrls[position];
        }

        @Override public long getItemId(int position) {
            return position;
        }

        @Override public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_imageview, parent, false);
                TextView title = (TextView) convertView.findViewById(R.id.position);
                ImageView img = (ImageView) convertView.findViewById(R.id.img);
                vh = new ViewHolder(title, img);
                convertView.setTag(R.id.vh, vh);
            } else {
                vh = (ViewHolder) convertView.getTag(R.id.vh);
            }
            vh.title.setText(String.valueOf(position));
            Glide.with(MainActivity.this).load(getItem(position)).into(vh.img);
            final ViewHolder finalVh = vh;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Rect rect = new Rect();
                    finalVh.img.getGlobalVisibleRect(rect);
                    BaseScaleElementAnimaActivity.startWithScaleElementActivity(
                        MainActivity.this,
                        imageThumbUrls[position],
                        rect,
                        GalleryActivity.class
                    );
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView title;
            public ImageView img;

            public ViewHolder(TextView title, ImageView img) {
                this.title = title;
                this.img = img;
                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) img.getLayoutParams();
                params.width= (int) (params.width+(Math.random()*150));
                params.height= (int) (params.height+(Math.random()*250));
                img.setLayoutParams(params);

            }
        }
    }
}
