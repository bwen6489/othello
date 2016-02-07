package pack;


import java.io.IOException;
import java.util.Timer;

import java.util.TimerTask;

class timer{
	
	public static void timer1() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (!rule.ok)
                	{main.output.append("connect lost\n");
                try {
					send.soc("");
					main.cli_a=false;
					main.show_id.setText("user's id=");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
            }
        }, 3000);
    }

}
