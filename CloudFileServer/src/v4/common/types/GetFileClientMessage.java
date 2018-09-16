package v4.common.types;

public class GetFileClientMessage extends ClientMessage {
	public String fileName;
	
	public GetFileClientMessage(String fileName) {
		super.messageType = MessageType.GET_FILE;
		this.fileName = fileName;
	}
}
