package v4.client;

import java.io.*;
import java.net.*;
import java.util.UUID;

import v4.common.types.ClientMessage;
import v4.common.types.FileListServerMessage;
import v4.common.types.FileManifest;
import v4.common.types.MessageType;
import v4.common.types.ServerMessage;

import java.util.Scanner;

public class CloudFileClient {
	
	private String clientID;
	
	CloudFileClient(String clientID) {	
		this.clientID = clientID;
		System.out.println("Starting client " + clientID); 
		try {
			Scanner reader = new Scanner(System.in); 
			while (true) {
				System.out.println("Enter the option number for the information you need from the server: "
							+ "\n 1. Server Time \n 2. Server Address \n 3. List Server Files \n 4. Exit Process");
				int option = reader.nextInt();
				if(option < 4) {
					ClientMessage message = buildClientMessage(option);
					Socket soc = new Socket("192.168.56.110", 5217);   
					ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
			        out.writeObject(message);
			        ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
		        	ServerMessage returnMessage = (ServerMessage) in.readObject();
		        	if (returnMessage instanceof FileListServerMessage) {
		        		readServerFileLists((FileListServerMessage)returnMessage);
		        	} else {
		        		System.out.println(returnMessage.message);
		        	}
				}
				if(option == 4) {
					System.out.println("Closing client " + clientID); 
					reader.close();
					break;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private void readServerFileLists(FileListServerMessage serverFileList) {
		for(FileManifest fileObject : serverFileList.fileManifestList) {
			System.out.println("File Name: " + fileObject.fileName + " | File Size: " + 
					fileObject.fileSize + " | Total Chunks: " + fileObject.numChunks);
		}
	}

	private ClientMessage buildClientMessage(int option) {
		ClientMessage message = new ClientMessage();
		try {
			message.clientID = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.clientID = "Cannot get localHost";
		}
		if(option == 1) {
			message.messageType = MessageType.GET_TIME;
		} 
		if(option == 2) {
			message.messageType = MessageType.GET_SERVER_ADDRESS;
		}
		if(option == 3) {
			message.messageType = MessageType.GET_FILE_MANIFESTS;
		}
		return message;
	}
	
	
	public static void main(String args[]) throws Exception {
        new CloudFileClient(UUID.randomUUID().toString());
    }    
}
