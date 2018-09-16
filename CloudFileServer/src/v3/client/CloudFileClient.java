package v3.client;

import java.io.*;
import java.net.*;
import java.util.UUID;

import v3.common.types.ClientMessage;
import v3.common.types.MessageType;
import v3.common.types.ServerMessage;

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
							+ "\n 1. Server Time \n 2. Server Address \n 3. Exit Process");
				int option = reader.nextInt();
				if(option == 1 || option == 2) {
					ClientMessage message = buildClientMessage(option);
					Socket soc = new Socket("192.168.56.110", 5217);   
					ObjectOutputStream out = new ObjectOutputStream(soc.getOutputStream());
			        out.writeObject(message);
			        ObjectInputStream in = new ObjectInputStream(soc.getInputStream());
			        ServerMessage returnMessage = (ServerMessage) in.readObject();
			        System.out.println(returnMessage.message);
				}
				if(option == 3) {
					System.out.println("Closing client " + clientID); 
					reader.close();
					break;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
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
		} else {
			message.messageType = MessageType.GET_SERVER_ADDRESS;
		}
		return message;
	}
	
	public static void main(String args[]) throws Exception {
        new CloudFileClient(UUID.randomUUID().toString());
    }    
}
