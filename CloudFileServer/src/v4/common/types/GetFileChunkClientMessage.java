package v4.common.types;

public class GetFileChunkClientMessage extends ClientMessage {
	public String fileName;
	public int chunkNumber;
	
	public GetFileChunkClientMessage(String fileName, int chunkNumber) {
		super.messageType = MessageType.GET_FILE_CHUNK;
		this.fileName = fileName;
		this.chunkNumber = chunkNumber;
	}
}
