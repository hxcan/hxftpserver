package com.stupidbeauty.farmingbookapp;

public interface OnDataChangeListener {
	
	public static final int NOTIFY_DATA_INERT = 0;
	public static final int NOTIFY_DATA_DELETE = 1;
	public static final int NOTIFY_DATA_UPDATE = 2;
	public static final int NOTIFY_DATA_FIND_END_ASY = 3;
	public static final int NOTIFY_CONTACT_SYN_FINISHED = 4;
	
	public void notifyDataChange(String table, int action);
}
