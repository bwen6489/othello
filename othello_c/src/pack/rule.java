package pack;

public class rule{
	static int on=2;
	
	static void status(){
		if (on==0)main.show_status.setText("status=single");
		else if (on==2)main.show_status.setText("status=waiting");
		else if (on==-1)main.show_status.setText("status=black");
		else if (on==1)main.show_status.setText("status=white");
		else if (on==3)main.show_status.setText("status=watching");
	}
	
	static void cnum(){
		int b=0,w=0;
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				if (loc[i][j]==-1) b++;
				else if(loc[i][j]==1) w++;
		main.output.append("black:"+b+";white:"+w+"\n");
	}
	
	private static int d[][]=new int [8][2];
	
	static int loc[][]=new int[8][8];
	
	static boolean ok=false;
	
	public static void maked()	{
		d[0][0]=0; d[0][1]=1;
		d[1][0]=0; d[1][1]=-1;
		d[2][0]=1; d[2][1]=1;
		d[3][0]=1; d[3][1]=-1;
		d[4][0]=-1; d[4][1]=0;
		d[5][0]=-1; d[5][1]=1;
		d[6][0]=-1; d[6][1]=-1;
		d[7][0]=1; d[7][1]=0;
		
	}
	
	public static void makeboard()
	{
		for (int i=0;i<8;i++)
			for (int j=0;j<8;j++)	loc[i][j]=0;
		loc[3][3]=-1; loc[3][4]=1; loc[4][3]=1; loc[4][4]=-1;
		main.mpanel.repaint();
		
	}
	
	public static void clearboard()
	{
		for (int i=0;i<8;i++)
			for (int j=0;j<8;j++)	loc[i][j]=0;
		main.mpanel.repaint();
	}
	
	public static boolean dfs(int x,int y,int i,int dir,boolean sign){
		boolean a=false;

		if (loc[x][y]==i) return true;
		else if (loc[x][y]==0) return false;
		else if (((x+d[dir][0]>-1)&(x+d[dir][0]<8))&((y+d[dir][1]>-1)&(y+d[dir][1]<8)))
			a=dfs(x+d[dir][0],y+d[dir][1],i,dir,sign);
		else return false;
		if (a) {
			if (sign) loc[x][y]=i;
			return true;
		}
		else return false;
	}
	
	public static boolean search(int x,int y,int i){
		boolean a=false;
		if (loc[x][y]==0){
			loc[x][y]=i;
			for(int j=0;j<8;j++) if ((x+d[j][0]>-1)&(x+d[j][0]<8)&(y+d[j][1]>-1)&(y+d[j][1]<8))
				if	(i!=loc[x+d[j][0]][y+d[j][1]]) a=a|dfs(x+d[j][0],y+d[j][1],i,j,true);
			}
		return a;
	}
	public static boolean check(int chess){
		boolean a=false;
		for (int x=0;(x<8)&(!a);x++)
			for (int y=0;(y<8)&(!a);y++)
				if (loc[x][y]==0){
					loc[x][y]=chess;
					for(int j=0;j<8;j++) if ((x+d[j][0]>-1)&(x+d[j][0]<8)&(y+d[j][1]>-1)&(y+d[j][1]<8))
						if	(chess!=loc[x+d[j][0]][y+d[j][1]]) a=a|dfs(x+d[j][0],y+d[j][1],chess,j,false);
					loc[x][y]=0;
				}
				else a=false;
					
		return a;
	}
	public static boolean checkend()
	{
		return !(check(-1)||check(1));
	}
}