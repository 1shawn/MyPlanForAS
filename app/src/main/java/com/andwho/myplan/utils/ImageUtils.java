package com.andwho.myplan.utils;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by ys_1shawn on 2016/2/14.
 */
public class ImageUtils {
    public static void initImageLoader(Context mContext) {

        // @SuppressWarnings("deprecation")
        // ImageLoaderConfiguration config = new
        // ImageLoaderConfiguration.Builder(
        // context).threadPoolSize(5)
        // .threadPriority(Thread.NORM_PRIORITY - 1)
        // .tasksProcessingOrder(QueueProcessingType.LIFO)
        // .denyCacheImageMultipleSizesInMemory()
        // .memoryCache(new LruMemoryCache(1024 * 1024))
        // .memoryCacheSize(1 * 1024 * 1024)
        // .discCacheSize(20 * 1024 * 1024).discCacheFileCount(400)
        // .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) //
        // default
        // .imageDownloader(new BaseImageDownloader(context)) // default
        // .imageDecoder(new BaseImageDecoder(false)).build();//
        // ImageLoader.getInstance().init(config);

        try {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    mContext.getApplicationContext())
                    // .offOutOfMemoryHandling()
                    .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                            // 1.5 Mb
                    .discCacheFileNameGenerator(new Md5FileNameGenerator())
                            // .diskCache(
                            // new TotalSizeLimitedDiscCache(RemoteImageView
                            // .getCacheImageFileDir(mContext,
                            // RemoteImageView.FILEDIR_STR),
                            // 10000000)) // 10000000
                    .memoryCache(new FIFOLimitedMemoryCache(5000000)) // 5000000
                            //.writeDebugLogs() // Not
                    .build();

            // Initialize ImageLoader with configuration.
            ImageLoader.getInstance().init(config);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
