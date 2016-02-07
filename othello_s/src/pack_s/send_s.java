package pack_s;

import java.io.IOException;
import java.net.DatagramPacket;

import pack_s.server.player;

public class send_s
{
	static void normal(player p,String sendStr)
	{	
		String s=p.count+"-";
		sendStr=s+sendStr;
    	byte[] sendBuf = null;
    	sendBuf = sendStr.getBytes();
    	DatagramPacket sendPacket 
        = new DatagramPacket(sendBuf , sendBuf.length , p.addr , p.port );
    	try {
			server.ser.send(sendPacket);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
	//void sending();
}