package v2.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CloudFileReader {
	
	public static final String FILE_DIR = "/home/nisha/Documents/Personal Git Repo/Java-Projects/CloudFileServer/src/files/";
	
	public static void main(String ...args) {
		CloudFileReader clf = new CloudFileReader();
		try {
			String output = clf.readFileContents(FILE_DIR + "sampleHTML.html");
			System.out.println(output);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
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
}
