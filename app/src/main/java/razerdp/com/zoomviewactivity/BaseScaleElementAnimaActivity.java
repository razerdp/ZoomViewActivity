package razerdp.com.zoomviewactivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by 大灯泡 on 2016/9/2.
 *
 * 包含放大缩小过渡动画的activity
 */

public abstract class BaseScaleElementAnimaActivity<V extends ImageView> extends AppCompatActivity {

    protected V targetScaleAnimaedImageView;
    private String picUrl;
    private boolean needAnima;
    private AnimatorSet currentAnimator;
    private Point globalOffset;

    private Rect startRect;
    private Rect endRect;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initImageView();
    }

    private void initData() {
        picUrl = getIntent().getStringExtra("url");
        startRect = getIntent().getParcelableExtra("fromRect");
        needAnima = startRect != null && !TextUtils.isEmpty(picUrl);
        if (needAnima) {
            endRect = new Rect();
            globalOffset = new Point();
        }
    }

    private void initImageView() {
        targetScaleAnimaedImageView = getAnimaedImageView();
        needAnima = (needAnima && targetScaleAnimaedImageView != null);
        if (needAnima) {
            targetScaleAnimaedImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    //此时目标已经有了宽高信息
                    targetScaleAnimaedImageView.getGlobalVisibleRect(endRect, globalOffset);
                    playEnterAnima();
                    targetScaleAnimaedImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
            targetScaleAnimaedImageView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    playExitAnima();
                }
            });
        }
    }

    @Override protected void onResume() {
        super.onResume();
    }

    private void playEnterAnima() {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        onLoadingPicture(imageViewTarget, picUrl);

        startRect.offset(-globalOffset.x, -globalOffset.y);
        endRect.offset(-globalOffset.x, -globalOffset.y);

        float[] ratios = calculateRatios(startRect, endRect);

        targetScaleAnimaedImageView.setPivotX(0.5f);
        targetScaleAnimaedImageView.setPivotY(0.5f);
        final AnimatorSet enter = new AnimatorSet();
        enter.play(ObjectAnimator.ofFloat(targetScaleAnimaedImageView, View.X, startRect.left, endRect.left))
             .with(ObjectAnimator.ofFloat(targetScaleAnimaedImageView, View.Y, startRect.top, endRect.top))
             .with(ObjectAnimator.ofFloat(targetScaleAnimaedImageView, View.SCALE_X, ratios[0], 1f))
             .with(ObjectAnimator.ofFloat(targetScaleAnimaedImageView, View.SCALE_Y, ratios[1], 1f));

        enter.setDuration(400);
        enter.setInterpolator(new DecelerateInterpolator());
        enter.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                currentAnimator = enter;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        enter.start();
    }

    private void playExitAnima() {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        float[] ratios = calculateRatios(startRect, endRect);

        Log.i("startRect", "exit after offset:  >>>   " + startRect.toString());
        Log.d("endtRect", "exit after offset:  >>>   " + endRect.toString());
        int deltaHeight = (int) (endRect.top * ratios[1]);
        int deltaWidth = (int) (endRect.left * ratios[0]);

        targetScaleAnimaedImageView.setPivotX(0.5f);
        targetScaleAnimaedImageView.setPivotY(0.5f);
        final AnimatorSet exit = new AnimatorSet();

        exit.play(ObjectAnimator.ofFloat(targetScaleAnimaedImageView, View.X, startRect.left + deltaWidth))
            .with(ObjectAnimator.ofFloat(targetScaleAnimaedImageView, View.Y, startRect.top - deltaHeight))
            .with(ObjectAnimator.ofFloat(targetScaleAnimaedImageView, View.SCALE_X, ratios[0]))
            .with(ObjectAnimator.ofFloat(targetScaleAnimaedImageView, View.SCALE_Y, ratios[1]));

        exit.setDuration(400);
        exit.setInterpolator(new DecelerateInterpolator());
        exit.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                currentAnimator = exit;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        exit.start();
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    private SimpleTarget<GlideDrawable> imageViewTarget = new SimpleTarget<GlideDrawable>() {
        @Override public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            if (resource instanceof GlideBitmapDrawable) {
                targetScaleAnimaedImageView.setImageBitmap(((GlideBitmapDrawable) resource).getBitmap());
                ImageRect imageRect = new ImageRect(targetScaleAnimaedImageView);
                RectF rect = imageRect.getImageRect();
                endRect.set((int) rect.left, (int) rect.top, (int) rect.right, (int) rect.bottom);

                Log.d("imgrect", rect.toShortString());
            }
        }
    };

    protected abstract V getAnimaedImageView();

    protected abstract void onLoadingPicture(SimpleTarget targetImageView, String url);

    private float[] calculateRatios(Rect startBounds, Rect finalBounds) {
        float[] result = new float[2];
        float widthRatio = startBounds.width() * 1.0f / finalBounds.width() * 1.0f;
        float heightRatio = startBounds.height() * 1.0f / finalBounds.height() * 1.0f;
        result[0] = widthRatio;
        result[1] = heightRatio;
        return result;
    }

    public static void startWithScaleElementActivity(Activity from,
                                                     @Nullable String picUrl,
                                                     @Nullable Rect fromRect,
                                                     Class<? extends BaseScaleElementAnimaActivity> clazz) {
        Intent intent = new Intent(from, clazz);
        intent.putExtra("url", picUrl);
        intent.putExtra("fromRect", fromRect);
        from.startActivity(intent);
        //禁用过渡动画
        from.overridePendingTransition(0, 0);
    }
}

