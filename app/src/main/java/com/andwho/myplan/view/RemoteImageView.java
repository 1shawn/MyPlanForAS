package com.andwho.myplan.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.andwho.myplan.utils.Log;
import com.andwho.myplan.utils.PhoneUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RemoteImageView extends ImageView {

	private static final String tag = "RemoteImageView";

	/*
	 * Cache-related fields and methods.
	 * 
	 * We use a hard and a soft cache. A soft reference cache is too
	 * aggressively cleared by the Garbage Collector.
	 */

	public static final String FILEDIR_STR = "scs/pic"; // SD��·��
	public static final String TMP_STR = "scs/scs_tmp";

	private File fileDir, tmpDir; //

	private Context mContext;

	public ImageLoader imageLoader = ImageLoader.getInstance();
	private static Builder memeryBuilder, disBuilder;
	private DisplayImageOptions options;

	public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public RemoteImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	public RemoteImageView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	private int level = 0;

	private int size;

	/**
	 * 设置图片级别与大小
	 * 
	 * @param level
	 * @param size
	 */
	public void setImageLevelSize(int level, int size) {
		this.level = level;
		this.size = size;
	}

	/**
	 * Sharable code between constructors
	 */
	private void init() {
		fileDir = getCacheImageFileDir(mContext, FILEDIR_STR);
		tmpDir = getCacheImageFileDir(mContext, TMP_STR);

		disBuilder = new DisplayImageOptions.Builder();

	}

	public static File getCacheImageFileDir(Context context, String path) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				&& Environment.getExternalStorageDirectory().exists()) {
			File desfileDir = new File(
					Environment.getExternalStorageDirectory(), path);
			if (!desfileDir.exists()) {
				desfileDir.mkdirs();
			}
			return desfileDir;
		} else {
			File desfileDir = new File(context.getCacheDir(), path);
			if (!desfileDir.exists()) {
				desfileDir.mkdirs();
			}
			return desfileDir;
		}
	}

	/**
	 * Remote image location
	 */
	// private String mLoadUrl;

	// public String getUrl()
	// {
	// return mLoadUrl;
	// }

	/**
	 * Position of the image in the mListView
	 */
	private int mPosition;

	/**
	 * ListView containg this image
	 */
	private ListView mListView;

	/**
	 * Default image shown while loading or on url not found
	 */
	private Integer mDefaultImage;

	private int quality = 5;

	/**
	 * Loads image from remote location
	 * 
	 * @param url
	 *            eg. http://random.com/abz.jpg
	 */
	// public void setImageUrl(String url, int level, int size) {
	// setImageUrl(url);
	// }

	int w;

	String mUrl;

	public String getSmallImageUrl(String url) {
		quality = 40;
		// return url + "?w=" + w + "&quality=" + 40;
		return url;
	}

	public String getBigImageUrl(String url) {
		quality = 100;
		// return url + "?w=" + w + "&quality=" + 100;
		return url;
	}

	public void cancelDisplayTask() {
		imageLoader.cancelDisplayTask(this);
	}

	// public void setBigImage(String url) {
	// mUrl = url;
	//
	// if (TextUtils.isEmpty(url)) {
	// return;
	// }
	//
	// try {
	// options = disBuilder.cacheOnDisc().cacheInMemory()
	// .showImageForEmptyUri(mDefaultImage)
	// .showStubImage(mDefaultImage).build();
	//
	// // 判断原图是否存在
	// Log.e(tag, "@@...sp...load image...big :" + getBigImageUrl(url));
	// if (!imageLoader.getDiscCache().get(getBigImageUrl(url)).exists()) {
	// imageLoader.displayImage(getBigImageUrl(url), this, options,
	// null);
	//
	// } else {
	// imageLoader.displayImage(getBigImageUrl(url), this, options,
	// null);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * Loads image from remote location
	 * 
	 * @param url
	 *            eg. http://random.com/abz.jpg
	 * 
	 *            sufix: w=180&quality=100_180x240
	 */
	public void setImageUrl(String url) {

		setBigImageUrl(url, null);
		// mUrl = url;
		//
		// if (TextUtils.isEmpty(url)) {
		// return;
		// }
		//
		// try {
		//
		// String sid = Properties.getSid();
		// Log.e(tag, "@@...scs...setBigImageUrl...sid = " + sid);
		// if (!TextUtils.isEmpty(sid)) {
		// ImageLoaderPreference.getInstance(mContext).saveSid(sid);
		// }
		//
		// options = disBuilder.cacheOnDisc().cacheInMemory()
		// .showImageForEmptyUri(mDefaultImage)
		// .showStubImage(mDefaultImage)// .setSid(Properties.getSid())
		// .build();
		//
		// // 判断原图是否存在
		// if (!imageLoader.getDiscCache().get(getBigImageUrl(url)).exists()) {
		// // 判断小图是否存在
		//
		// imageLoader.displayImage(getSmallImageUrl(url), this, options,
		// null);
		//
		// } else {
		// imageLoader.displayImage(getBigImageUrl(url), this, options,
		// null);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public void setBigImageUrl(String url, ImageLoadingListener lsnr) {

		mUrl = url;

		if (TextUtils.isEmpty(url)) {
			return;
		}

		try {

			options = disBuilder.cacheOnDisc().cacheInMemory()
					.showImageForEmptyUri(mDefaultImage)
					.showStubImage(mDefaultImage)
					.build();

			// 判断原图是否存在
			if (!imageLoader.getDiscCache().get(getBigImageUrl(url)).exists()) {
				imageLoader.displayImage(getBigImageUrl(url), this, options,
						lsnr);
			} else {
				imageLoader.displayImage(getBigImageUrl(url), this, options,
						lsnr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCustomQualityUrl(String url, int q) {
		if (q < 10) {
			q = 10;
		} else if (q > 100) {
			q = 100;
		}
		quality = q;
		return url + "?w=" + w + "&quality=" + q;
	}

	// public void setImageUrl(String url, int quality) {
	//
	// mUrl = url;
	//
	// if (TextUtils.isEmpty(url)) {
	// return;
	// }
	//
	// try {
	// options = disBuilder.cacheOnDisc().cacheInMemory()
	// .showImageForEmptyUri(mDefaultImage)
	// .showStubImage(mDefaultImage).build();
	//
	// Log.d(tag, "@@...yxt...load image...custom quality:"
	// + getCustomQualityUrl(url, quality));
	//
	// imageLoader.displayImage(getCustomQualityUrl(url, quality), this,
	// options, null);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * Loads image from remote location
	 * 
	 * @param url
	 *            eg. http://random.com/abz.jpg
	 */
	public void setImageUrl(String url, ImageLoadingListener lsnr) {
		mUrl = url;
		// 使用测试地址
		// url = url.replaceAll("http://www.189mv.cn:80",
		// "http://113.108.186.147:8026");
		if (TextUtils.isEmpty(url)) {
			return;
		}

		try {

			options = disBuilder.cacheOnDisc().cacheInMemory()
					.showImageForEmptyUri(mDefaultImage)
					.showStubImage(mDefaultImage)// .setSid(Properties.getSid())
					.build();

			imageLoader.displayImage(mUrl, this, options, lsnr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	SimpleImageLoadingListener simpleImageLoadingListener = new SimpleImageLoadingListener() {
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub
			super.onLoadingStarted(imageUri, view);

		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub
			super.onLoadingComplete(imageUri, view, loadedImage);
			Log.w("SimpleImageLoader",
					"@@...yxt...load image...simple load complete!"
							+ quality
							+ "-"
							+ imageLoader
									.getLoadingUriForView(RemoteImageView.this));

			RemoteImageView.this.setImageBitmap(loadedImage);

		}

	};

	ImageLoadingListener listener = new ImageLoadingListener() {

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			Log.w("RemoteImageView", "@@...yxt...remote on load start!");
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			Log.w("RemoteImageView",
					"@@...yxt...remote on load onLoadingFailed!");

		}

		@Override
		public void onLoadingComplete(String imageUri, final View imageView,
				Bitmap loadedImage) {
			Log.d("RemoteImageView",
					"@@...yxt...load image..on Loading Complete! quality = "
							+ quality);
			if (quality < 100) {
				quality = 100;
				imageLoader.loadImage(getBigImageUrl(mUrl), options,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// TODO Auto-generated method stub
								// PhoneUtils.javaToast.makeText(mContext, "加载失败", 1000).show();
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, final Bitmap loadedImage) {
								Log.d("RemoteView",
										"@@...yxt...load image... complete! ");
								RemoteImageView.this
										.setImageBitmap(loadedImage);
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}
						});
			}
			// File file = imageLoader
			// .getDiscCache()
			// .get(mUrl
			// + "?w=600&quality="
			// + quality);
			// if (file != null
			// && file.exists())
			// {
			// file.delete();
			//
			// Log.w("RemoteImageView",
			// "@@...yxt...删除小图!");
			// }

		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			Log.w("RemoteImageView",
					"@@...yxt...remote on load onLoadingCancelled!");

		}
	};

	/**
	 * Sets default local image shown when remote one is unavailable
	 * 
	 * @param resid
	 */
	public void setDefaultImage(Integer resid) {

		mDefaultImage = resid;
		setImageResource(resid);

		w = this.getLayoutParams().width;
		if (w <= 0) {
			w = PhoneUtils.getResolution(mContext)[0];
		}

	}

	/**
	 * Loads image from remote location in the ListView
	 * 
	 */
	// public void setImageUrl(String url, int position, ListView listView,
	// int level, int size) {
	// mPosition = position;
	// mListView = listView;
	//
	// setImageUrl(url, level, size);
	// }

	@Override
	public boolean isShown() {
		// TODO Auto-generated method stub
		return super.isShown();
	}

	// private Bitmap decodeImage(String path, int size) {
	//
	// Bitmap bmp = null;
	// try {
	// Options opts = new Options();
	// // opts.inJustDecodeBounds = true;
	// // Bitmap bmp = BitmapFactory.decodeFile(path, opts);
	//
	// opts.inSampleSize = size;
	//
	// opts.inJustDecodeBounds = false;
	// opts.inPreferredConfig = Bitmap.Config.RGB_565;
	// // bmp = BitmapFactory.decodeFile(path, opts);
	// // bmp = BitmapFactory.decodeFile(path, opts);
	//
	// InputStream is = new FileInputStream(path);
	// byte[] bt = getBytes(is);
	// bmp = BitmapFactory.decodeByteArray(bt, 0, bt.length, opts);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (OutOfMemoryError e) {
	// e.printStackTrace();
	// }
	//
	// return bmp;
	// }

	// private byte[] getBytes(InputStream is) throws IOException {
	//
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// byte[] b = new byte[1024];
	// int len = 0;
	//
	// while ((len = is.read(b, 0, 1024)) != -1) {
	// baos.write(b, 0, len);
	// baos.flush();
	// }
	// byte[] bytes = baos.toByteArray();
	// return bytes;
	// }

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */
	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	// private String saveBitmap(String url,
	// InputStream inputStream)
	// {
	// File file = null;
	// try
	// {
	// if (level > 0)
	// {
	// file = new File(tmpDir, MD5.crypt(url));
	// } else
	// {
	// file = new File(fileDir, MD5.crypt(url));
	// }
	//
	// // test
	// if (file.exists())
	// return file.getAbsolutePath();
	//
	// FileOutputStream fileOutputStream = new FileOutputStream(
	// file);
	// int len = 0;
	// byte[] buffer = new byte[1024];
	// while ((len = inputStream.read(buffer)) != -1)
	// {
	// fileOutputStream.write(buffer, 0, len);
	// }
	// inputStream.close();
	// fileOutputStream.close();
	// } catch (Exception e)
	// {
	// file.delete();
	// e.printStackTrace();
	// return null;
	// }
	// return file.getAbsolutePath();
	// }

	public static void deleteCache(Context context, int level) {
		String path;
		if (level > 0) {
			path = TMP_STR;
			File fileDir = getCacheImageFileDir(context, path);
			for (File file : fileDir.listFiles()) {
				// Log.i("t", "fffffffffffffffff");
				if (file.exists()) {
					file.delete();
				}
			}
		} else {
			path = FILEDIR_STR;
			File fileDir = getCacheImageFileDir(context, path);

			for (File file : fileDir.listFiles()) {
				// Log.i("t", "fffffffffffffffff");
				if (file.exists()) {
					Log.w("RemoteView",
							"@@...yxt...remoteivew film:" + file.length()
									/ 1000);
					file.delete();
				}
			}

			if (fileDir.exists()) {
				fileDir.delete();
			}

		}
	}

	// 返回缓存大小单位:M
	public static float getCacheSize(Context context, int level) {
		float sum = 0;
		String path;
		if (level > 0) {
			path = TMP_STR;

		} else {
			path = FILEDIR_STR;

		}

		File file = getCacheImageFileDir(context, path);

		for (File file2 : file.listFiles()) {
			if (file2.isFile()) {
				sum += file2.length();
			}

		}

		return sum / (1024 * 1024);
	}

	public void recyleBm() {
		// Bitmap bitmap = RemoteImageView.this.getDrawingCache();
		// Log.w("RemoteView", "@@...yxt...get bitmap!" + bitmap);
		// if (bitmap != null && !bitmap.isRecycled()) {
		// Log.w("RemoteView", "@@...yxt...bitmap.recycle()!");
		// bitmap.recycle();
		// }

		// Drawable drawable = this.getDrawable();
		// Log.w("RemoteView", "@@...yxt...get drawable!"
		// + drawable);
		// if (drawable instanceof BitmapDrawable)
		// {
		// BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		// Bitmap bitmap = bitmapDrawable.getBitmap();
		//
		// if (bitmap != null && !bitmap.isRecycled())
		// {
		// Log.w("RemoteView",
		// "@@...yxt...bitmap.recycle()!");
		// // bitmap.recycle();
		// bitmap = null;
		// }
		//
		// }

	}

}
