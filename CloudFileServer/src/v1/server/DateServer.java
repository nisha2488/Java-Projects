package v1.server;

import java.net.*;
import java.io.*;
import java.util.*;

public class DateServer {
	public static void main(String args[]) throws Exception {
        ServerSocket s=new ServerSocket(5217);
        while(true) {
            System.out.println("Waiting For Connection ...");
            Socket soc = s.accept();
            DataOutputStream out = new DataOutputStream(soc.getOutputStream());
            DataOutputStream out2 = new DataOutputStream(soc.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            if(in.read() == 1) {
            	out.writeBytes("Server Date: " + (new Date()).toString());
            }
            else {
            	out.writeBytes("Server Address: " + InetAddress.getLocalHost());
            }
//            out.writeBytes("Server Date: " + (new Date()).toString());
            out2.writeBytes(" \n Data Sent!");
            out.close();
            out2.close();
            soc.close();
            s.close();
        }

    }
}


//--- 

//class DateServerSlave implements Runnable {
//  public void run() {
//
//  }
//}
