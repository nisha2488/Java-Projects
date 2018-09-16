package v4.common.types;

import java.io.Serializable;

public class ServerMessage implements Serializable {
	public MessageType messageType;
	public String message;
	public boolean hasError;
}