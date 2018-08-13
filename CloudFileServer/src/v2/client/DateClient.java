package v2.client;

import java.io.*;
import java.net.*;
import java.util.UUID;
import java.util.Scanner;

public class DateClient {
	
	private String clientID;
	
	DateClient(String clientID) {	
		this.clientID = clientID;
		System.out.println("Starting client " + clientID); 
		try {
			Scanner reader = new Scanner(System.in); 
			while (true) {
				System.out.println("Enter the option number for the information you need from the server: "
							+ "\n 1. Server Time \n 2. Server Address \n 3. Exit Process");
				int option = reader.nextInt();
				if(option == 1 || option == 2) {
					Socket soc = new Socket("192.168.56.110", 5217);   
			        DataOutputStream out = new DataOutputStream(soc.getOutputStream());
			        BufferedReader in= new BufferedReader(new InputStreamReader(soc.getInputStream()));
			        StringBuilder sb = new StringBuilder();
			        while(true) {
			        	String currentLine = in.readLine();
			        	if(currentLine == null || currentLine.isEmpty()) {
			        		break;
			        	}
			        	sb.append(currentLine);
			        }
			        System.out.println(sb);
				}
				if(option == 3) {
					System.out.println("Closing client " + clientID); 
					reader.close();
					break;
				}
		}
		}catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws Exception {
        new DateClient(UUID.randomUUID().toString());
    }    
}
