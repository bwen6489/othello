package pack;

import java.io.IOException;

public class game
{
	static boolean lock=false;
	static boolean correct=false;
	static int play=-1;

	static void single(int px,int py){
		if(rule.on==0){
		if(rule.loc[px/50][py/50]==0) {
			correct=rule.search(px/50,py/50,play);
			if (!correct) rule.loc[px/50][py/50]=0;
			
		}
		else correct=false;
		
		System.out.println(rule.check(play));
		System.out.println(rule.check(-play));
		
		if (rule.check(-play)&&correct) play=-play;
		if (!correct)	main.output.append("wrong step\n");
		if (rule.check(-play)||rule.check(play))
		{
			if (play==-1) main.output.append("next black go\n");
			else main.output.append("next white go\n");
		}
		else{
			int count=0;
			for (int i=0;i<8;i++){
				for (int j=0;j<8;j++) count+=rule.loc[i][j];
			}
			main.output.append("end\n");
			if (count>0) main.output.append("winner is white\n");
			else if (count<0) main.output.append("winner is black\n");
			else main.output.append("tie\n");
			rule.on=2;
		}
		}
		
	}
	
	static void move(int px,int py)
	{
		rule.search(px,py,-rule.on);
		main.mpanel.repaint();
		if(rule.check(rule.on))
		{
			main.output.append("your turn\n");
			lock=false;
		}
		else if(rule.check(-rule.on)) main.output.append("opponent's turn\n");
		else{
			main.output.append("end\n");
			rule.on=2;
		}
	}
	
	
	static void online(int px,int py)
	{
		if (!lock){
		System.out.println(rule.loc[px/50][py/50]==0);
		if(rule.loc[px/50][py/50]==0) {
			correct=rule.search(px/50,py/50,rule.on);
			System.out.println(rule.on);
			if (!correct) rule.loc[px/50][py/50]=0;
		}
		else correct=false;
		String root="";
		if (!correct)	main.output.append("wrong step\n");
		else{
			if (rule.on==-1) root+="b ";
			else root+="w ";
			root+=String.valueOf(px/50);
			root+=",";
			root+=String.valueOf(py/50);
			try {
				send.soc(root);
				main.output.append(root+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (rule.check(0-rule.on))
			{
				main.output.append("opponent's turn\n"); 
				lock=true;
			}
			else if(rule.check(rule.on))
			{
				main.output.append("your turn\n");
			}
			else{
/*				int count=0;
				for (int i=0;i<8;i++){
					for (int j=0;j<8;j++) count+=rule.loc[i][j];
					}	*/
				main.output.append("end\n");
/*				if (count>0) main.output.append("winner is white\n");
				else if (count<0) main.output.append("winner is black\n");
				else main.output.append("tie\n");*/
				rule.on=2;
				try {
					send.soc("end");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
		}
		else main.output.append("not your turn\n");
	}
}