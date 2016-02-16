package com.andwho.myplan.model;

import java.io.Serializable;

public class Plan implements Serializable {
	public String planid;// 表键
	public String content;// 内容
	public String createtime;// 创建时间
	public String completetime;// 完成时间
	public String updatetime;// 更新时间
	public String iscompleted;// 是否完成 1是，0否
	public String isnotify;// 是否通知  1是，0否
	public String notifytime;// 通知时间
	public String plantype;// 计划类型 1每日计划，0长远计划
	public String isdeleted;// 是否已删除
}
