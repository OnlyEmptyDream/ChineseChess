package Chess;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.*;

public class MyUI 
{
	public static void main(String []args) throws MalformedURLException
	{
		new ChessBoard();
	}
}


class ChessBoard extends JFrame implements MouseListener
{		
	int redFlag=0;
	//棋子所在的横纵线位置
	int HLY[]={10,58,110,160,208,252,300,350,398,450};//横线
	int VLX[]={10,60,105,155,202,250,300,345,390};//竖线
	
	Chess aa=new Chess();
	Move mv=new Move();
	//该走的步数
	int step;
	//控制棋子闪烁的线程
	Thread tmain1;
	Thread tmain;
	int man;
	//窗格
	Container con;
	//工具栏
	JToolBar jmain;	
	//棋盘
	JLabel image;	
	private Rectangle2D current;
	//是否被clickFlag
	static int clickFlag=0;
	int clickPlayFlag=0;	
	
	JLabel play[] = new JLabel[32];
	public void mousePressed(MouseEvent me){}
	public void mouseReleased(MouseEvent me){}
	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	
	/*单击按扭*********************************/
	public void mouseClicked(MouseEvent event)
	{
		JLabel n=new JLabel();
		//System.out.println("clickFlag=1");
		clickPlayFlag=0;
		for (int i=0;i<16;i++)
		{
			//被单击的棋子
			if (event.getSource().equals(play[i]))
			{	
				play[man].setVisible(true);	
				//告诉线程让该棋子闪烁		
				man=i;
				//开始闪烁
				clickFlag=1;
				clickPlayFlag=1;
				break;
			}
		}//for
		if(clickFlag==1)
		{
			int i;
			int j;
			int p;
			int n2;
			int playx;
			int playy;
			int stepFlag=0;
			int canMoveFlag=55;
			int disappearFlag=0;
			int i1,j1;
			//System.out.println("man="+man);
			//启动线程
			if (tmain == null)
			{
				tmain = new Thread(new ThreadShine());
				tmain.start();
			}
			
			if(clickPlayFlag==0)
			{
				stepFlag=0;
				if(((event.getX()-20-play[man].getX())%48)>24)
					stepFlag=1;
				if(((event.getX()-20-play[man].getX())%48)<(-24))
					stepFlag=-1;
				playx=(event.getX()-20-play[man].getX())/48+stepFlag;
				stepFlag=0;
				if(((event.getY()-20-play[man].getY())%48)>24)
					stepFlag=1;
				if(((event.getY()-20-play[man].getY())%48)<(-24))
					stepFlag=-1;
				playy=(event.getY()-20-play[man].getY())/48+stepFlag;
				step=playx+playy*16;
				p=((play[man].getX()/48)+(play[man].getY()/48)*16)+48+3;
				n2=p+step;
				mv.from=p;
				mv.to=n2;
				canMoveFlag=aa.legalMove(mv);
	//			System.out.println("p="+p);
	//			System.out.println("n2="+n2);
	//			System.out.println("step="+step);
	//			System.out.println("canMoveFlag="+canMoveFlag);
				if(aa.side==0)
				{
					if(aa.legalMove(mv)!=0)
					{
						for(i=0;i<8;i++)
						{
							if((playx*48+play[man].getX()-VLX[i])<40)
								break;
						}
						for(j=0;j<9;j++)
						{
							if((playy*48+play[man].getY()-HLY[j])<40)
								break;
						}
						play[man].setBounds(VLX[i],HLY[j],40,40);
						redFlag=1;
						//System.out.println("clickFlagclickFlagclickFlagclickFlagclickFlagclickFlagclickFlagclickFlagclickFlagclickFlagclickFlag");
//						play[man].setBounds(playx*48+play[man].getX(),playy*48+play[man].getY(),40,40);
						//System.out.println("redFlag="+redFlag);
						if(aa.board[mv.to]!=0)
						{
							disappearFlag=aa.board[mv.to];
							play[disappearFlag-16].setBounds(1000,1000,40,40);
						}
						aa.makeMove(mv);
						clickFlag=0;
						
					}
				}
			}
		}
	}

				
	
	class ThreadShine implements Runnable
	{
		public void run()
		{
			while (true)
			{
				if(redFlag==1)
				{
					int disappearFlag1=0;
					int i2,j2;
					int besty=0;
					int bestx=0;
					aa.computerThink();
					if(aa.board[aa.bestMove.to]!=0)
					{
						disappearFlag1=aa.board[aa.bestMove.to];
						play[disappearFlag1-16].setBounds(1000,1000,40,40);
					}
					if(aa.bestMove.from==0&&aa.bestMove.to==0)
					{
						JOptionPane.showMessageDialog(null,"恭喜你,你赢了!");
					}
						
					aa.makeMove(aa.bestMove);
					besty=(aa.bestMove.to-aa.bestMove.from)/16;
					bestx=(aa.bestMove.to-aa.bestMove.from)%16;
					if(bestx>=11)
					{
						bestx=bestx-16;
						besty=besty+1;
					}
					if(bestx<-11)
					{
						bestx=bestx+16;
						besty=besty-1;
					}
					for(i2=0;i2<8;i2++)
					{
						if((play[aa.bestMove.capture-16].getX()+bestx*(48)-VLX[i2])<40)
							break;
					}
					for(j2=0;j2<9;j2++)
					{
						if((play[aa.bestMove.capture-16].getY()+besty*(48)-HLY[j2])<40)
							break;
					}
					play[aa.bestMove.capture-16].setBounds(VLX[i2],HLY[j2],40,40);
					aa.side=0;
					redFlag=0;
				}
				
				//单击棋子第一下开始闪烁
				if (clickFlag==1)
				{				
					play[man].setVisible(false);					
					//时间控制
					try
					{
						Thread.sleep(800);
					}
					catch(Exception e){}
					
					play[man].setVisible(true);								
				}
				try
				{
					tmain.sleep(800);
				}	
				catch (Exception e){}	
			}
		}
	}

	public  ChessBoard()
	{
		//Graphics g =new Graphics2D();
		try
		{
		File file1 = new File("src\\back.mid");
		AudioClip sound1;
		sound1 = Applet.newAudioClip(file1.toURL());
		sound1.play();
		}
		catch(Exception e){}
		aa.initBoard();
		//获行客格引用
		con = this.getContentPane();
		con.setLayout(null);
		//创建工具栏
		jmain = new JToolBar();
		JButton anew = new JButton(" 新 游 戏 ");
		JButton exit = new JButton(" 退  出 ");
		jmain.setLayout(new GridLayout(0,2));
		jmain.setBounds(0,500,450,30);
		jmain.add(anew);
		jmain.add(exit);
		con.add(jmain);
		drawChessMan();
		//注册棋子移动监听
		for (int i=0;i<32;i++)
		{
			con.add(play[i]);
			if(i<16)
			{
				play[i].addMouseListener(this);
			}
		}
		//添加棋盘标签
		con.add(image = new JLabel(new ImageIcon("CChess.gif")));
//		image.setBounds(0,0,500,500);
		image.setBounds(0,0,445,498);
		image.addMouseListener(this);
		//窗体居中
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		
		this.setLocation((screenSize.width - frameSize.width) / 2 - 200 ,(screenSize.height - frameSize.height ) / 2 - 290);
	
		this.setIconImage(new ImageIcon("相1.gif").getImage());
		this.setResizable(false);
		this.setTitle("象棋");
		this.setSize(450,555);
		this.setVisible(true);
	}
	public void drawChessMan()
	{
		//流程控制
		int i,k;
		//图标
		Icon in;
				
		//黑色棋子
		
		//车
		in = new ImageIcon("车1.GIF");
		for (i=23,k=10;i<25;i++,k+=385)
		{		
			play[i] = new JLabel(in);
			play[i].setBounds(k,10,40,40);	
			play[i].setName("车1");			
		}	
		
		//马
		in = new ImageIcon("马1.GIF");
		for (i=21,k=60;i<23;i++,k+=285)
		{			
			play[i] = new JLabel(in);			
			play[i].setBounds(k,10,40,40);			
			play[i].setName("马1");
		}
		
		//相
		in = new ImageIcon("相1.GIF");
		for (i=19,k=105;i<21;i++,k+=195)
		{			
			play[i] = new JLabel(in);
			play[i].setBounds(k,10,40,40);			
			play[i].setName("相1");
		}
		
		//士
		in = new ImageIcon("士1.GIF");			
		for (i=17,k=155;i<19;i++,k+=95)
		{			
			play[i] = new JLabel(in);
			play[i].setBounds(k,10,40,40);			
			play[i].setName("士1");
		}
		
		//卒
		in = new ImageIcon("卒1.GIF");			
		for (i=27,k=10;i<32;i++,k+=96.5)
		{		
			play[i] = new JLabel(in);
			play[i].setBounds(k,160,40,40);			
			play[i].setName("卒1" + i);
		}
		
		//炮
		in = new ImageIcon("炮1.GIF");			
		for (i=25,k=60;i<27;i++,k+=289)
		{			
			play[i] = new JLabel(in);
			play[i].setBounds(k,110,40,40);				
			play[i].setName("炮1" + i);
		}
		
		//将
		in = new ImageIcon("将1.GIF");			
		play[16] = new JLabel(in);
		play[16].setBounds(202,10,40,40);			
		play[16].setName("将1");

		
		//红色棋子
		
		//车
		in = new ImageIcon("车2.GIF");
		for (i=7,k=10;i<9;i++,k+=385)
		{						
			play[i] = new JLabel(in);
			play[i].setBounds(k,450,40,40);		
			play[i].setName("车2");
		}
		
		//马
		in = new ImageIcon("马2.GIF");
		for (i=5,k=60;i<7;i++,k+=285)
		{			
			play[i] = new JLabel(in);			
			play[i].setBounds(k,450,40,40);			
			play[i].setName("马2");
		}
		
		//相
		in = new ImageIcon("相2.GIF");			
		for (i=3,k=105;i<5;i++,k+=195)
		{			
			play[i] = new JLabel(in);
			play[i].setBounds(k,450,40,40);			
			play[i].setName("相2");
		}
		
		//士
		in = new ImageIcon("士2.GIF");			
		for (i=1,k=155;i<3;i++,k+=95)
		{			
			play[i] = new JLabel(in);
			play[i].setBounds(k,450,40,40);			
			play[i].setName("士2");
		}
		
		//兵
		in = new ImageIcon("兵2.GIF");			
		for (i=11,k=10;i<16;i++,k+=96.5)
		{			
			play[i] = new JLabel(in);
			play[i].setBounds(k,300,40,40);			
			play[i].setName("兵2" + i);
		}
		
		//炮
		in = new ImageIcon("炮2.GIF");			
		for (i=9,k=60;i<11;i++,k+=289)
		{			
			play[i] = new JLabel(in);
			play[i].setBounds(k,350,40,40);	
			play[i].setName("炮2" + i);
		}
		
		//帅
		in = new ImageIcon("帅2.GIF");			
		play[0] = new JLabel(in);
		play[0].setBounds(202,450,40,40);		
		play[0].setName("帅2");
	}
}








