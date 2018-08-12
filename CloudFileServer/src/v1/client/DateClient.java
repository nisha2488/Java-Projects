package v1.client;

import java.io.*;
import java.net.*;

public class DateClient {
	public static void main(String args[]) throws Exception
    {
        Socket soc=new Socket("192.168.56.110", 5217);        
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
}
