package a;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import util.DBUtil;

class Tank{
	private int x;
	private int y;
	private int direct;
	private int speed;
	private int type;
	private boolean isLive;
	private boolean isInvincible;
	private boolean isSuspend = false;
	public Vector<Bullet> bullets;
	// 定义一个向量，可以访问到MyPanel上的所有蓝色坦克
	public Vector<BlueTank> bts = null;
	
	public Tank(int x, int y){
		this.x = x;
		this.y = y;
		isLive = true;
		isInvincible = false;
		bullets = new Vector<Bullet>();
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getX(){
		return x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getY(){
		return y;
	}
	
	public void setDirect(int theDirect){
		direct = theDirect;
	}
	
	public int getDirect(){
		return direct;
	}

	public void setSpeed(int theSpeed){
		speed = theSpeed;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public void setType(int theType){
		type = theType;
	}
	
	public int getType(){
		return type;
	}
	
	public void setIsLive(boolean theIsLive){
		isLive = theIsLive;
	}
	
	public boolean getIsLive(){
		return isLive;
	}
	
	public boolean isInvincible() {
		return isInvincible;
	}
	
	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}
	
	public boolean isSuspend() {
		return isSuspend;
	}

	public void setSuspend(boolean isSuspend) {
		this.isSuspend = isSuspend;
	}

	public void setBts(Vector<BlueTank> theBts){
		bts = theBts;
	}
	
	public void moveUp(){
		y -= speed;
	}
	
	public void moveDown(){
		y += speed;
	}
	
	public void moveLeft(){
		x -= speed;
	}
	
	public void moveRight(){
		x += speed;
	}
	
	public boolean isTouchBlueTank(){
		boolean b = false;
		
		for (int i=0; i<bts.size(); i++){
			BlueTank bt = bts.get(i);
			if (this != bt){
				if (this instanceof YelloTank){
					b = SAT.tank_tank(this, bt);
				} else {
					b = SAT2.tank_tank(this, bt);
				}
			}
			if (b){
				break;
			}
		}
		
		return b;
	}
}

class YelloTank extends Tank{
	private Bullet bullet;

	public YelloTank(int x, int y, int direct) {
		super(x, y);
		setSpeed(5);
		setType(0);
		setDirect(direct);
	}
	
	public void setBts(Vector<BlueTank> theBts){
		bts = theBts;
	}
	
	public void fire(){
		if (!this.isSuspend()){
			switch (this.getDirect()){
			case 0:
				bullet = new Bullet(this.getX(), this.getY()-40, this.getDirect());
				bullet.setSpeed(15);
				bullets.add(bullet);
				break;
			case 1:
				bullet = new Bullet(this.getX(), this.getY()+40, this.getDirect());
				bullet.setSpeed(15);
				bullets.add(bullet);
				break;
			case 2:
				bullet = new Bullet(this.getX()-40, this.getY(), this.getDirect());
				bullet.setSpeed(15);
				bullets.add(bullet);
				break;
			case 3:
				bullet = new Bullet(this.getX()+40, this.getY(), this.getDirect());
				bullet.setSpeed(15);
				bullets.add(bullet);
				break;
			}
			Thread t = new Thread(bullet);
			t.start();
		}
	}
	
}

class BlueTank extends Tank implements Runnable{
	
	YelloTank yelloTank = null;
	Home home = null;
	
	WoodWall woodWall = null;
	StoneWall stoneWall = null;
	River river = null;
	
	public static int bulletSpeed = 10;
	
	public BlueTank(int x, int y, int direct) {
		super(x, y);
		setSpeed(5);
		setType(1);
		setDirect(direct);
	}
	
	public void setWoodWall(WoodWall woodWall) {
		this.woodWall = woodWall;
	}

	public void setStoneWall(StoneWall stoneWall) {
		this.stoneWall = stoneWall;
	}

	public void setRiver(River river) {
		this.river = river;
	}

	public void setYelloTank(YelloTank theYelloTank){
		yelloTank = theYelloTank;
	}
	
	public void setHome(Home theHome){
		home = theHome;
	}

	public boolean isTouchYelloTank(){
		boolean b = false;
		
		b = SAT2.tank_tank(this, yelloTank);
		
		return b;
	}
	
	private void move() {
		boolean isTouchBoundary = false;
		
		switch (this.getDirect()){
		case 0:
			this.moveUp();
			break;
		case 1:
			this.moveDown();
			break;
		case 2:
			this.moveLeft();
			break;
		case 3:
			this.moveRight();
			break;
		}
		
		switch (this.getDirect()){
		case 0:
			isTouchBoundary = (this.getY()-80 <= 0);  // 多出来的50是给刚新建的坦克的空间，避免新旧坦克碰撞
			if (isTouchBoundary) {
				this.moveDown();
			}
			break;
		case 1:
			isTouchBoundary = (this.getY()+30 >= 600);
			if (isTouchBoundary) {
				this.moveUp();
			}
			break;
		case 2:
			isTouchBoundary = (this.getX()-30 <= 0);
			if (isTouchBoundary) {
				this.moveRight();
			}
			break;
		case 3:
			isTouchBoundary = (this.getX()+30 >= 800);
			if (isTouchBoundary) {
				this.moveLeft();
			}
			break;
		}
		if (this.isTouchYelloTank()
				|| this.isTouchBlueTank()
				|| woodWall.isTouchWall(this)
				|| stoneWall.isTouchWall(this)
				|| river.isTouchRiver(this)
				|| home.isTouchHome(this)){
			switch (this.getDirect()){
			case 0:
				this.moveDown();
				break;
			case 1:
				this.moveUp();
				break;
			case 2:
				this.moveRight();
				break;
			case 3:
				this.moveLeft();
				break;
			}
		}
	}

	@Override
	public void run() {
		int times = 0;
		while (true){
			for (int i=0; i<30; i++){  // 移动30下再换方向
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (!this.isSuspend()){
					this.move();
				}
			}
			
			// 随机产生新方向
			if (!this.isSuspend()){
				this.setDirect((int)(Math.random()*4));
			}
			
			if (!this.getIsLive()){
				break;
			}
			
			times++;
			
			if (times % 2 == 0) {
				if (this.getIsLive()){
					if (!this.isSuspend()){
						if (this.bullets.size() < 1){
							Bullet bullet = null;
							switch (this.getDirect()){
							case 0:
								bullet = new Bullet(this.getX(), this.getY()-40, this.getDirect());
								bullet.setSpeed(bulletSpeed);
								bullets.add(bullet);
								break;
							case 1:
								bullet = new Bullet(this.getX(), this.getY()+40, this.getDirect());
								bullet.setSpeed(bulletSpeed);
								bullets.add(bullet);
								break;
							case 2:
								bullet = new Bullet(this.getX()-40, this.getY(), this.getDirect());
								bullet.setSpeed(bulletSpeed);
								bullets.add(bullet);
								break;
							case 3:
								bullet = new Bullet(this.getX()+40, this.getY(), this.getDirect());
								bullet.setSpeed(bulletSpeed);
								bullets.add(bullet);
								break;
							}
							Thread t = new Thread(bullet);
							t.start();
						}
					}
				}
			}
		}
	}
	
}

class Bullet implements Runnable{
	private int x;
	private int y;
	private int direct;
	private int speed;
	private boolean isLive;
	private boolean isSuspend;
	
	public Bullet(int x, int y, int direct){
		this.x = x;
		this.y = y;
		this.direct = direct;
		isLive = true;
		isSuspend = false;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getX(){
		return x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getY(){
		return y;
	}
	
	public void setDirect(int theDirect){
		this.direct = theDirect;
	}
	
	public int getDirect(){
		return direct;
	}
	
	public void setSpeed(int theSpeed){
		speed = theSpeed;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public void setIsLive(boolean theIsLive){
		isLive = theIsLive;
	}
	
	public boolean getIsLive(){
		return isLive;
	}

	public boolean isSuspend() {
		return isSuspend;
	}

	public void setSuspend(boolean isSuspend) {
		this.isSuspend = isSuspend;
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(50);  // 每隔50ms子弹改变一次坐标
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (!isSuspend){
				switch(direct){
				case 0:
					y -= speed;
					break;
				case 1:
					y += speed;
					break;
				case 2:
					x -= speed;
					break;
				case 3:
					x += speed;
					break;
				}
			}
			
			if ((x>=800) || (x<=0) || (y>=600) || (y<=0)){
				isLive = false;
				break;
			}
		}
	}
}

class Bomb{
	private int x;
	private int y;
	private boolean isLive;
	private int life;
	
	public Bomb(int x, int y){
		this.x = x;
		this.y = y;
		isLive = true;
		life = 9;
	}
	
	public void lifeDown(){
		if (life > 0){
			life--;
		} else {
			this.setLive(false);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
}

class Recorder{
	private static int blueTankNum = 10;
	private static int yelloTankNum = 3;
	private static int grade = 0;
	private static Vector<BlueTank> bts = new Vector<BlueTank>();
	private static Vector<YelloTank> yts = new Vector<YelloTank>();
	private static Vector<Coordinate> cos = new Vector<Coordinate>();
	
	public static int getBlueTankNum() {
		return blueTankNum;
	}
	public static void setBlueTankNum(int blueTankNum) {
		Recorder.blueTankNum = blueTankNum;
	}
	public static int getYelloTankNum() {
		return yelloTankNum;
	}
	public static void setYelloTankNum(int yelloTankNum) {
		Recorder.yelloTankNum = yelloTankNum;
	}
	
	public static int getGrade() {
		return grade;
	}
	public static void setGrade(int grade) {
		Recorder.grade = grade;
	}
	public static Vector<BlueTank> getBts() {
		return bts;
	}
	public static void setBts(Vector<BlueTank> bts) {
		Recorder.bts = bts;
	}
	public static Vector<YelloTank> getYts() {
		return yts;
	}
	public static void setYts(Vector<YelloTank> yts) {
		Recorder.yts = yts;
	}
	public static Vector<Coordinate> getCos() {
		return cos;
	}
	public static void setCos(Vector<Coordinate> cos) {
		Recorder.cos = cos;
	}
	public static void reduceBTNum(){
		blueTankNum--;
	}
	public static void reduceYTNum(){
		yelloTankNum--;
	}
	public static void addGrade(){
		grade += 100;
	}
	public static void addYTNum(){
		yelloTankNum++;
	}
	
	public static void getRecorder(){
		Scanner inputStream = null;
		StringTokenizer position = null;
		Coordinate co = null;
		
		try {
			inputStream = new Scanner(new FileInputStream("save.txt"));
			while (inputStream.hasNextLine()){
				String line = inputStream.nextLine();
				position = new StringTokenizer(line);
				String tank_x = position.nextToken();
				String tank_y = position.nextToken();
				String direct = position.nextToken();
				co = new Coordinate(tank_x, tank_y, direct);
				cos.add(co);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		inputStream.close();
	}
	
	public static void keepRecorder(){
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(new FileOutputStream("save.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int i=0; i<bts.size(); i++){
			BlueTank bt = bts.get(i);
			
			if (bt.getIsLive()){
				String position = bt.getX() + " " + bt.getY() + " " + bt.getDirect();
				outputStream.println(position);
			}
		}
		
		for (int i=0; i<yts.size(); i++){
			YelloTank yt = yts.get(i);
			
			if (yt.getIsLive()){
				String position = yt.getX() + " " + yt.getY() + " " + yt.getDirect();
				outputStream.println(position);
			}
		}
		
		outputStream.close();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  // HH表示24小时制，hh表示12小时制
		String time = sdf.format(new Date());
		DBUtil.executeUpdate("insert into score (score,time) values(?,?)",
				new Object[]{grade, time}); 
	}
}

class Home{
	private int x;
	private int y;
	private int width;
	private int length;
	private boolean isLive;
	
	public Home(int x, int y, int theWidth, int theLength){
		this.x = x;
		this.y = y;
		width = theWidth;
		length = theLength;
		isLive = true;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public boolean isTouchHome(Tank tank){
		boolean b = false;
		if (tank instanceof YelloTank){
			b = SAT.tank_home(tank, this);
		} else {
			b = SAT2.tank_home(tank, this);
		}
		return b;
	}
}

class Wall{
	private int x;
	private int y;
	private int width;
	private int length;
	private boolean isLive;
	
	public Wall(int x, int y, int theWidth, int theLength){
		this.x = x;
		this.y = y;
		width = theWidth;
		length = theLength;
		isLive = true;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
}

class WoodWall extends Wall{
	public Vector<WoodWall> ws = new Vector<WoodWall>();

	public WoodWall(int x, int y, int theWidth, int theLength) {
		super(x, y, theWidth, theLength);
	}
	
	public Vector<WoodWall> getWs() {
		return ws;
	}

	public void setWs(Vector<WoodWall> ws) {
		this.ws = ws;
	}

	public boolean isTouchWall(Tank tank){
		boolean b = false;
		for (int i=0; i<ws.size(); i++){
			Wall w = ws.get(i);
			if (w.isLive()){
				if (tank instanceof YelloTank){
					b = SAT.tank_wall(tank, w);
				} else {
					b = SAT2.tank_wall(tank, w);
				}
				if (b){
					return true;
				}
			}
		}
		return false;
	}
}

class StoneWall extends Wall{
	public Vector<StoneWall> ws = new Vector<StoneWall>();

	public StoneWall(int x, int y, int theWidth, int theLength) {
		super(x, y, theWidth, theLength);
	}

	public Vector<StoneWall> getWs() {
		return ws;
	}

	public void setWs(Vector<StoneWall> sws) {
		this.ws = sws;
	}
	
	public boolean isTouchWall(Tank tank){
		boolean b = false;
		for (int i=0; i<ws.size(); i++){
			Wall w = ws.get(i);
			if (w.isLive()){
				if (tank instanceof YelloTank){
					b = SAT.tank_wall(tank, w);
				} else {
					b = SAT2.tank_wall(tank, w);
				}
				if (b){
					return true;
				}
			}
		}
		return false;
	}
}

class Grass{
	private int x;
	private int y;
	private int width;
	private int length;
	public Grass(int x, int y, int width, int length) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.length = length;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}

class River{
	private int x;
	private int y;
	private int width;
	private int length;
	public Vector<River> rs = new Vector<River>();
	
	public River(int x, int y, int width, int length) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.length = length;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	public void setRs(Vector<River> rs){
		this.rs = rs;
	}
	
	public boolean isTouchRiver(Tank tank){
		boolean b = false;
		for (int i=0; i<rs.size(); i++){
			River river = rs.get(i);
			if (tank instanceof YelloTank){
				b = SAT.tank_river(tank, river);
			} else {
				b = SAT2.tank_river(tank, river);
			}
			if (b){
				break;
			}
		}
		return b;
	}
}

class Blood implements Runnable{
	private int x;
	private int y;
	private int width;
	private int length;
	private boolean isLive;
	
	public Blood() {
		x = (int)(Math.random()*721);
		y = (int)(Math.random()*521);
		width = 80;
		length = 80;
		isLive = false;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	public boolean isTouchBlood(YelloTank yelloTank){
		boolean b = false;
		if (isLive()){
			b = SAT.tank_blood(yelloTank, this);
		}
		return b;
	}
	
	@Override
	public void run() {
		while (true){
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!isLive){
				isLive = true;
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (isLive){
				isLive = false;
			}
			x = (int)(Math.random()*721);
			y = (int)(Math.random()*521);
		}
	}
}

class Invincible implements Runnable{
	private int x;
	private int y;
	private int width;
	private int length;
	private boolean isLive;
	
	public Invincible() {
		x = (int)(Math.random()*721);
		y = (int)(Math.random()*521);
		width = 80;
		length = 80;
		isLive = false;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	public boolean isTouchInvincible(YelloTank yelloTank){
		boolean b = false;
		if (isLive()){
			b = SAT.tank_invincible(yelloTank, this);
		}
		return b;
	}
	
	@Override
	public void run() {
		while (true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!isLive){
				isLive = true;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (isLive){
				isLive = false;
			}
			x = (int)(Math.random()*721);
			y = (int)(Math.random()*521);
		}
	}
}

class Coordinate{
	String tank_x;
	String tank_y;
	String direct;
	public Coordinate(String tank_x, String tank_y, String direct) {
		super();
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		this.direct = direct;
	}
}