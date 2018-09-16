package v4.server;

import java.net.*;
import java.io.*;
import java.util.*;

import v4.common.CloudFileReader;
import v4.common.types.ClientMessage;
import v4.common.types.FileListServerMessage;
import v4.common.types.FileManifest;
import v4.common.types.MessageType;
import v4.common.types.ServerMessage;

public class CloudFileServer {
	public static void main(String args[]) throws IOException {
		ServerSocket s = new ServerSocket(5217);
		try {
			while (true) {
				System.out.println("Waiting For Connection ...");
				Socket soc = s.accept();
				initializeSlaveForClient(soc);
			}
		} catch (Throwable t) {
			s.close();
		}
	}

	private static void initializeSlaveForClient(Socket soc) {
		CloudFileServerSlave dss = new CloudFileServerSlave(soc);
		// Schedules a new thread and lets the current thread continue (back to main function)
		new Thread(dss).start(); 
	}
}

// Multi-threading to handle multiple client requests.

class CloudFileServerSlave implements Runnable {

	Socket soc;

	CloudFileServerSlave(Socket soc) {
		this.soc = soc;
	}

	@Override
	public void run() {
		System.out.println("DateServerSlave starting for : " + soc.getRemoteSocketAddress().toString());
		try {
			ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
	        ClientMessage message = (ClientMessage)in.readObject();
	        ServerMessage returnMessage = new ServerMessage();
	        returnMessage.messageType = message.messageType;
			if(message.messageType == MessageType.GET_TIME) {
	        	System.out.println("Connection accepted for client: " + message.clientID);
	        	returnMessage.message = "Server Date: " + (new Date()).toString();
	        	out.writeObject(returnMessage);
	        	System.out.println("Data sent back to the client: " + message.clientID);
	        } else if(message.messageType == MessageType.GET_SERVER_ADDRESS) {
	        	returnMessage.message = "Server Address: " + InetAddress.getLocalHost();
	        	out.writeObject(returnMessage);
	        } else if(message.messageType == MessageType.GET_FILE_MANIFESTS) {
				out.writeObject(getFileManifests(message.messageType));
			} else {
				throw new RuntimeException("Unknown messageType: " + message.messageType);
			}
	        out.close();
	        soc.close();
		} catch (Throwable t) {
			System.err.println("Throwable Caught in CloudFileServerSlave:");
			t.printStackTrace();
		}
	}

	private FileListServerMessage getFileManifests(MessageType messageType) {
		File[] listOfFiles = new File(CloudFileReader.FILE_DIR).listFiles();
		FileListServerMessage fileManifests = new FileListServerMessage(); 
		fileManifests.messageType = messageType;
		Arrays.stream(listOfFiles)
			.filter(File::isFile)
			.forEach(file -> fileManifests.fileManifestList.add(
					new FileManifest(file.getName(), (int)file.length())));
//		for (File file : listOfFiles) {
//			if (file.isFile()) {
//				fileManifests.fileManifestList.add(
//						new FileManifest(file.getName(), (int)file.length()));
//			}
//		}
		return fileManifests;
	}
}