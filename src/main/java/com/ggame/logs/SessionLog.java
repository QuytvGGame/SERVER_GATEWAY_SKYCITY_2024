package com.ggame.logs;

public class SessionLog extends PrLog {
	public long userid;
	public String username;
	public long goldOld;
	public long goldNew;
	public long coinOld;
	public long cointNew;
	public long diamondOld;
	public long diamondNew;
	public StringBuilder descriptionGold = new StringBuilder();
	public StringBuilder descriptionSilver = new StringBuilder();
	public StringBuilder descriptionPaddy = new StringBuilder();
	public String loginTime;
	public String logoutTitme;
	public int numMatch = 0;

	@Override
	public String toString() {
		strFileLog = new StringBuilder();
		strFileLog.append(userid);
		strFileLog.append(username);
		strFileLog.append(p);
		strFileLog.append(goldOld);
		strFileLog.append(p);
		strFileLog.append(goldNew);
		strFileLog.append(p);
		strFileLog.append(coinOld);
		strFileLog.append(p);
		strFileLog.append(cointNew);
		strFileLog.append(p);
		strFileLog.append(diamondOld);
		strFileLog.append(p);
		strFileLog.append(diamondNew);
		strFileLog.append(p);
		strFileLog.append(descriptionGold.toString());
		strFileLog.append(descriptionSilver.toString());
		strFileLog.append(descriptionPaddy.toString());
		strFileLog.append(p);
		strFileLog.append(loginTime);
		strFileLog.append(p);
		strFileLog.append(logoutTitme);
		return strFileLog.toString();
	}
}
