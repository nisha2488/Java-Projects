package v3.common;

import java.io.Serializable;

public class ClientMessage implements Serializable {
	private static final long serialVersionUID = -7213278526188231853L;
	
	public String messageType;
	public String clientID;
}