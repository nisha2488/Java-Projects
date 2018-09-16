package v3.server;

import java.net.*;
import java.time.LocalDateTime;
import java.io.*;
import java.util.*;

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
			DataOutputStream out = new DataOutputStream(soc.getOutputStream());
	        BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
	        if(in.read() == 1) {
	        	System.out.println("Connection accepted for client: " + soc.getRemoteSocketAddress().toString());
	        	out.writeBytes("Server Date: " + (new Date()).toString());
	        	System.out.println("Current slave put to sleep for client: " + soc.getRemoteSocketAddress().toString());
	        	System.out.println("Data sent back to the client: " + soc.getRemoteSocketAddress().toString());
	        } else {
	        	out.writeBytes("Server Address: " + InetAddress.getLocalHost());
	        }
	        out.close();
	        soc.close();
		} catch (Throwable t) {
			System.err.println("Throwable Caught in CloudFileServerSlave:");
		}
	}
}