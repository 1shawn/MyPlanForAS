package com.andwho.myplan.upgrade;

 
/**
 * 下载文件时回调接口，通知下载进行时的信息
 */
public interface DownloadCallback {

	/**
	 * 通知下载进行时的信息
	 * @param progress 下载进度
	 * @param downloadSize 目前已下载的大小
	 * @param total 下载文件的总大小
	 * @return 是否取消下载
	 */ 
	public boolean onProgressChanged(int progress, int downloadSize, int total);
}
