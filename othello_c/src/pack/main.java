package pack;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class main {

	private JFrame frame = new JFrame("othello");
	private Container c = frame.getContentPane();
	private JTextField command = new JTextField();
	static TextArea output=new TextArea("",0,0,1);
	static Label show_id=new Label();
	static Label show_status=new Label();
	private JScrollPane jp=new JScrollPane(output);
	
	private JButton ok = new JButton("确定");
	private JButton cancel = new JButton("退出");
	private JButton clear = new JButton("清除");
	
//	private rule re;
	
	static int px;
	static int py;
	
	static boolean cli_a=false;
	
	public main(){
		
		frame.setSize(680,500);
		frame.setResizable(false);
		c.setLayout(new BorderLayout());
		initFrame();
		frame.setVisible(true);
		
		}
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
						if (rule.loc[i][j]==1) 
					        g.drawImage(white, i*50+2, j*50+2, 46,46, this);
						if (rule.loc[i][j]==-1) 
							g.drawImage(black, i*50+2, j*50+2, 46,46, this);
					}
				}
				
		    }
	}
	
 static MainPanel mpanel;
	
	private void initFrame() {
	 mpanel = new MainPanel();

		mpanel.setBounds(0,0,410,410);
		c.add(mpanel);
		
		mpanel.addMouseListener(new MouseAdapter(){

			public void mouseClicked(MouseEvent e){
				px=e.getX();
				py=e.getY();
			if (rule.on==0)	game.single(px,py);
			else if (rule.on==2)
			{
				output.append("not start yet\n");
				rule.clearboard();
			}
			else if (rule.on==3) output.append("you are not in game\n");
			else game.online(px,py);
			mpanel.repaint();
			
			}
		});
		
		//中部表单
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(null);
		JLabel l1 = new JLabel("command");
		l1.setBounds(410,30, 70, 20);
		fieldPanel.add(l1);
		
		command.setBounds(480,30,200,20);
		show_id.setBounds(410, 10, 150, 20);
		show_status.setBounds(570, 10, 110, 20);
		output.setBounds(410,60,270,340);
		
		show_id.setText("user's id=");
		show_status.setText("status=waiting");
		
		jp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		fieldPanel.add(show_id);
		fieldPanel.add(show_status);
		fieldPanel.add(jp);
		fieldPanel.add(command);
		fieldPanel.add(output);
		output.setEditable(false);
		
		c.add(fieldPanel,"Center");

		rule.maked();
		
		cancel.addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent e){
			if (e.getSource()==cancel){
				try {
					if (cli_a)
						if (rule.on==2) send.soc("2logout");
						else send.soc("logout");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
				}
	}	
		});
		ok.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getSource()==ok){
					output.append(">"+command.getText()+"\n");
					if(command.getText().equals("chess number")&&rule.on!=2) {rule.cnum();}
					else com.text(command.getText());
					mpanel.repaint();	
					command.setText("");
				}
			}
		});
		clear.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getSource()==clear){
					if (rule.on==2)rule.clearboard();
					output.setText("");
				}
			}	
		});
		
		
		//底部按钮
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		buttonPanel.add(clear);
		c.add(buttonPanel,"South");
	}

	
	public static void main(String[] args){
		new main();
	}
	
}