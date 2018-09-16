package v3.common.types;

import java.io.Serializable;

public class ClientMessage implements Serializable {
	public MessageType messageType;
	public String clientID;
}