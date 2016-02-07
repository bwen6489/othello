package pack;

import java.io.IOException;

class com
{
	static boolean comcr=false;
	
	static boolean in=false;
	
	public static boolean isNum(String str) {
		  
        try {
            int num=Integer.valueOf(str);
            if (num>100||num<0) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	static String id="user's id=";
	
	static void text(String s)
	{
		//单机模式
		if(s.equals("play single")&&!main.cli_a) {
			rule.on=0;
			main.output.append("next black go\n");
			rule.maked();
			rule.makeboard();
			comcr=true;
			main.show_status.setText("status=single");
		}
		
		else if(s.equals("leave single")&&!main.cli_a) {
			rule.on=2;
			rule.clearboard();
			comcr=true;
			main.show_status.setText("status=waiting");
		}
		//联机模式
		else if (s.length()>7&&s.substring(0,7).equals("/login ")&&!main.cli_a)
		{rule.clearboard(); send.a=true; main.cli_a=true; 
		comcr=true; id="user's id="+s.substring(7);}
		else if (main.cli_a)
		{
			if(s.equals("/listplayer")||s.equals("/listgame")||(s.length()>10&&s.substring(0,10).equals("/msgworld ")))
			{
				comcr=true;
			}
			else if(s.equals("/check"))
			{
				rule.ok=false;
				comcr=true;
				timer.timer1();
			}
			else if(s.length()>9&&s.substring(0,9).equals("/relogin "))
			{
				comcr=true;
				id="user's id="+s.substring(9);
			}
			else if((s.length()>6&&s.substring(0,6).equals("/join ")&&isNum(s.substring(6)))
					||(s.length()>7&&s.substring(0,7).equals("/watch ")&&isNum(s.substring(7))))
			{
				if(!in)
				{
					comcr=true;
				}
				else if(s.substring(0,7).equals("/watch ")) rule.on=3;
				else main.output.append("you are already in room\n");
			}
			else if (s.equals("/restart"))
			{
				if(in&&(rule.on==1||rule.on==-1))comcr=true;
				else main.output.append("you are not in a game\n");
			}
			else if(s.length()>9&&s.substring(0,9).equals("/msggame "))
			{
				if(in)comcr=true;
				else main.output.append("you are not in a room\n");
			}
			else if(s.equals("/leave"))
			{
				if(in){comcr=true;in=false;rule.on=2;rule.clearboard();}
				else main.output.append("you are not in a room\n");
			}
			else main.output.append("command error\n");
		}
		else main.output.append("command error\n");
		if(main.cli_a&&comcr){
			try {
				send.soc(s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			comcr=false;
		}
	}
}