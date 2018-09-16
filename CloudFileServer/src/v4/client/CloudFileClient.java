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
							+ "\n 1. List Server Files \n 2. Download a File \n 3. Download a file Chunk \n 4. Exit Process");
				int option = reader.nextInt();
				if(option == 4) {
					System.out.println("Closing client " + clientID); 
					reader.close();
					break;
				} else {
					ClientMessage message = buildClientMessage(option);
					Socket soc = new Socket("192.168.56.110", 5217); 
					callServerHandler(soc, message);
				}
				
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private void callServerHandler(Socket soc, ClientMessage message) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
    	if(message.messageType == MessageType.GET_FILE_MANIFESTS){
    		out.writeObject(message);
    		FileListServerMessage returnMessage = (FileListServerMessage) in.readObject();
    	    readServerFileLists((FileListServerMessage)returnMessage);
    	} else if(message.messageType == MessageType.GET_FILE){
    		ServerMessage returnMessage = (ServerMessage) in.readObject();
    		
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
			message.messageType = MessageType.GET_FILE_MANIFESTS;
		} 
		if(option == 2) {
			message.messageType = MessageType.GET_FILE;
		}
		if(option == 3) {
			message.messageType = MessageType.GET_FILE_CHUNK;
		}
		return message;
	}
	
	
	public static void main(String args[]) throws Exception {
        new CloudFileClient(UUID.randomUUID().toString());
    }    
}
