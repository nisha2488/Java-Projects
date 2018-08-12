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
            out.writeBytes("Server Date: " + (new Date()).toString() + "\n");
            out2.writeBytes("Nothing!");
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
