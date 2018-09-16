package v4.common.types;

import java.util.ArrayList;
import java.util.List;

public class FileListServerMessage extends ServerMessage {
	public List<FileManifest> fileManifestList;
	
	public FileListServerMessage() {
		this.fileManifestList = new ArrayList<FileManifest>();
	}
}
