package v4.client;

import java.io.*;
import java.net.*;
import java.util.UUID;

import v4.common.CloudFileReader;
import v4.common.CloudFileWriter;
import v4.common.types.ClientMessage;
import v4.common.types.FileListServerMessage;
import v4.common.types.FileManifest;
import v4.common.types.GetFileChunkClientMessage;
import v4.common.types.GetFileClientMessage;
import v4.common.types.MessageType;
import v4.common.types.ServerMessage;

import java.util.Scanner;

public class CloudFileClient {
	
	private String clientID;
//	private static Scanner reader = new Scanner(System.in); 
	
	CloudFileClient(String clientID) {	
		this.clientID = clientID;
		System.out.println("Starting client " + clientID); 
		try {
			
			while (true) {
				System.out.println("Enter the option number for the information you need from the server: "
							+ "\n 1. List Server Files \n 2. Download a File \n 3. Download a file Chunk \n 4. Download a file in Chunks \n 5. Exit Process");
				int option = Integer.parseInt(readStringFromUser());
				if(option == 5) {
					System.out.println("Closing client " + clientID);
					break;
				} else {
					ClientMessage message = buildClientMessage(option);
					Socket soc = new Socket("192.168.56.110", 5217); 
					handle(soc, message);
				}
				
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private void handle(Socket soc, ClientMessage message) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
    	if(message.messageType == MessageType.GET_FILE_MANIFESTS){
    		out.writeObject(message);
    	    readServerFileLists((FileListServerMessage) in.readObject());
    	} else if(message.messageType == MessageType.GET_FILE){
    		GetFileClientMessage clientMessage = (GetFileClientMessage)message; 
    		out.writeObject(clientMessage);
    		downloadServerFile((ServerMessage) in.readObject(), clientMessage.fileName);
    	} else if(message.messageType == MessageType.GET_FILE_IN_CHUNKS){
    		out.close();
    		in.close();
    		soc.close();
    		GetFileClientMessage clientMessage = (GetFileClientMessage)message; 
//    		out.writeObject(clientMessage);
//    		downloadServerFile((ServerMessage) in.readObject(), clientMessage.fileName);
    		new SplitFileGetter().getFileInChunks(clientMessage);
    	} else if(message.messageType == MessageType.GET_FILE_CHUNK){
    		GetFileChunkClientMessage clientMessage = (GetFileChunkClientMessage)message; 
    		out.writeObject(clientMessage);
    		downloadServerFile((ServerMessage) in.readObject(), clientMessage.fileName+clientMessage.chunkNumber);
    	}
	}

	private void downloadServerFile(ServerMessage returnMessage, String fileName) throws IOException {
		if(returnMessage.hasError == true) {
			System.err.println(returnMessage.errorCause);
		} else {
			new CloudFileWriter().writeFileContents(fileName, returnMessage.message);
		}
	}

	private void readServerFileLists(FileListServerMessage serverFileList) {
		for(FileManifest fileObject : serverFileList.fileManifestList) {
			System.out.println("File Name: " + fileObject.fileName + " | File Size: " + 
					fileObject.fileSize + " | Total Chunks: " + fileObject.numChunks);
		}
	}

	private ClientMessage buildClientMessage(int option) {
			if(option == 1) {
				ClientMessage message = new ClientMessage();
				message.clientID = getClientID();
				message.messageType = MessageType.GET_FILE_MANIFESTS;
				return message;
			} else if(option == 2) {
				GetFileClientMessage message = new GetFileClientMessage(getFileName());
				message.clientID = getClientID();
				return message;
			} else if(option == 3) {
				String fileName = getFileName();
				int chunkNumber = getFileChunk();
				GetFileChunkClientMessage message = new GetFileChunkClientMessage(fileName, chunkNumber);
				message.clientID = getClientID();
				return message;
			} else if (option == 4) {
				GetFileClientMessage message = new GetFileClientMessage(getFileName());
				message.clientID = getClientID();
				return message;
			} else {
				throw new RuntimeException();
			}
	}
	
	
	private Integer getFileChunk() {
		System.out.print("Enter the chunk you want to download: ");
		return Integer.parseInt(readStringFromUser());
	}

	private String getFileName() {
		System.out.print("Enter the file name to download: ");
		return readStringFromUser();
	}
	
	private String readStringFromUser() {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		try {
			return bf.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}

	private String getClientID() {
		try {
			return InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "0000"; 
		}
	}
	


	public static void main(String args[]) throws Exception {
        new CloudFileClient(UUID.randomUUID().toString());
    }
}
