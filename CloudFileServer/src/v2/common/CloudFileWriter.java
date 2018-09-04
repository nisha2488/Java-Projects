package v2.common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CloudFileWriter {
	public static void main(String ...args) {
		CloudFileReader cfr = new CloudFileReader();
		CloudFileWriter cfw = new CloudFileWriter();
		try {
			String content = cfr.readFileContents(CloudFileReader.FILE_DIR + "sampleHTML.html");
			cfw.writeFileContents("sampleOutput.txt", content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void writeFileContents(String fileName, String content) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(CloudFileReader.FILE_DIR + fileName));
		bw.write(content);
		bw.close();
	}
	
}
