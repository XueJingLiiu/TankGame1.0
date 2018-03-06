package a;

// 分离轴算法 SeparatingAxisTheorem
public class SAT2 {
	private static int a00 = 0;  // A点坐标，即左上点坐标
	private static int a01 = 0;  // A点坐标，即左上点坐标
	private static int a10 = 0;  // A点坐标，即左上点坐标
	private static int a11 = 0;  // A点坐标，即左上点坐标
	private static int b00 = 0;  // B点坐标，即右上点坐标
	private static int b01 = 0;  // B点坐标，即右上点坐标
	private static int b10 = 0;  // B点坐标，即右上点坐标
	private static int b11 = 0;  // B点坐标，即右上点坐标
	private static int c00 = 0;  // C点坐标，即左下点坐标
	private static int c01 = 0;  // C点坐标，即左下点坐标
	private static int c10 = 0;  // C点坐标，即左下点坐标
	private static int c11 = 0;  // C点坐标，即左下点坐标
	private static int left_clearance = 1;  // 默认左间隙为1
	private static int right_clearance = 1;  // 默认右间隙为1
	private static int up_clearance = 1;  // 默认上间隙为1
	private static int down_clearance = 1;  // 默认下间隙为1
	public static int direct_first = 4;
	public static boolean isJustRight = false;  // 默认不是恰好碰撞
	
	public static boolean letItGo(Tank tank){
		int direct_now = tank.getDirect();
		
		switch (direct_first){
		case 0:
			if (direct_now == 1){
				direct_first = 4;
				tank.moveDown();  // 如果是反方向，让它走，并且立即走
				return true;
			}
			break;
		case 1:
			if (direct_now == 0){
				direct_first = 4;
				tank.moveUp();  // 如果是反方向，让它走，并且立即走
				return true;
			}
			break;
		case 2:
			if (direct_now == 3){
				direct_first = 4;
				tank.moveRight();  // 如果是反方向，让它走，并且立即走
				return true;
			}
			break;
		case 3:
			if (direct_now == 2){
				direct_first = 4;
				tank.moveLeft();  // 如果是反方向，让它走，并且立即走
				return true;
			}
			break;
		}
		return false;
	}
	
	private static boolean isTouch(){
		boolean b = false;
		left_clearance = a00 - b10;
		right_clearance = a10 - b00;
		up_clearance = a01 - c11;
		down_clearance = a11 - c01;
		
		if (left_clearance<=0 && right_clearance<=0 && up_clearance<=0 && down_clearance<=0){
			b = true;
		}
		return b;
	}
	
	private static boolean isTouchtwice(boolean b, Tank tank){
		if (b){
			if (direct_first == 4){  // 如果是第一次碰撞
				isJustRight = true;
				direct_first = tank.getDirect();
			} else {  // 如果不是第一次碰撞
				if (letItGo(tank)){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean tank_tank(Tank ta, Tank tb){
		boolean b1 = false;
		switch (ta.getDirect()){
		case 0:  // ta
		case 1:  // ta
			a00 = ta.getX() - 20;  // Xa
			a01 = ta.getY() - 30;  // Ya
			b00 = ta.getX() + 20;  // xb
			b01 = ta.getY() - 30;  // Yb
			c00 = ta.getX() - 20;  // Xc
			c01 = ta.getY() + 30;  // Yc
			switch (tb.getDirect()){
			case 0:  // tb
			case 1:  // tb
				a10 = tb.getX() - 20;  // Xa副
				a11 = tb.getY() - 30;  // Ya副
				b10 = tb.getX() + 20;  // Xb副
				b11 = tb.getY() - 30;  // Yb副
				c10 = tb.getX() - 20;  // Xc副
				c11 = tb.getY() + 30;  // Yc副
				
				b1 = isTouch();
				break;
			case 2:  // tb
			case 3:  // tb
				a10 = tb.getX() - 30;  // Xa副
				a11 = tb.getY() - 20;  // Ya副
				b10 = tb.getX() + 30;  // Xb副
				b11 = tb.getY() - 20;  // Yb副
				c10 = tb.getX() - 30;  // Xc副
				c11 = tb.getY() + 20;  // Yc副
				
				b1 = isTouch();
				break;
			}
			break;
		case 2:  // ta
		case 3:  // ta
			a00 = ta.getX() - 30;  // Xa
			a01 = ta.getY() - 20;  // Ya
			b00 = ta.getX() + 30;  // Xb
			b01 = ta.getY() - 20;  // Yb
			c00 = ta.getX() - 30;  // Xc
			c01 = ta.getY() + 20;  // Yc
			
			switch (tb.getDirect()){
			case 0:  // tb
			case 1:  // tb
				a10 = tb.getX() - 20;  // Xa副
				a11 = tb.getY() - 30;  // Ya副
				b10 = tb.getX() + 20;  // Xb副
				b11 = tb.getY() - 30;  // Yb副
				c10 = tb.getX() - 20;  // Xc副
				c11 = tb.getY() + 30;  // Yc副
				
				b1 = isTouch();
				break;
			case 2:  // tb
			case 3:  // tb
				a10 = tb.getX() - 30;  // Xa副
				a11 = tb.getY() - 20;  // Ya副
				b10 = tb.getX() + 30;  // Xb副
				b11 = tb.getY() - 20;  // Yb副
				c10 = tb.getX() - 30;  // Xc副
				c11 = tb.getY() + 20;  // Yc副
				
				b1 = isTouch();
				break;
			}
			break;
		}
		return b1;
	}
	
	public static boolean tank_bullet(Tank tank, Bullet bullet){
		boolean b1 = false;
		a00 = bullet.getX() - 2;  // 子弹A点：Xa
		a01 = bullet.getY() - 2;  // 子弹A点：Ya
		b00 = bullet.getX() + 2;  // 子弹B点：Xb
		b01 = bullet.getY() - 2;  // 子弹B点：Yb
		c00 = bullet.getX() - 2;  // 子弹C点：Xc
		c01 = bullet.getY() + 2;  // 子弹C点：Yc
		
		switch (tank.getDirect()){
		case 0:
		case 1:
			a10 = tank.getX() - 20;  // Xa
			a11 = tank.getY() - 30;  // Ya
			b10 = tank.getX() + 20;  // Xb
			b11 = tank.getY() - 30;  // Yb
			c10 = tank.getX() - 20;  // Xc
			c11 = tank.getY() + 30;  // Yc
			
			b1 = isTouch();
			break;
		case 2:
		case 3:
			a10 = tank.getX() - 30;  // Xa
			a11 = tank.getY() - 20;  // Ya
			b10 = tank.getX() + 30;  // Xb
			b11 = tank.getY() - 20;  // Yb
			c10 = tank.getX() - 30;  // Xc
			c11 = tank.getY() + 20;  // Yc
			
			b1 = isTouch();
			break;
		}
		
		return b1;
	}
	
	public static boolean tank_wall(Tank tank, Wall wall){
		boolean b1 = false;
		a00 = wall.getX();  // 墙A点：Xa
		a01 = wall.getY();  // 墙A点：Ya
		b00 = wall.getX() + wall.getWidth();  // 墙B点：Xb
		b01 = wall.getY();  // 墙B点：Yb
		c00 = wall.getX();  // 墙C点：Xc
		c01 = wall.getY() + wall.getLength();  // 墙C点：Yc
		
		switch (tank.getDirect()){
		case 0:
		case 1:
			a10 = tank.getX() - 20;  // Xa
			a11 = tank.getY() - 30;  // Ya
			b10 = tank.getX() + 20;  // Xb
			b11 = tank.getY() - 30;  // Yb
			c10 = tank.getX() - 20;  // Xc
			c11 = tank.getY() + 30;  // Yc
			
			b1 = isTouch();
			break;
		case 2:
		case 3:
			a10 = tank.getX() - 30;  // Xa
			a11 = tank.getY() - 20;  // Ya
			b10 = tank.getX() + 30;  // Xb
			b11 = tank.getY() - 20;  // Yb
			c10 = tank.getX() - 30;  // Xc
			c11 = tank.getY() + 20;  // Yc
			
			b1 = isTouch();
			break;
		}
		return b1;
	}
	
	// 取河的x轴为投影轴，坦克在河左边有左间隙，在河右边有右间隙
	public static boolean tank_river(Tank tank, River river){
		boolean b1 = false;
		a00 = river.getX();  // 河A点：Xa
		a01 = river.getY();  // 河A点：Ya
		b00 = river.getX() + river.getWidth();  // 河B点：Xb
		b01 = river.getY();  // 河B点：Yb
		c00 = river.getX();  // 河C点：Xc
		c01 = river.getY() + river.getLength();  // 河C点：Yc
		
		switch (tank.getDirect()){
		case 0:
		case 1:
			a10 = tank.getX() - 20;  // Xa
			a11 = tank.getY() - 30;  // Ya
			b10 = tank.getX() + 20;  // Xb
			b11 = tank.getY() - 30;  // Yb
			c10 = tank.getX() - 20;  // Xc
			c11 = tank.getY() + 30;  // Yc
			
			b1 = isTouch();
			break;
		case 2:
		case 3:
			a10 = tank.getX() - 30;  // Xa
			a11 = tank.getY() - 20;  // Ya
			b10 = tank.getX() + 30;  // Xb
			b11 = tank.getY() - 20;  // Yb
			c10 = tank.getX() - 30;  // Xc
			c11 = tank.getY() + 20;  // Yc
			
			b1 = isTouch();
			break;
		}
		return b1;
	}
	
	public static boolean tank_blood(Tank tank, Blood blood){
		boolean b1 = false;
		a00 = blood.getX();  // 血包A点：Xa
		a01 = blood.getY();  // 血包A点：Ya
		b00 = blood.getX() + blood.getWidth();  // 血包B点：Xb
		b01 = blood.getY();  // 血包B点：Yb
		c00 = blood.getX();  // 血包C点：Xc
		c01 = blood.getY() + blood.getLength();  // 血包C点：Yc
		
		switch (tank.getDirect()){
		case 0:
		case 1:
			a10 = tank.getX() - 20;  // Xa
			a11 = tank.getY() - 30;  // Ya
			b10 = tank.getX() + 20;  // Xb
			b11 = tank.getY() - 30;  // Yb
			c10 = tank.getX() - 20;  // Xc
			c11 = tank.getY() + 30;  // Yc
			
			b1 = isTouch();
			break;
		case 2:
		case 3:
			a10 = tank.getX() - 30;  // Xa
			a11 = tank.getY() - 20;  // Ya
			b10 = tank.getX() + 30;  // Xb
			b11 = tank.getY() - 20;  // Yb
			c10 = tank.getX() - 30;  // Xc
			c11 = tank.getY() + 20;  // Yc
			
			b1 = isTouch();
			break;
		}
		return b1;
	}
	
	public static boolean bullet_wall(Bullet bullet, Wall wall){
		boolean b1 = false;
		a00 = wall.getX();  // 墙A点：Xa
		a01 = wall.getY();  // 墙A点：Ya
		b00 = wall.getX() + wall.getWidth();  // 墙B点：Xb
		b01 = wall.getY();  // 墙B点：Yb
		c00 = wall.getX();  // 墙C点：Xc
		c01 = wall.getY() + wall.getLength();  // 墙C点：Yc
		
		a10 = bullet.getX() - 2;  // 子弹A点：Xa
		a11 = bullet.getY() - 2;  // 子弹A点：Ya
		b10 = bullet.getX() + 2;  // 子弹B点：Xb
		b11 = bullet.getY() - 2;  // 子弹B点：Yb
		c10 = bullet.getX() - 2;  // 子弹C点：Xc
		c11 = bullet.getY() + 2;  // 子弹C点：Yc
		
		b1 = isTouch();
		
		return b1;
	}
	
	public static boolean bullet_home(Bullet bullet, Home home){
		boolean b1 = false;
		a00 = home.getX();  // 家A点：Xa
		a01 = home.getY();  // 家A点：Ya
		b00 = home.getX() + home.getWidth();  // 家B点：Xb
		b01 = home.getY();  // 家B点：Yb
		c00 = home.getX();  // 家C点：Xc
		c01 = home.getY() + home.getLength();  // 家C点：Yc
		
		a10 = bullet.getX() - 2;  // 子弹A点：Xa
		a11 = bullet.getY() - 2;  // 子弹A点：Ya
		b10 = bullet.getX() + 2;  // 子弹B点：Xb
		b11 = bullet.getY() - 2;  // 子弹B点：Yb
		c10 = bullet.getX() - 2;  // 子弹C点：Xc
		c11 = bullet.getY() + 2;  // 子弹C点：Yc
		
		b1 = isTouch();
		
		return b1;
	}
	
	public static boolean tank_home(Tank tank, Home home){
		boolean b1 = false;
		a00 = home.getX();  // 家A点：Xa
		a01 = home.getY();  // 家A点：Ya
		b00 = home.getX() + home.getWidth();  // 家B点：Xb
		b01 = home.getY();  // 家B点：Yb
		c00 = home.getX();  // 家C点：Xc
		c01 = home.getY() + home.getLength();  // 家C点：Yc
		
		switch (tank.getDirect()){
		case 0:
		case 1:
			a10 = tank.getX() - 20;  // Xa
			a11 = tank.getY() - 30;  // Ya
			b10 = tank.getX() + 20;  // Xb
			b11 = tank.getY() - 30;  // Yb
			c10 = tank.getX() - 20;  // Xc
			c11 = tank.getY() + 30;  // Yc
			
			b1 = isTouch();
			break;
		case 2:
		case 3:
			a10 = tank.getX() - 30;  // Xa
			a11 = tank.getY() - 20;  // Ya
			b10 = tank.getX() + 30;  // Xb
			b11 = tank.getY() - 20;  // Yb
			c10 = tank.getX() - 30;  // Xc
			c11 = tank.getY() + 20;  // Yc
			
			b1 = isTouch();
			break;
		}
		return b1;
	}
}
