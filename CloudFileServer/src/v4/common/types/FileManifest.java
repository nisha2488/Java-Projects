package v4.common.types;

import java.io.Serializable;

public class FileManifest implements Serializable {
	public String fileName;
	public int fileSize;
	public int numChunks;
}
