package v4.common.types;

public class SingleFileManifestServerMessage extends ServerMessage {
	public FileManifest fileManifest;
	
	public SingleFileManifestServerMessage() {
		super.messageType = MessageType.GET_FILE_MANIFESTS;
	}
}
