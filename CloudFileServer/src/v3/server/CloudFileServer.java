package v3.server;

import java.net.*;
import java.time.LocalDateTime;
import java.io.*;
import java.util.*;

import v3.common.types.ClientMessage;
import v3.common.types.MessageType;
import v3.common.types.ServerMessage;

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
			if(message.messageType == MessageType.GET_TIME) {
	        	System.out.println("Connection accepted for client: " + message.clientID);
	        	returnMessage.message = "Server Date: " + (new Date()).toString();
	        	out.writeObject(returnMessage);
	        	System.out.println("Data sent back to the client: " + message.clientID);
	        } else {
	        	returnMessage.message = "Server Address: " + InetAddress.getLocalHost();
	        	out.writeObject(returnMessage);
	        }
	        out.close();
	        soc.close();
		} catch (Throwable t) {
			System.err.println("Throwable Caught in CloudFileServerSlave:");
		}
	}
}