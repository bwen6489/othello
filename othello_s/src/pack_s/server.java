package pack_s;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


class server implements Runnable
{
	boolean sign=false;
	
	static DatagramSocket ser;
	
	static int num=0;
	
	class player
	{
		int port=-1;
		String id="";
		InetAddress addr=null;
		int in=-1;
		boolean ava;
		int count=0;
		int watch=-1;

	
	player(int i,InetAddress add,String n)
	{
		this.port=i;
		this.addr=add;
		this.id=n;
	}
	
	}
	
	
	static player p[]= new player[100];
	
public void run()
{
	while(true)
	{
		ser = null;
		try {
			ser = new DatagramSocket(5000);
			res.iniroom();
			sign=true;
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println("server already opened");
			e.printStackTrace();
			break;
		}
		
		
		while(sign)
		{
			
			byte[] recvBuf = new byte[100];
			DatagramPacket recvPacket 
			= new DatagramPacket(recvBuf , recvBuf.length);
			try {
				ser.receive(recvPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String recvStr = new String(recvPacket.getData() , 0 , recvPacket.getLength());
			
			int port = recvPacket.getPort();
			InetAddress addr = recvPacket.getAddress();
			int i=0;
			for (;i<num&&p[i].port!=port;i++);
			if (i==num)
			{
				i=0;
			for (;i<num&&p[i].port!=-1;i++);
				p[i]=new player(port,addr,"");
				if (i==num) num++;
				res.pnum[i]=0;
				p[i].count=0;
			}
			
	//		System.out.println("Hello World!" + recvStr);
		//	System.out.println(port);
			//System.out.println(addr); 
			
			int mark=recvStr.indexOf("-");
			int scount1=Integer.valueOf(recvStr.substring(0,mark));
			if (scount1-res.pnum[i]<=1)
			{
				res.pnum[i]=scount1;
			}
			else
			{
				p[i].ava=false;
				
			}
			
			recvStr=recvStr.substring(mark+1);
			
			if (recvStr.length()>7&&recvStr.substring(0,7).equals("/login "))
			{
				String id=recvStr.substring(7);
				int i1=0;
				for(;i1<num-1&&!server.p[i1].id.equals(id);i1++);
				if (id.indexOf(" ")!=-1||id.indexOf(",")!=-1) {send_s.normal(p[i], "id error");p[i].id=" ";}
				else if (i1==num-1) {p[i].id=recvStr.substring(7); p[i].ava=true;send_s.normal(p[i],"login succeed");p[i].count++;}
				else {send_s.normal(p[i],"id used");p[i].id=" ";}
			//	p[i].count++;
			}
			else if (recvStr.length()>9&&recvStr.substring(0,9).equals("/relogin "))
			{
				p[i].id="";
				int i1=0;
				String id=recvStr.substring(9);
				for(;i1<num-1&&!server.p[i1].id.equals(id);i1++);
				if (id.indexOf(" ")!=-1||id.indexOf(",")!=-1) {send_s.normal(p[i], "id error");p[i].id=" ";}
				else if (i1==num-1) {p[i].id=id; p[i].ava=true;send_s.normal(p[i],"relogin succeed");p[i].count++;}
				else {send_s.normal(p[i],"id used");p[i].id=" ";}
			//	p[i].count++;
			}

			else if (p[i].ava) res.re(recvStr,p[i],i);
			if (p[i].id==" ") {send_s.normal(p[i],"please change your id"); p[i].count++;}
			
		}
		}
}
}
//}