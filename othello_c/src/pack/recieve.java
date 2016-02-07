package pack;

import java.io.IOException;
import java.net.DatagramPacket;

class recieve implements Runnable
{
	public void run()
	{
		int scount=0;
		while(true){
			rule.status();
			byte[] recvBuf = new byte[100];
			DatagramPacket recvPacket 
			= new DatagramPacket(recvBuf , recvBuf.length);
			try {
				send.client.receive(recvPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String recvStr = new String(recvPacket.getData() , 0 , recvPacket.getLength());
			//提取序列号
			int mark=recvStr.indexOf("-");
			int scount1=Integer.valueOf(recvStr.substring(0,mark));
			recvStr=recvStr.substring(mark+1);
			if (scount1-scount<=1)
			{
				scount=scount1;
				if(!(recvStr.length()>5&&recvStr.substring(0,5).equals("board")))
					main.output.append(recvStr+"\n");
			}
			else
			{
				main.output.append("out of connection\n");
				try {
					if (main.cli_a)
						if (rule.on==2) send.soc("2logout");
						else send.soc("logout");
					send.close=true;
					send.soc("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				send.close=false; send.a=false; main.cli_a=false;
				
				break;
			}

			if (recvStr.equals("relogin succeed")||recvStr.equals("login succeed")) main.show_id.setText(com.id);
			else if (recvStr.equals("you are kicked")||recvStr.equals("room closed"))
			{
				rule.on=2;
				game.lock=true;
				rule.clearboard();
			}
			else if (recvStr.equals("your opponent is kicked,you win"))
			{
				rule.on=2;
				game.lock=true;
				rule.clearboard();
			}
			else if (recvStr.equals("you are black"))
			{
				rule.on=-1;
				rule.makeboard();
				game.lock=false;
				main.output.append("your turn\n");
			}
			else if (recvStr.equals("you are white"))
			{
				rule.on=1;
				rule.makeboard();
				game.lock=true;
			}
			else if (recvStr.equals("room not open")||recvStr.equals("room full"))
			{
				com.in=false;
			}
			else if ((rule.on!=3)&&(recvStr.length()>2&&(recvStr.substring(0,2).equals("w ")||recvStr.substring(0,2).equals("b "))))
			{
				String a[]=recvStr.substring(2).split(",");
				game.move(Integer.parseInt(a[0]),Integer.parseInt(a[1]));
			}
			else if(recvStr.equals("join succeed"))
			{
				com.in=true;
			}
			else if(recvStr.equals("watch succeed"))
			{
				com.in=true;
				rule.on=3;
			}
			else if (recvStr.length()>6&&recvStr.substring(0,6).equals("board "))
			{
				char a[]=recvStr.substring(6).toCharArray();
				int num=0;
				for(int i=0;i<8;i++)
					for(int j=0;j<8;j++)
						{
							if(a[num]=='b') rule.loc[i][j]=-1;
							else if(a[num]=='w') rule.loc[i][j]=1;
							else rule.loc[i][j]=0;
							num++;
						}
				main.mpanel.repaint();
			}
			else if (recvStr.equals("your opponent left,you win"))
			{
				rule.on=2;
				game.lock=true;
			}
			else if(recvStr.equals("server check")){
				try {
					send.soc("get");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(recvStr.equals("get"))
			{
				rule.ok=true;
			}
		}
	}
}