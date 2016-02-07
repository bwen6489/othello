package pack_s;

public class rule_s{

	private static int d[][]=new int [8][2];
	
	static int loc[][][]=new int[100][8][8];
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
	
	public static void makeboard(int n)
	{
		for (int i=0;i<8;i++)
			for (int j=0;j<8;j++)	loc[n][i][j]=0;
		loc[n][3][3]=-1; loc[n][3][4]=1; loc[n][4][3]=1; loc[n][4][4]=-1;
		if (main_s.watch) main_s.mpanel.repaint();
		
	}
	
	static void move(int px,int py,int i,int n)
	{
		search(px,py,i,n);
		if (!check(n)||main_s.watch)
		{
			int count=0;
			for (int i1=0;i1<8;i1++){
				for (int j=0;j<8;j++) count+=loc[n][i1][j];
			}
			System.out.println("end");
			if (count>0) System.out.println("winner is white");
			else if (count<0) System.out.println("winner is black");
			else System.out.println("tie");
		}
	}
	
	public static boolean dfs(int x,int y,int i,int dir,boolean sign,int n){
		boolean a=false;

		if (loc[n][x][y]==i) return true;
		else if (loc[n][x][y]==0) return false;
		else if (((x+d[dir][0]>-1)&(x+d[dir][0]<8))&((y+d[dir][1]>-1)&(y+d[dir][1]<8)))
			a=dfs(x+d[dir][0],y+d[dir][1],i,dir,sign,n);
		else return false;
		if (a) {
			if (sign) loc[n][x][y]=i;
			return true;
		}
		else return false;
	}
	
	public static boolean search(int x,int y,int i,int n){
		boolean a=false;
		if (loc[n][x][y]==0){
			loc[n][x][y]=i;
			for(int j=0;j<8;j++) if ((x+d[j][0]>-1)&(x+d[j][0]<8)&(y+d[j][1]>-1)&(y+d[j][1]<8))
				if	(i!=loc[n][x+d[j][0]][y+d[j][1]]) a=a|dfs(x+d[j][0],y+d[j][1],i,j,true,n);
			}
		return a;
	}
	public static boolean check(int n){
		boolean a=false;
		for (int chess=-1;chess<2;chess=chess+2)
		for (int x=0;(x<8)&(!a);x++)
			for (int y=0;(y<8)&(!a);y++)
				if (loc[n][x][y]==0){
					loc[n][x][y]=chess;
					for(int j=0;j<8;j++) if ((x+d[j][0]>-1)&(x+d[j][0]<8)&(y+d[j][1]>-1)&(y+d[j][1]<8))
						if	(chess!=loc[n][x+d[j][0]][y+d[j][1]]) a=a||dfs(x+d[j][0],y+d[j][1],chess,j,false,n);
					loc[n][x][y]=0;
				}
				else a=false;
					
		return a;
	}
}