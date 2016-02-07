package pack_s;

import pack_s.server.player;

class res
{
	static int room[][]=new int[100][2];
	static boolean roomr[][]=new boolean[100][2];
	
	static boolean rm[]=new boolean[100];
	static int pnum[]=new int[100]; 

	static int watch[][]=new int[100][100]; 
	
	static String trboard(int n)
	{
		String b="board ";
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				if (rule_s.loc[n][i][j]==-1) b+="b";
				else if (rule_s.loc[n][i][j]==1) b+="w";
				else b+=0;
	//	System.out.println(b);
		return b;
	}
	
	static void iniroom()
	{
		for (int i=0;i<100;i++)
		{
			room[i][0]=-1;
			room[i][1]=-1;
			roomr[i][0]=false;
			roomr[i][0]=false;
		}
	}
	
	static int rnum(player p,int or){
		int i=0;
		for(;i<2;i++)
			if (room[p.in][i]==or)
				break;
		return i;
	}
	
	static void re(String s,player p,int or)
	{
		//查询当前
		if (s.equals("/listplayer"))
		{
			String root="";
			int count=0;
			for(int i=0;i<server.num;i++) if (server.p[i].ava) count++;
			if (count>0)
			{
				root+=count;
				if (count==1) root+="player left:";
				else root+="players left:";
				for(int i=0;i<server.num;i++) if (server.p[i].ava)
				{
					root+="\n";
					root+=server.p[i].id;
					int n=server.p[i].in;
					if (n!=-1) {
						root+=" in room"; root+=n;
						if (res.room[n][0]!=-1&&res.room[n][1]!=-1)
						{
							if (res.room[n][0]==i) root+=" black";
							else root+=" white";
						}	
					}
					if (server.p[i].watch!=-1) root+=" watch room"+server.p[i].watch;
				}
			}
			else root+="no player left";
			send_s.normal(p,root);
			p.count++;
		}
		else if (s.equals("/listgame"))
		{
			String root="";
			int count=0;
			for(int i=0;i<100;i++) if (rm[i]) count++;
			if (count>0)
			{
				root+=count;
				if (count==1) root+="room left:";
				else root+="rooms left:";
				for(int i=0;i<100;i++) if (rm[i])
				{
					root+="\nroom"+i+":";
					int n=room[i][0];
					root+="<";
					if (n!=-1) root+=server.p[n].id;
					root+=">";
					root+="vs";
					int n1=room[i][1];
					root+="<";
					if (n1!=-1) root+=server.p[n1].id;
					root+=">";
				}
			}
			else root+="";
			send_s.normal(p,root);
			p.count++;
		}
		else if (s.equals("/check"))
		{
			send_s.normal(p, "get");
			p.count++;
		}
		//房间操作
		else if (s.length()>6&&s.substring(0,6).equals("/join ")) 
		{
			int n=Integer.valueOf(s.substring(6));
			if (!rm[n]) {send_s.normal(p, "room not open");p.count++;}
			else{
				boolean em=true;
				for(int i=0;i<2&&em;i++)
					if (room[n][i]==-1)
					{
						room[n][i]=or;
						em=false;
						p.in=n;
						send_s.normal(p, "join succeed");
					}
				if (em) {send_s.normal(p, "room full");p.count++;}
				else if ((room[n][0]!=-1)&&(room[n][1]!=-1))
				{
					send_s.normal(server.p[room[n][0]], "you are black");
					send_s.normal(server.p[room[n][1]], "you are white");
					rule_s.makeboard(n);
					for (int i=0;i<100;i++) if (watch[n][i]!=-1)
					{
						send_s.normal(server.p[watch[n][i]], "<"+server.p[room[n][0]].id+">vs<"+server.p[room[n][1]].id+">");
						send_s.normal(server.p[watch[n][i]], trboard(n));
						server.p[watch[n][i]].count++;
					}
					server.p[room[n][0]].count++;server.p[room[n][1]].count++;
				}
			}
			
		}
		else if (s.length()>7&&s.substring(0,7).equals("/watch "))
		{
			int n=Integer.valueOf(s.substring(7));
			if (!rm[n]) send_s.normal(p, "room not open");
			else {
				for(int i=0;i<100;i++) if (watch[n][i]==-1) 
				{
					p.watch=n;
					watch[n][i]=or;
				if (room[n][0]!=-1&&room[n][1]!=-1)	
					{
						send_s.normal(p, "<"+server.p[room[n][0]].id+">vs<"+server.p[room[n][1]].id+">");
						send_s.normal(p, trboard(n));
					}
					break;
				}
				send_s.normal(p, "watch succeed");
				if (!rule_s.check(n)&&room[n][0]!=-1&&room[n][1]!=-1){
					int count=0;
					for (int i=0;i<8;i++){
						for (int j=0;j<8;j++) count+=rule_s.loc[n][i][j];
						}
					if (count<0){
						send_s.normal(p,"game ends\n"+server.p[room[n][0]].id+ " wins");
					}
					else if (count>0){
						send_s.normal(p,"game ends\n"+server.p[room[n][1]].id+ " wins");
					}
					else {
						send_s.normal(p,"ties");
					}
				}
			}
			p.count++;
		}
		else if(s.equals("get"))
		{
			main_s.check[or]=true;
		}
		//游戏相关
		else if (s.equals("/restart"))
		{
			int i=rnum(p,or);
			roomr[p.in][i]=true;
			if (roomr[p.in][0]&&roomr[p.in][1])
			{
				roomr[p.in][0]=false;
				roomr[p.in][1]=false;
				send_s.normal(server.p[room[p.in][0]], "restart game");
				send_s.normal(server.p[room[p.in][1]], "restart game");
				send_s.normal(server.p[room[p.in][0]], "you are black");
				send_s.normal(server.p[room[p.in][1]], "you are white");
				rule_s.makeboard(p.in);
				server.p[room[p.in][0]].count++;server.p[room[p.in][1]].count++;
				for (int i1=0;i1<100;i1++) if (watch[p.in][i1]!=-1)
				{
					send_s.normal(server.p[watch[p.in][i1]], "game restarted");
					send_s.normal(server.p[watch[p.in][i1]], trboard(p.in));
					server.p[watch[p.in][i1]].count++;
				}
				
			}			
			else {send_s.normal(server.p[room[p.in][1-i]], "your opponent askes you to restart game");
			server.p[room[p.in][1-i]].count++;}
		}
		else if (s.length()>2&&s.substring(0,2).equals("w "))
		{
			send_s.normal(server.p[room[p.in][0]],s);
			server.p[room[p.in][0]].count++;
			String a[]=s.substring(2).split(",");
			int x=Integer.valueOf(a[0]);
			int y=Integer.valueOf(a[1]);
			rule_s.search(x, y, 1, p.in);
			if (main_s.watch) main_s.mpanel.repaint();
			for (int i=0;i<100;i++) if (watch[p.in][i]!=-1) 
			{
				send_s.normal(server.p[watch[p.in][i]], trboard(p.in));
				send_s.normal(server.p[watch[p.in][i]], s);
				server.p[watch[p.in][i]].count++;
			}
		}
		
		else if (s.length()>2&&s.substring(0,2).equals("b "))
		{
			send_s.normal(server.p[room[p.in][1]],s);
			server.p[room[p.in][1]].count++;
			String a[]=s.substring(2).split(",");
			int x=Integer.valueOf(a[0]);
			int y=Integer.valueOf(a[1]);
			rule_s.search(x, y, -1, p.in);
			if (main_s.watch) main_s.mpanel.repaint();
			for (int i=0;i<100;i++) if (watch[p.in][i]!=-1)
			{
				send_s.normal(server.p[watch[p.in][i]], trboard(p.in));
				send_s.normal(server.p[watch[p.in][i]], s);
				server.p[watch[p.in][i]].count++;
			}
		}
		else if (s.equals("end"))
		{
			int count=0;
			for (int i=0;i<8;i++){
				for (int j=0;j<8;j++) count+=rule_s.loc[p.in][i][j];
				}
			if (count<0){
				System.out.print("room"+p.in+" "+server.p[room[p.in][0]].id+ " wins");
				send_s.normal(server.p[room[p.in][0]],server.p[room[p.in][0]].id+ " wins");
				send_s.normal(server.p[room[p.in][1]],server.p[room[p.in][0]].id+ " wins");
				for (int i=0;i<100;i++) if (watch[p.in][i]!=-1) {send_s.normal(server.p[watch[p.in][i]],"game ends\n"+server.p[room[p.in][0]].id+ " wins");server.p[watch[p.in][i]].count++;}
			}
			else if (count>0){
				System.out.print("room"+p.in+" "+server.p[room[p.in][1]].id+ " wins");
				send_s.normal(server.p[room[p.in][0]],server.p[room[p.in][1]].id+ " wins");
				send_s.normal(server.p[room[p.in][1]],server.p[room[p.in][1]].id+ " wins");
				for (int i=0;i<100;i++) if (watch[p.in][i]!=-1) {send_s.normal(server.p[watch[p.in][i]],"game ends\n"+server.p[room[p.in][1]].id+ " wins");server.p[watch[p.in][i]].count++;}
			}
			else {
				System.out.print("room"+p.in+" "+"ties");
				send_s.normal(server.p[room[p.in][0]],"ties");
				send_s.normal(server.p[room[p.in][1]],"ties");
				for (int i=0;i<100;i++) if (watch[p.in][i]!=-1) {send_s.normal(server.p[watch[p.in][i]],"ties");server.p[watch[p.in][i]].count++;}
			}
			server.p[room[p.in][0]].count++;server.p[room[p.in][1]].count++;
		}
		//退出动作
		else if (s.equals("logout"))
		{
			if (p.in!=-1)
			{
				int i=rnum(p,or);
				if(room[p.in][1-i]!=-1)send_s.normal(server.p[room[p.in][1-i]],"your opponent left,you win");
				for (int j=0;j<100;j++) 
					if (watch[p.in][j]!=-1) 
					{
						send_s.normal(server.p[watch[p.in][j]],"<"+p.id+">left, <"+server.p[room[p.in][1-i]].id+"> wins");
						server.p[watch[p.in][j]].count++;
					}
				room[p.in][i]=-1;
				p.in=-1;
			}
			else if(p.watch!=-1)
				for(int i=0;i<100;i++) if (watch[p.watch][i]==or) {watch[p.watch][i]=-1;p.watch=-1;break;} 
			p.id="";
			p.port=-1;
			p.ava=false;
		}
		else if (s.equals("/leave"))
		{
			if (p.in!=-1)
			{
				int i=rnum(p,or);
				if(room[p.in][1-i]!=-1)send_s.normal(server.p[room[p.in][1-i]],"your opponent left,you win");
				for (int j=0;j<100;j++) 
					if (watch[p.in][j]!=-1) 
					{
						send_s.normal(server.p[watch[p.in][j]],"<"+p.id+">left, <"+server.p[room[p.in][1-i]].id+"> wins");
						server.p[watch[p.in][j]].count++;
					}
				room[p.in][i]=-1;
				p.in=-1;
			}
			else if(p.watch!=-1)
				for(int i=0;i<100;i++) if (watch[p.watch][i]==or) {watch[p.watch][i]=-1;p.watch=-1;break;} 
			send_s.normal(p, "leave succeed");
			p.count++;
		}
		else if (s.equals("2logout"))
		{
			if (p.in!=-1)
			{
				int i=rnum(p,or);
				room[p.in][i]=-1;
				p.in=-1;
			}
			else if(p.watch!=-1)
				for(int i=0;i<100;i++) if (watch[p.watch][i]==or) {watch[p.watch][i]=-1;p.watch=-1;break;} 
			p.id="";
			p.port=-1;
			p.ava=false;
			
		}
		//聊天功能
		else if (s.length()>9&&s.substring(0,9).equals("/msggame "))
		{	
			int i=0;
			if (p.in!=-1) 
			{
				i=rnum(p,or);
				for (int i1=0;i1<100;i1++) if (watch[p.in][i1]!=-1) 
				{
					send_s.normal(server.p[watch[p.in][i1]],p.id+":"+s.substring(9));
					server.p[watch[p.in][i1]].count++;
				}
				send_s.normal(p,p.id+":"+s.substring(9));p.count++;
				if(room[p.in][1-i]!=-1) {
					send_s.normal(server.p[room[p.in][1-i]],p.id+":"+s.substring(9));
					server.p[room[p.in][1-i]].count++;
				}
			}
			else if (p.watch!=-1){
				for (int i1=0;i1<100;i1++) if (watch[p.watch][i1]!=-1) 
				{
					send_s.normal(server.p[watch[p.watch][i1]],p.id+":"+s.substring(9));
					server.p[watch[p.watch][i1]].count++;
				}
				for (int i1=0;i1<2;i1++) if(room[p.watch][i1]!=-1) {
					send_s.normal(server.p[room[p.watch][i1]],p.id+":"+s.substring(9));
					server.p[room[p.watch][i1]].count++;
				}
			}
		}
		else if (s.length()>10&&s.substring(0,10).equals("/msgworld "))
		{
			for(int i=0;i<server.num;i++) if (server.p[i].ava)
			{
				send_s.normal(server.p[i],p.id+":"+s.substring(10));
				server.p[i].count++;
			}
		}
	}
}