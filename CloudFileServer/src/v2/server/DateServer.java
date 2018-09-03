package v2.server;

import java.net.*;
import java.time.LocalDateTime;
import java.io.*;
import java.util.*;

public class DateServer {
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
		DateServerSlave dss = new DateServerSlave(soc);
		// Schedules a new thread and lets the current thread continue (back to main function)
		new Thread(dss).start(); 
	}
}

// Multi-threading to handle multiple client requests.

class DateServerSlave implements Runnable {

	Socket soc;

	DateServerSlave(Socket soc) {
		this.soc = soc;
	}

	@Override
	public void run() {
		try {
			DataOutputStream out = new DataOutputStream(soc.getOutputStream());
	        BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
	        if(in.read() == 1) {
	        	System.out.println("Connection accepted for client: " + soc.getRemoteSocketAddress().toString());
	        	out.writeBytes("Server Date: " + (new Date()).toString());
//	        	out.writeBytes("Current thread put to sleep at : " + (LocalDateTime.now()).toString());
	        	//Sleep for 20 seconds to test multi-threading
//	        	Thread.sleep(20000);
//	        	out.writeBytes("Current thread back to runnable at : " + (LocalDateTime.now()).toString());
	        	System.out.println("Data sent back to the client: " + soc.getRemoteSocketAddress().toString());
	        } else {
	        	out.writeBytes("Server Address: " + InetAddress.getLocalHost());
//	        	out.writeBytes("Current thread executed at : " + (LocalDateTime.now()).toString());
	        }
	        out.close();
	        soc.close();
		} catch (Throwable t) {
			System.err.println("Throwable Caught in DateServerSlave:");
			t.printStackTrace();
		}
	}
}