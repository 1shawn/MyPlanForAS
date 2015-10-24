package com.andwho.myplan.upgrade;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class CheckUpdate extends BmobObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 610199227247767436L;

	public String currentVersion;// 当前版本号
	public String newVersion;// 新版本号
	public String availableVersion;// 可用版本号
	public String description;// 更新内容
	public String isForced;// 是否强制升级 0否 1是
	public String path;// 安装包地址
	public String packageSize;// 安装包大小

}
