package com.ggame.entity.player;

public class DisplayerNameInfo {
	private long userd_id;
	private String displayer_name;
	
	public DisplayerNameInfo(long userd_id, String displayer_name) {
		super();
		this.userd_id = userd_id;
		this.displayer_name = displayer_name;
	}
	public DisplayerNameInfo() {
		// TODO Auto-generated constructor stub
	}
	public long getUserd_id() {
		return userd_id;
	}
	public void setUserd_id(long userd_id) {
		this.userd_id = userd_id;
	}
	public String getDisplayer_name() {
		return displayer_name;
	}
	public void setDisplayer_name(String displayer_name) {
		this.displayer_name = displayer_name;
	}
}
