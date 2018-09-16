package v4.common.types;

public class FileChunk {
	public int chunkNumber;
	public String chunk;
	
	public FileChunk(int chunkNumber, String chunk) {
		this.chunkNumber = chunkNumber;
		this.chunk = chunk;
	}
}
