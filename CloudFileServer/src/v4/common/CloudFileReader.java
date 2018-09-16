package v4.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CloudFileReader {
	
	public static final String FILE_DIR = "/home/nisha/Documents/Personal Git Repo/Java-Projects/CloudFileServer/src/files/";
	public static final int CHUNK_SIZE = 1024;
	
	public static void main(String ...args) {
		CloudFileReader clf = new CloudFileReader();
		try {
			String output = clf.readFileChunk(FILE_DIR + "sampleHTML.html", 4);
//			String output = clf.readFileContents(FILE_DIR + "sampleHTML.html");
			System.out.println(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String readFileContents(String fileName) throws IOException {
		File file = new File(fileName);
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder fileContent = new StringBuilder();
		String line;
		while ((line = br.readLine())!= null) {
			fileContent.append(line);
		}
		br.close();
		return fileContent.toString();
	}
	
	public String readFileChunk(String fileName, int chunkNumber) throws Exception {
		byte[] chunk = new byte[CHUNK_SIZE];
		RandomAccessFile raf = new RandomAccessFile(fileName, "r");
		if(raf.length()<=chunkNumber*CHUNK_SIZE) {
			raf.close();
			throw new Exception("Required chunk number "+chunkNumber+" does not exist in "+fileName);
		}
		raf.seek(chunkNumber*CHUNK_SIZE);
		raf.read(chunk);
		raf.close();
		return new String(chunk);
	}
}
