package com.ggame.logs;

public class PingFailException extends Throwable {

	private static final long serialVersionUID = -1204122998667381920L;

	@Override
	public String getMessage() {
		String msg = "Can't connect to client! May be socket was hard disconected!";
		return msg;
	}

}
