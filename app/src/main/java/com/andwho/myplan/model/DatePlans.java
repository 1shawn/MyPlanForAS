package com.andwho.myplan.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class DatePlans implements Serializable, Comparable<DatePlans> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6065874701142274647L;
	public String date;
	public ArrayList<Plan> plans;

	public DatePlans(String date, ArrayList<Plan> plans) {
		this.date = date;
		this.plans = plans;
	}

	@Override
	public int compareTo(DatePlans another) {
		// TODO Auto-generated method stub
		// 这个倒过来可以控制顺序
		return another.date.compareTo(this.date);
	}

}
