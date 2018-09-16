package v4.common.types;

public class GetSingleFileManifestClientMessage extends ClientMessage {
	public String fileName;
	
	public GetSingleFileManifestClientMessage(String fileName) {
		super.messageType = MessageType.GET_SINGLE_FILE_MANIFEST;
		this.fileName = fileName;
	}
}
