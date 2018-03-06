package a;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/*
 * 功能：1、画出一辆坦克
 * 2、坦克可以在规定的区域移动
 * 3、画出3辆敌方坦克
 * 4、画出子弹，子弹能动，子弹连发
 * 5、我方子弹击中坦克，坦克消失
 * 6、敌方坦克能在规定的区域随机移动
 * 7、敌方坦克能发射子弹，击中我方坦克，坦克消失
 * 8、敌方坦克移动时不可重叠
 * 9、添加记分板，包括得分和坦克数
 * 10、画出了各种障碍物，并设置了碰撞效果
 * 11、添加了坦克血包，为坦克增加生命
 * 12、添加了坦克无敌BUFF，使己方坦克无视敌方子弹
 * 13、添加了胜利和失败的效果显示
 * 14、添加了游戏暂停功能，坦克和子弹暂停移动
 * 15、添加菜单栏，并实现了其中的一部分功能
 */

public class Test extends JFrame implements ActionListener{
	
	MyPanel mp = null;
	MyStartPanel msp = null;
	
	public Test(){
		super("坦克游戏");
		setSize(1200, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenu youxi = new JMenu("游戏");
		JMenuItem newGame = new JMenuItem("新游戏");
		newGame.addActionListener(this);
		youxi.add(newGame);
		JMenuItem conGame = new JMenuItem("继续游戏");
		conGame.addActionListener(this);
		youxi.add(conGame);
		JMenuItem exitGame = new JMenuItem("保存并退出");
		exitGame.addActionListener(this);
		youxi.add(exitGame);
		
		JMenu shezhi = new JMenu("设置");
		JMenuItem setupGame = new JMenuItem("设置游戏");
		setupGame.addActionListener(this);
		shezhi.add(setupGame);
		
		
		JMenu bangzhu = new JMenu("帮助");
		JMenuItem help = new JMenuItem("按键说明");
		help.addActionListener(this);
		bangzhu.add(help);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(youxi);
		menuBar.add(shezhi);
		menuBar.add(bangzhu);
		
		this.setJMenuBar(menuBar);
		
		newStartPanel();
	}

	private void newStartPanel() {
		msp = new MyStartPanel();
		this.add(msp);
		this.setVisible(true);
	}

	private void newGame() {
		if (mp != null){
			this.remove(mp);
		}
		mp = new MyPanel(true);

		Thread t = new Thread(mp);
		t.start();
		
		this.remove(msp);
		this.add(mp);
		this.addKeyListener(mp);
		this.setVisible(true);
	}
	
	private void conGame() {
		if (mp != null){
			this.remove(mp);
		}
		mp = new MyPanel(false);

		Thread t = new Thread(mp);
		t.start();
		
		this.remove(msp);
		this.add(mp);
		this.addKeyListener(mp);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Test();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if (s.equals("新游戏")){
			newGame();
		}
		if (s.equals("继续游戏")){
			conGame();
		}
		if (s.equals("保存并退出")){
			Recorder.setBts(mp.bts);
			Recorder.setYts(mp.yts);
			Recorder.keepRecorder();
			System.exit(0);
		}
		
		if (s.equals("设置游戏")){
			SetUp sz = new SetUp();
			sz.setVisible(true);
		}
		
		if (s.equals("按键说明")){
			Help h = new Help();
			h.setVisible(true);
		}
	}
}

class Help extends JFrame{
	public Help(){
		super("按键说明");
		setSize(200, 100);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		setLayout(new GridLayout(2, 1));
		
		JLabel labell = new JLabel("方向键： w、s、a、d");
		JLabel label2 = new JLabel("暂停键：h");
		
		add(labell);
		add(label2);
	}
}

class SetUp extends JFrame implements ActionListener {
	TextField field1 = null;
	TextField field2 = null;
	TextField field3 = null;
	TextField field4 = null;
	
	public SetUp() {
		setTitle("设置");
		setSize(600, 300);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		setLayout(new GridLayout(1, 3));
		
		JPanel blueTankNum = new JPanel();
		blueTankNum.setLayout(new GridLayout(3, 1));
		blueTankNum.setBackground(Color.BLUE);
		JLabel label1 = new JLabel("坦克数量");
		int n1 = Recorder.getBlueTankNum();
		field1 = new TextField(""+n1);
		JButton button1 = new JButton("确定");
		button1.setActionCommand("blueTankNum");
		button1.addActionListener(this);
		blueTankNum.add(label1);
		blueTankNum.add(field1);
		blueTankNum.add(button1);
		
		
		JPanel bulletNum = new JPanel();
		bulletNum.setLayout(new GridLayout(3, 1));
		bulletNum.setBackground(Color.YELLOW);
		JLabel label2 = new JLabel("子弹数量");
		field2 = new TextField("");
		JButton button2 = new JButton("确定");
		button2.setActionCommand("bulletNum");
		button2.addActionListener(this);
		bulletNum.add(label2);
		bulletNum.add(field2);
		bulletNum.add(button2);
		
		JPanel bulletSpeed = new JPanel();
		bulletSpeed.setLayout(new GridLayout(3, 1));
		bulletSpeed.setBackground(Color.CYAN);
		JLabel label3 = new JLabel("子弹速度");
		field3 = new TextField("10");
		JButton button3 = new JButton("确定");
		button3.setActionCommand("bulletSpeed");
		button3.addActionListener(this);
		bulletSpeed.add(label3);
		bulletSpeed.add(field3);
		bulletSpeed.add(button3);
		
		JPanel guanQia = new JPanel();
		guanQia.setLayout(new GridLayout(3, 1));
		guanQia.setBackground(Color.GREEN);
		JLabel label4 = new JLabel("选择关卡");
		field4 = new TextField("1");
		JButton button4 = new JButton("确定");
		button4.setActionCommand("guanQia");
		button4.addActionListener(this);
		guanQia.add(label4);
		guanQia.add(field4);
		guanQia.add(button4);
		
		add(blueTankNum);
		add(bulletSpeed);
		add(guanQia);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if (s.equals("blueTankNum")){
			String num = field1.getText();
			int n = Integer.parseInt(num);
			Recorder.setBlueTankNum(n);
		}
		if (s.equals("bulletNum")){
		}
		if (s.equals("bulletSpeed")){
			String num3 = field3.getText();
			int n3 = Integer.parseInt(num3);
			BlueTank.bulletSpeed = n3;
		}
		if (s.equals("guanQia")){
			String num4 = field4.getText();
			int n4 = Integer.parseInt(num4);
			MyPanel.guanNum = n4;
		}
	}
	
}

class MyStartPanel extends JPanel{
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 1200, 900);
		
		Font f = new Font("Serif", Font.ITALIC, 80);
		g.setFont(f);
		g.setColor(Color.PINK);
		g.drawString("请开始游戏", 400, 300);
	}
}

class MyPanel extends JPanel implements KeyListener, Runnable{
	
	public static boolean is_suspend = false;
	public static int guanNum = 1;
	
	YelloTank yelloTank = null;
	
	Vector<BlueTank> bts = new Vector<BlueTank>();
	Vector<YelloTank> yts = new Vector<YelloTank>();
	int blueTankNum = 0;  // 屏幕坦克余量
	
	Vector<Bomb> bomsShotTank = new Vector<Bomb>();
	Vector<Bomb> bomsShotStoneWall = new Vector<Bomb>();
	
	WoodWall woodWall = null;
	StoneWall stoneWall = null;
	Grass grass = null;
	River river = null;
	Vector<WoodWall> wws = new Vector<WoodWall>();
	Vector<StoneWall> sws = new Vector<StoneWall>();
	Vector<Grass> gs = new Vector<Grass>();
	Vector<River> rs = new Vector<River>();
	
	Blood blood = null;
	Invincible invincible = null;
	
	Image image1 = null;
	Image image2 = null;
	Image image3 = null;
	Image image4 = null;  // 子弹撞击石墙的爆炸效果1
	Image image5 = null;  // 子弹撞击石墙的爆炸效果2
	Image imageHome = null;
	Image imageWoodWall = null;
	Image imageStoneWall = null;
	Image imageGrass = null;
	Image imageRiver = null;
	Image imageBlood = null;
	Image imageBuff = null;
	
	Home home = null;
	
	public MyPanel(boolean isNewGame) {
		home = new Home(360, 520, 80, 80);
		
		try {
			image1=ImageIO.read(new File("bomb_1.gif"));
			image2=ImageIO.read(new File("bomb_2.gif"));
			image3=ImageIO.read(new File("bomb_3.gif"));
			image4=ImageIO.read(new File("0.gif"));
			image5=ImageIO.read(new File("1.gif"));
			imageHome = ImageIO.read(new File("home.jpg"));
			imageWoodWall = ImageIO.read(new File("woodWall.gif"));
			imageStoneWall = ImageIO.read(new File("stoneWall.gif"));
			imageGrass = ImageIO.read(new File("grass.gif"));
			imageRiver = ImageIO.read(new File("river.jpg"));
			imageBlood = ImageIO.read(new File("blood.png"));
			imageBuff = ImageIO.read(new File("wudi.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (isNewGame){
			guanNum = 1;
			guanQia(guanNum);
			addYelloTank();
			addBlueTank();
		} else {
			guanNum = 1;
			guanQia(guanNum);
			conGame();
		}
	}
	
	public void guanQia(int n){
		if (n == 1){
			addStoneWall();
			addWoodWall();
			addRiver();  // 墙、河要比坦克先创建
			addGrass();
			addBlood();
			addInvincible();
		}
		if (n == 2){
			addStoneWall();
			addWoodWall();
			addRiver();  // 墙、河要比坦克先创建
			addGrass();
			addBlood();
			addInvincible();
		}
	}
	
	public void addInvincible(){
		invincible = new Invincible();
		Thread t = new Thread(invincible);
		t.start();
	}
	
	public void addBlood(){
		blood = new Blood();
		Thread t = new Thread(blood);
		t.start();
	}
	
	public void addRiver(){
		for (int i=0; i<6; i++){
			if (i < 3){
				river = new River(380, 350+40*i, 20, 40);
			} else {
				river = new River(400, 350+40*(i-3), 20, 40);
			}
			rs.add(river);
		}
		river.setRs(rs);
	}
	
	public void addGrass(){
		for (int i=0; i<8; i++){
			if (i < 2){
				grass = new Grass(140+40*i, 80, 40, 40);
			} else if (i < 4){
				grass = new Grass(580+40*(i-2), 80, 40, 40);
			} else if (i < 6){
				grass = new Grass(140+40*(i-4), 120, 40, 40);
			} else {
				grass = new Grass(580+40*(i-6), 120, 40, 40);
			}
			gs.add(grass);
		}
		for (int i=0; i<8; i++){
			if (i < 2){
				grass = new Grass(220+40*i, 80, 40, 40);
			} else if (i < 4){
				grass = new Grass(500+40*(i-2), 80, 40, 40);
			} else if (i < 6){
				grass = new Grass(220+40*(i-4), 120, 40, 40);
			} else {
				grass = new Grass(500+40*(i-6), 120, 40, 40);
			}
			gs.add(grass);
		}
	}
	
	public void addStoneWall(){
		for (int i=0; i<12; i++){
			if (i < 2){
				stoneWall = new StoneWall(40*i, 260, 40, 40);
			} else if (i < 4){
				stoneWall = new StoneWall(360+40*(i-2), 260, 40, 40);
			} else if (i < 6){
				stoneWall = new StoneWall(720+40*(i-4), 260, 40, 40);
			} else if (i < 8){
				stoneWall = new StoneWall(40*(i-6), 300, 40, 40);
			} else if (i < 10){
				stoneWall = new StoneWall(360+40*(i-8), 300, 40, 40);
			} else {
				stoneWall = new StoneWall(720+40*(i-10), 300, 40, 40);
			}
			sws.add(stoneWall);
		}
		stoneWall.setWs(sws);
	}

	public void addWoodWall() {
		for(int i=0; i<8; i++){
			if (i < 4){
				woodWall = new WoodWall(320+40*i, 480, 40, 40);
			} else if (i < 6){
				woodWall = new WoodWall(320, 520+40*(i-4), 40, 40);
			} else {
				woodWall = new WoodWall(440, 520+40*(i-6), 40, 40);
			}
			wws.add(woodWall);
		}
		woodWall.setWs(wws);
	}
	
	public void addYelloTank() {
		yelloTank = new YelloTank(290, 570, 0);
		yelloTank.setBts(bts);
		yts.add(yelloTank);
	}

	public void addBlueTank() {
		for (int i=0; i<3; i++){
			BlueTank bt = null;
			switch (i){
			case 0:
				bt = new BlueTank(30, 30, 1);
				break;
			case 1:
				bt = new BlueTank(400, 30, 1);
				break;
			case 2:
				bt = new BlueTank(770, 30, 1);
				break;
			}
			
			// 此for循环要放在线程开始之前
			for (int j=0; j<sws.size(); j++){
				StoneWall stoneWall = sws.get(j);
				bt.setStoneWall(stoneWall);
			}
			for (int j=0; j<wws.size(); j++){
				WoodWall woodWall = wws.get(j);
				bt.setWoodWall(woodWall);
			}
			for (int j=0; j<rs.size(); j++){
				River river = rs.get(j);
				bt.setRiver(river);
			}
			
			bt.setYelloTank(yelloTank);
			bt.setHome(home);
			bt.setBts(bts);
			bts.add(bt);
			blueTankNum++;
			
			Thread t = new Thread(bt);
			t.start();
			if (Recorder.getBlueTankNum() == 1){
				break;
			}
		}
	}
	
	public void conGame(){
		Recorder.getRecorder();
		Vector<Coordinate> cos = new Vector<Coordinate>();
		cos = Recorder.getCos();
		Coordinate coo = null;
		BlueTank bt = null;
		for (int i=cos.size()-1; i>=0; i--){
			coo = cos.get(i);
			if (i == cos.size()-1){  // 黄坦克
				yelloTank = new YelloTank(Integer.parseInt(coo.tank_x), Integer.parseInt(coo.tank_y), Integer.parseInt(coo.direct));
				yelloTank.setBts(bts);
				yts.add(yelloTank);
			} else {  // 蓝坦克
				bt = new BlueTank(Integer.parseInt(coo.tank_x), Integer.parseInt(coo.tank_y), Integer.parseInt(coo.direct));
				
				//此for循环要放在线程开始之前
				for (int j=0; j<sws.size(); j++){
					StoneWall stoneWall = sws.get(j);
					bt.setStoneWall(stoneWall);
				}
				for (int j=0; j<wws.size(); j++){
					WoodWall woodWall = wws.get(j);
					bt.setWoodWall(woodWall);
				}
				for (int j=0; j<rs.size(); j++){
					River river = rs.get(j);
					bt.setRiver(river);
				}
				
				bt.setYelloTank(yelloTank);
				bt.setHome(home);
				bt.setBts(bts);
				bts.add(bt);
				blueTankNum++;
				
				Thread t = new Thread(bt);
				t.start();
				if (Recorder.getBlueTankNum() == 1){
					break;
				}
			}
		}
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 800, 600);  // 画板区域
		
		if (home.isLive() && Recorder.getYelloTankNum()!=0){
			if ((blueTankNum==0 && Recorder.getBlueTankNum()==0)){
				drawYouWin(g);
			} else {
				drawYelloTank(g);
				drawBlueTank(g);
				
				bombShotTank(g);
				bombShotStoneWall(g);
				
				drawInfo(g);
				
				drawHome(g);
				drawWall(g);
				drawGrass(g);
				drawRiver(g);
				drawBlood(g);
				drawInvincible(g);
			}
		} else {
			drawGameOver(g);
		}
	}

	private void drawBlueTank(Graphics g) {
		for (int i=0; i<bts.size(); i++){
			if (bts.get(i).getIsLive()){
				drawTank(bts.get(i).getX(), bts.get(i).getY(), g, bts.get(i).getDirect(), bts.get(i).getType(), false);
				for (int j=0; j<bts.get(i).bullets.size(); j++){
					if (bts.get(i).bullets.get(j).getIsLive()){
						g.fillRect(bts.get(i).bullets.get(j).getX()-2, bts.get(i).bullets.get(j).getY()-2, 4, 4);
					} else {
						bts.get(i).bullets.remove(j);
					}
				}
			}
		}
	}

	private void drawYelloTank(Graphics g) {
		if (yelloTank.getIsLive()){
			drawTank(yelloTank.getX(), yelloTank.getY(), g, yelloTank.getDirect(), yelloTank.getType(), yelloTank.isInvincible());
		}
		
		for (int i=0; i<yelloTank.bullets.size(); i++){
			if (yelloTank.bullets.get(i) != null){
				g.fillRect(yelloTank.bullets.get(i).getX()-2, yelloTank.bullets.get(i).getY()-2, 4, 4);
			}
			if (yelloTank.bullets.get(i).getIsLive() == false){
				yelloTank.bullets.remove(i);
			}
		}
	}
	
	private void drawYouWin(Graphics g) {
		g.setColor(Color.green);
		Font myFont = new Font("华文新魏", Font.BOLD, 50);
		g.setFont(myFont);
		g.drawString("You Win", 250, 200);
	}

	private void drawGameOver(Graphics g) {
		g.setColor(Color.green);
		Font myFont = new Font("华文新魏", Font.BOLD, 50);
		g.setFont(myFont);
		g.drawString("Game Over", 250, 200);
		if (!home.isLive()){
			g.drawString("基地爆炸", 250, 300);
		}
		if (!yelloTank.getIsLive()){
			g.drawString("你弱爆了", 250, 300);
		}
	}
	
	public void drawInvincible(Graphics g){
		if (invincible.isLive()){
			g.drawImage(imageBuff, invincible.getX(), invincible.getY(), invincible.getWidth(), invincible.getLength(), this);
		}
	}
	
	public void drawBlood(Graphics g){
		if (blood.isLive()){
			g.drawImage(imageBlood, blood.getX(), blood.getY(), blood.getWidth(), blood.getLength(), this);
		}
	}
	
	public void drawRiver(Graphics g){
		for (int i=0; i<rs.size(); i++){
			river = rs.get(i);
			g.drawImage(imageRiver, river.getX(), river.getY(), river.getWidth(), river.getLength(), this);
		}
	}
	
	public void drawGrass(Graphics g){
		for (int i=0; i<gs.size(); i++){
			grass = gs.get(i);
			g.drawImage(imageGrass, grass.getX(), grass.getY(), grass.getWidth(), grass.getLength(), this);
		}
	}

	public void bombShotTank(Graphics g) {
		for (int i=0; i<bomsShotTank.size(); i++){
			Bomb b = bomsShotTank.get(i);
			
			if (b.getLife() > 6){
				g.drawImage(image1, b.getX()-20, b.getY()-20, 40, 40, this);
			} else if (b.getLife() > 3){
				g.drawImage(image2, b.getX()-20, b.getY()-20, 40, 40, this);
			} else {
				g.drawImage(image3, b.getX()-20, b.getY()-20, 40, 40, this);
			}
			
			b.lifeDown();
			
			if (b.getLife() == 0){
				bomsShotTank.remove(b);
			}
		}
	}
	
	public void bombShotStoneWall(Graphics g) {
		for (int i=0; i<bomsShotStoneWall.size(); i++){
			Bomb b = bomsShotStoneWall.get(i);
			
			if (b.getLife() > 4){
				g.drawImage(image1, b.getX()-4, b.getY()-4, 8, 8, this);
			} else {
				g.drawImage(image3, b.getX()-4, b.getY()-4, 8, 8, this);
			}
			
			b.lifeDown();
			
			if (b.getLife() == 0){
				bomsShotStoneWall.remove(b);
			}
		}
	}

	public void drawHome(Graphics g) {
		if (home.isLive()){
			g.drawImage(imageHome, home.getX(), home.getY(), home.getWidth(), home.getLength(), this);
		}
	}
	
	public void drawWall(Graphics g) {
		for (int i=0; i<wws.size(); i++){
			WoodWall woodWall = (WoodWall) wws.get(i);
			if (woodWall.isLive()){
				g.drawImage(imageWoodWall, woodWall.getX(), woodWall.getY(), woodWall.getWidth()
						, woodWall.getLength(), this);
			}
		}
		for (int i=0; i<sws.size(); i++){
			StoneWall stoneWall = (StoneWall) sws.get(i);
			if (stoneWall.isLive()){
				g.drawImage(imageStoneWall, stoneWall.getX(), stoneWall.getY(), stoneWall.getWidth()
						, stoneWall.getLength(), this);
			}
		}
	}
	
	public void hitYelloTank(){
		if (!yelloTank.isInvincible()){
			for (int i=0; i<bts.size(); i++){
				BlueTank blueTank = bts.get(i);
				
				for (int j=0; j<blueTank.bullets.size(); j++){
					Bullet bullet = blueTank.bullets.get(j);
					
					for (int k=0; k<yts.size(); k++){
						if (yelloTank.getIsLive()){
							hitTank(bullet, yelloTank);
							if (!yelloTank.getIsLive()){
								Recorder.reduceYTNum();
								if (Recorder.getYelloTankNum() > 0){
									addYelloTank();
								}
								yts.remove(yelloTank);
							}
						} 
					}
				}
			}
		}
	}
	
	public void hitBlueTank(){
		for (int i=0; i<yelloTank.bullets.size(); i++){
			Bullet bullet = yelloTank.bullets.get(i);
			
			if (bullet.getIsLive()){
				for (int j=0; j<bts.size(); j++){
					BlueTank blueTank = bts.get(j);
					if (blueTank.getIsLive()){
						hitTank(bullet, blueTank);
						if (!blueTank.getIsLive()){
							Recorder.reduceBTNum();
							Recorder.addGrade();
							blueTankNum--;
							if ((blueTankNum==0 && Recorder.getBlueTankNum()>0)){
								addBlueTank();
							}
							bts.remove(blueTank);
						}
					} 
				}
			}
		}
	}
	
	public void hitTank(Bullet bullet, Tank tank){
		switch (tank.getDirect()){
		case 0:
		case 1:
			if (SAT.tank_bullet(tank, bullet))
			{
				bullet.setIsLive(false);
				tank.setIsLive(false);
				Bomb b = new Bomb(tank.getX(), tank.getY());
				bomsShotTank.add(b);
			}
			break;
		case 2:
		case 3:
			if (SAT.tank_bullet(tank, bullet))
			{
				bullet.setIsLive(false);
				tank.setIsLive(false);
				Bomb b = new Bomb(tank.getX(), tank.getY());
				bomsShotTank.add(b);
			}
			break;
		}
	}
	
	public void hitHome(){
		for (int i=0; i<bts.size(); i++){
			BlueTank blueTank = bts.get(i);
			
			for (int j=0; j<blueTank.bullets.size(); j++){
				Bullet bullet = blueTank.bullets.get(j);
				if (home.isLive()){
					if (SAT.bullet_home(bullet, home)){
						bullet.setIsLive(false);
						home.setLive(false);
						Bomb b = new Bomb(home.getX()+40, home.getY()+40);
						bomsShotTank.add(b);
					}
				} 
			}
		}
		for (int i=0; i<yelloTank.bullets.size(); i++){
			Bullet bullet = yelloTank.bullets.get(i);
			
			if (bullet.getIsLive()){
				if (home.isLive()){
					if (SAT.bullet_home(bullet, home)){
						bullet.setIsLive(false);
						home.setLive(false);
						Bomb b = new Bomb(home.getX()+40, home.getY()+40);
						bomsShotTank.add(b);
					}
				} 
			}
		}
	}
	
	public void hitWoodWall(){
		for (int i=0; i<yelloTank.bullets.size(); i++){
			Bullet bullet = yelloTank.bullets.get(i);
			
			if (bullet.getIsLive()){
				for (int j=0; j<wws.size(); j++){
					WoodWall woodWall = wws.get(j);
					if (woodWall.isLive()){
						if (SAT.bullet_wall(bullet, woodWall)){
							bullet.setIsLive(false);
							woodWall.setLive(false);
						}
					} 
				}
			}
		}
		for (int i=0; i<bts.size(); i++){
			BlueTank blueTank = bts.get(i);
			
			for (int j=0; j<blueTank.bullets.size(); j++){
				Bullet bullet = blueTank.bullets.get(j);
				
				for (int k=0; k<wws.size(); k++){
					WoodWall woodWall = wws.get(k);
					if (woodWall.isLive()){
						if (SAT.bullet_wall(bullet, woodWall)){
							bullet.setIsLive(false);
							woodWall.setLive(false);
						}
					} 
				}
			}
		}
	}
	
	public void hitStoneWall(){
		for (int i=0; i<yelloTank.bullets.size(); i++){
			Bullet bullet = yelloTank.bullets.get(i);
			
			if (bullet.getIsLive()){
				for (int j=0; j<sws.size(); j++){
					StoneWall stoneWall = sws.get(j);
					if (stoneWall.isLive()){
						if (SAT.bullet_wall(bullet, stoneWall)){
							Bomb b = new Bomb(bullet.getX(), bullet.getY());
							bomsShotStoneWall.add(b);
							bullet.setIsLive(false);
						}
					} 
				}
			}
		}
		for (int i=0; i<bts.size(); i++){
			BlueTank blueTank = bts.get(i);
			
			for (int j=0; j<blueTank.bullets.size(); j++){
				Bullet bullet = blueTank.bullets.get(j);
				
				for (int k=0; k<sws.size(); k++){
					StoneWall stoneWall = sws.get(k);
					if (stoneWall.isLive()){
						if (SAT.bullet_wall(bullet, stoneWall)){
							bullet.setIsLive(false);
							Bomb b = new Bomb(bullet.getX(), bullet.getY());
							bomsShotStoneWall.add(b);
						}
					} 
				}
			}
		}
	}
	
	public void drawTank(int x, int y, Graphics g, int direct, int type, boolean isInvincible){
		switch(type){
		case 0:  // 我方坦克
			if (!isInvincible){
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.WHITE);
			}
			break;
		case 1:  // 敌方坦克
			g.setColor(Color.BLUE);
			break;
		}
		
		switch(direct){
		case 0:  // 上
			g.fill3DRect(x-20, y-30, 10, 60, false);
			g.fill3DRect(x+10, y-30, 10, 60, false);
			g.fill3DRect(x-10, y-20, 20, 40, false);
			g.fillOval(x-10, y-10, 20, 20);
			g.drawLine(x, y, x, y-40);
			break;
		case 1:  // 下
			g.fill3DRect(x-20, y-30, 10, 60, false);
			g.fill3DRect(x+10, y-30, 10, 60, false);
			g.fill3DRect(x-10, y-20, 20, 40, false);
			g.fillOval(x-10, y-10, 20, 20);
			g.drawLine(x, y, x, y+40);
			break;
		case 2:  // 左
			g.fill3DRect(x-30, y-20, 60, 10, false);
			g.fill3DRect(x-30, y+10, 60, 10, false);
			g.fill3DRect(x-20, y-10, 40, 20, false);
			g.fillOval(x-10, y-10, 20, 20);
			g.drawLine(x, y, x-40, y);
			break;
		case 3:  // 右
			g.fill3DRect(x-30, y-20, 60, 10, false);
			g.fill3DRect(x-30, y+10, 60, 10, false);
			g.fill3DRect(x-20, y-10, 40, 20, false);
			g.fillOval(x-10, y-10, 20, 20);
			g.drawLine(x, y, x+40, y);
			break;
		}
	}
	
	public void drawInfo(Graphics g){
		drawTank(100, 750, g, 0, 0, false);
		drawTank(300, 750, g, 0, 1, false);
		g.setColor(Color.BLACK);
		g.drawString(Recorder.getYelloTankNum()+"", 160, 750);
		g.drawString(Recorder.getBlueTankNum()+"", 360, 750);
		
		// 画出成绩
		Font f = new Font("宋体", Font.BOLD, 20);
		g.setFont(f);
		g.drawString("总成绩", 950, 50);
		drawTank(900, 150, g, 0, 1, false);
		g.drawString(Recorder.getGrade() + "", 1050, 150);
		
		Font ff = new Font("Serif", Font.BOLD,30);
		g.setFont(ff);
		g.setColor(Color.BLACK);
		g.drawString("这是第"+guanNum+"关", 900, 300);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (yelloTank.getIsLive()){
			if (e.getKeyCode() == KeyEvent.VK_W){
				this.yelloTank.moveUp();
				this.yelloTank.setDirect(0);
				move();
			}
			if (e.getKeyCode() == KeyEvent.VK_S){
				this.yelloTank.moveDown();
				this.yelloTank.setDirect(1);
				move();
			}
			if (e.getKeyCode() == KeyEvent.VK_A){
				this.yelloTank.setDirect(2);
				this.yelloTank.moveLeft();
				move();
			}
			if (e.getKeyCode() == KeyEvent.VK_D){
				this.yelloTank.setDirect(3);
				this.yelloTank.moveRight();
				move();
			}
			if (e.getKeyCode() == KeyEvent.VK_J){
				if (this.yelloTank.bullets.size() < 5){
					this.yelloTank.fire();
				}
			}
			this.repaint();  // 画板重绘
		}
		
		if (e.getKeyCode() == KeyEvent.VK_H){
			if (!is_suspend){
				is_suspend = true;
			} else {
				is_suspend = false;
			}
			for (int i=0; i<bts.size(); i++){
				BlueTank bt = bts.get(i);
				bt.setSuspend(is_suspend);
				
				for (int j=0; j<bt.bullets.size(); j++){
					Bullet bullet = bt.bullets.get(j);
					bullet.setSuspend(is_suspend);
				}
			}
			for (int i=0; i<bts.size(); i++){
				BlueTank bt = bts.get(i);
				bt.setSuspend(is_suspend);
				
				for (int j=0; j<bt.bullets.size(); j++){
					Bullet bullet = bt.bullets.get(j);
					bullet.setSuspend(is_suspend);
				}
			}
			for (int i=0; i<yts.size(); i++){
				YelloTank yt = yts.get(i);
				yt.setSuspend(is_suspend);
				
				for (int j=0; j<yt.bullets.size(); j++){
					Bullet bullet = yt.bullets.get(j);
					bullet.setSuspend(is_suspend);
				}
			}
		}
	}

	private void move() {
		boolean isTouchBoundary = false;
		switch (yelloTank.getDirect()){
		case 0:
			isTouchBoundary = (yelloTank.getY()-30 <= 0);
			if (isTouchBoundary) {
				this.yelloTank.moveDown();
			}
			break;
		case 1:
			isTouchBoundary = (yelloTank.getY()+30 >= 600);
			if (isTouchBoundary) {
				this.yelloTank.moveUp();
			}
			break;
		case 2:
			isTouchBoundary = (yelloTank.getX()-30 <= 0);
			if (isTouchBoundary) {
				this.yelloTank.moveRight();
			}
			break;
		case 3:
			isTouchBoundary = (yelloTank.getX()+30 >= 800);
			if (isTouchBoundary) {
				this.yelloTank.moveLeft();
			}
			break;
		}
		if (yelloTank.isTouchBlueTank()
				|| woodWall.isTouchWall(yelloTank)
				|| stoneWall.isTouchWall(yelloTank)
				|| river.isTouchRiver(yelloTank)
				|| home.isTouchHome(yelloTank)){
			if (SAT.isJustRight){
				SAT.isJustRight = false;
			} else {
				if (yelloTank.getDirect() != SAT.direct_first){
				SAT.direct_first = 4;
				} else {
					switch (yelloTank.getDirect()){
					case 0:
						yelloTank.moveDown();
						break;
					case 1:
						yelloTank.moveUp();
						break;
					case 2:
						yelloTank.moveRight();
						break;
					case 3:
						yelloTank.moveLeft();
						break;
					}
				}
			}
		} else {
			SAT.direct_first = 4;
		}
		if (blood.isLive()){
			if (blood.isTouchBlood(yelloTank)){
				blood.setLive(false);
				Recorder.addYTNum();
			}
		}
		if (invincible.isLive()){
			if (invincible.isTouchInvincible(yelloTank)){
				invincible.setLive(false);
				yelloTank.setInvincible(true);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void run() {
		int times = 0;
		int times2 = 0;
		while (true){
			if (yelloTank.isInvincible()){
				times2++;
				if (times%100 == 0){  // 设置无敌时间,大约4秒
					yelloTank.setInvincible(false);
					times2 = 0;
				}
			}
			
			hitBlueTank();
			hitYelloTank();
			hitWoodWall();
			hitStoneWall();
			hitHome();

			try {
				Thread.sleep(100);  // 每隔100ms重绘一次
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			times++;
			if (!is_suspend){
				if (times%100 == 0 && (Recorder.getBlueTankNum()-blueTankNum)>=3){  // 每十秒出现新坦克
					addBlueTank();
				}
			}
			this.repaint();
		}
	}
}