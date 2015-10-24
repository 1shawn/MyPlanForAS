package com.andwho.myplan.model;

import java.io.Serializable;

public class Plan implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1655160779487163526L;
	public String planid;// 表键
	public String content;// 内容
	public String createtime;// 创建时间
	public String completetime;// 完成时间
	public String updatetime;// 更新时间
	public String iscompleted;// 是否完成
	public String isnotify;// 是否通知
	public String notifytime;// 通知时间
	public String plantype;// 计划类型
	public String isdeleted;// 是否已删除
}
