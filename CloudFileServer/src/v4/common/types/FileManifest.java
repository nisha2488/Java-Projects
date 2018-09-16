package v4.common.types;

import java.io.Serializable;

import v4.common.CloudFileReader;

public class FileManifest implements Serializable {
	public String fileName;
	public int fileSize;
	public int numChunks;
	
	public FileManifest(String fileName, int fileSize) {
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.numChunks = (int) Math.ceil((fileSize * 1.0/(CloudFileReader.CHUNK_SIZE)));
	}
}
