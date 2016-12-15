package com.marno.mbasiclib.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.marno.mbasiclib.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by 李刚 on 2016/4/16/08:55.
 * Glide封装
 */
public final class GlideManager {

    private static int sCommonPlaceholder = R.drawable.shape_placeholder;

    private static int sRoundPlaceholder = sCommonPlaceholder;

    /**
     * 设置圆形图片占位图
     *
     * @param roundPlaceholder
     */
    public static void setRoundPlaceholder(int roundPlaceholder) {
        sRoundPlaceholder = roundPlaceholder;
    }

    /**
     * 设置通用占位图
     *
     * @param commonPlaceholder
     */
    public static void setCommonPlaceholder(int commonPlaceholder) {
        sCommonPlaceholder = commonPlaceholder;
    }

    /**
     * 加载网络图片
     *
     * @param obj
     * @param iv
     */
    public static void loadImg(Object obj, ImageView iv) {
        Context context = iv.getContext();
        Glide.with(context)
                .load(obj)
                .centerCrop()
                .dontAnimate()
                .placeholder(sCommonPlaceholder)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(iv);
    }


    public static void loadRoundImg(Object obj, ImageView iv) {
        Context context = iv.getContext();
        Glide.with(context)
                .load(obj)
                .centerCrop()
                .dontAnimate()
                .transform(new GlideCircleTransform(context))
                .placeholder(sRoundPlaceholder)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(iv);
    }

    /**
     * Glide加载圆形图片
     */
    private static class GlideCircleTransform extends BitmapTransformation {

        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.RGB_565);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
            }
            result = compressBitmap(result);

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        private static Bitmap compressBitmap(Bitmap source) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            source.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 100;

            while (baos.toByteArray().length / 1024 > 500) {
                baos.reset();//避免泄漏，每次清空
             source.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 10;
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            Bitmap result = BitmapFactory.decodeStream(isBm, null, null);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }
}


