package pack_s;

import java.util.Timer;

import java.util.TimerTask;

import pack_s.server.player;

class timer{
	
	public static void timer1() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
            	for(int i=0;i<server.num;i++){
            		if((server.p[i].port!=-1)&&!main_s.check[i])
            		{	server.p[i].id="";
            			server.p[i].ava=false;
            			System.out.println("connect lost"+server.p[i].port);	
            			server.p[i].port=-1;}
                }
            }
        }, 5000);
    }

}
