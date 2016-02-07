package pack_s;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

class main_s{
	
	public static boolean isNum(String str) {
		  
        try {
            int num=Integer.valueOf(str);
            if (num>100||num<0) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	private static JFrame frame;
	
	static int m[][]=new int[8][8];
	static boolean watch=false;
	static boolean check[]=new boolean[100];
	
	public class MainPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(final Graphics g) {
				super.paint(g);
		        Toolkit tool = this.getToolkit();
		        
		        Image black = tool.getImage("1.jpg");
		        Image white = tool.getImage("2.jpg");
		        Image board = tool.getImage("board.png");
		        g.drawImage(board, 0, 0, 400,400, this);
		        
				for (int i=0;i<8;i++){
					for (int j=0;j<8;j++) {
						if (m[i][j]==1) 
					        g.drawImage(white, i*50+2, j*50+2, 46,46, this);
						if (m[i][j]==-1) 
							g.drawImage(black, i*50+2, j*50+2, 46,46, this);
					}
				}
				
		    }
	}
	static MainPanel mpanel;
	
	public main_s(){
		Container c = frame.getContentPane();
		frame.setSize(400,420);
		frame.setResizable(false);
		c.setLayout(new BorderLayout());
		paint(frame);
		frame.setVisible(true);
		c.add(mpanel);
		}
	
	private void paint(JFrame frame)
	{
		mpanel=new MainPanel();
		mpanel.setBounds(0,0,410,410);
	}
	
    public static void main(String[] args){
    	server ser=new server();
    	Thread s1=new Thread(ser);
    	Scanner in=new Scanner(System.in);
    	rule_s.maked();
    	s1.start();
    	for (int i=0;i<100;i++) res.rm[i]=false;
    	for (int i=0;i<100;i++)
    		for (int i1=0;i1<100;i1++) res.watch[i][i1]=-1;
    	while(true)
    		{
    		StringBuffer ord = new StringBuffer(in.nextLine());
    //		System.out.println(ord);
    			if (ord.substring(0,ord.length()).equals("/list"))
    			{
    				int count=0;
    				for(int i=0;i<server.num;i++) if (server.p[i].ava) count++;
    				if (count>0)
    				{
    					System.out.print(count);
    					if (count==1) System.out.print("player left:\n");
    					else System.out.print("players left:\n");
    					for(int i=0;i<server.num;i++) if (server.p[i].ava)
    					{
    						System.out.print(server.p[i].id);
    						int rm=server.p[i].in;
    						if (rm!=-1) {
    							System.out.print(" in room "); System.out.print(rm);
    							if (res.room[rm][0]!=-1&&res.room[rm][1]!=-1)
    							{	if (res.room[rm][0]==i) System.out.print(" black");
    							else System.out.print(" white");}	
    						}
    						rm=server.p[i].watch;
    						if (rm!=-1) System.out.print(" watch room "+rm);
    						System.out.println();
    					}
    					
    				}
    				else System.out.println("no player left");
    			}
    			else if (ord.length()>7&&ord.substring(0,7).equals("/watch ")&&isNum(ord.substring(7)))
    			{
    				int num=Integer.valueOf(ord.substring(7));
    				if (res.rm[num])
    				{	if(!watch){
    					String title="room"+ord.substring(7);
    					System.out.print("watch "+title+" succeed\n");
    					frame=new JFrame(title);
    					new main_s();	
    					m=rule_s.loc[num];
    					watch=true;
    					frame.setVisible(true);
    				}
    				else System.out.println("already watching");
    				}
    				else System.out.println("room not open");
    			}
    			else if (ord.length()>6&&ord.substring(0,6).equals("/kick "))
    			{
    				String a[]=ord.substring(6).split(",");
    				for(int i=0;i<a.length;i++)
    				{
    					int j=0;
   						for(;j<server.num;j++)
   						{
    						if (server.p[j].id.equals(a[i]))
    						{
    							if (server.p[j].in!=-1)
    							{
    								send_s.normal(server.p[j],"you are kicked");
    								System.out.print(a[i]+" is kicked\n");
    								int j1=0;
    								for(;j1<2&&res.room[server.p[j].in][j1]!=j;j1++);
    								if (res.room[server.p[j].in][1-j1]!=-1)
    									send_s.normal(server.p[res.room[server.p[j].in][1-j1]],"your opponent is kicked,you win");
    								res.room[server.p[j].in][j1]=-1;
    								server.p[j].in=-1;
    							}
    							else if(server.p[j].watch!=-1)
    							{
    								send_s.normal(server.p[j],"you are kicked");
    								System.out.print(a[i]+" is kicked\n");
    							}
    							else 
    								System.out.println(a[i]+" is not in any game");
    							break;
    						}
    					}
   						if (j==server.num) {System.out.print("no player named "+a[i]+"\n");}
    				}
    			}
    			else if (ord.substring(0,ord.length()).equals("/leave"))
    			{	
    				if (watch){
    				frame.setVisible(false);
    				frame.dispose();
    				System.out.println("leave succeed");
    				watch=false;}
    				else System.out.println("not watching");
    			}
    			else if (ord.length()>5&&ord.substring(0,5).equals("/msg "))
    			{
    				int mark=(ord.substring(5).indexOf(" "));
    				if (mark==-1) System.out.println("no message sent");
    				else{
    					String a[]=ord.substring(5,mark+5).split(",");
    					for(int i=0;i<a.length;i++)
    					{
    						int j=0;
    						for(;j<server.num;j++)
    						{
    							String word="server:";
    							word+=ord.substring(mark+5);
    							if (server.p[j].id.equals(a[i]))
    								{send_s.normal(server.p[j],word);
    							break;}
    						}
    						if (j==server.num) {System.out.println("no player named "+a[i]);}
    					}
    				}
    			}
    			else if(ord.length()>10&&ord.substring(0,10).equals("/opengame ")&&isNum(ord.substring(10)))
    			{
    				int num=Integer.valueOf(ord.substring(10));
    				if (!res.rm[num]){
    					res.rm[num]=true;
    					System.out.println("room"+num+" opened");
    				}
    				else System.out.println("room already opened");
    			}
    			else if(ord.length()>10&&ord.substring(0,11).equals("/closegame ")&&isNum(ord.substring(11)))
    			{
    				int num=Integer.valueOf(ord.substring(11));
    				if (res.rm[num]){
    					res.rm[num]=false;
    					System.out.println("room"+num+" closed");
    					for(int i=0;i<2;i++)
    						if (res.room[num][i]!=-1) 
    						{
    							send_s.normal(server.p[res.room[num][i]], "room closed");
    							server.p[res.room[num][i]].count++;
    							server.p[res.room[num][i]].in=-1;
    						}
    					for (int i=0;i<100;i++)
    						if (res.watch[num][i]!=-1) 
    						{
    							send_s.normal(server.p[res.watch[num][i]], "room closed");
    							server.p[res.watch[num][i]].count++;
    							server.p[res.watch[num][i]].count++;
    						}
    				}
    				else System.out.println("room not opened yet\n");
    			}
    			else if (ord.substring(0,ord.length()).equals("/games"))
    			{
    				int num=0;
    				for (int i=0;i<100;i++)
    					if (res.rm[i]){
    						System.out.print("room"+i+":<");
    						if (res.room[i][0]!=-1) System.out.print(server.p[res.room[i][0]].id);
    						System.out.print(">"+"vs<");
    						if (res.room[i][1]!=-1) System.out.print(server.p[res.room[i][1]].id);
    						System.out.println(">");
    						num++;
    					}
    				if (num==0) System.out.print("no game\n");
    			}
    			else if (ord.substring(0,ord.length()).equals("/check"))
    			{
    				for(int i=0;i<server.num;i++)
    				{
    					check[i]=false;
    					if (server.p[i].port!=-1)
    						send_s.normal(server.p[i],"server check");
    				}
    				timer.timer1();
    			}
    		//	else if (ord.substring(0,ord.length()).equals("/quit")) break;
    			else System.out.println("command error");
    		}
    	

    }
}