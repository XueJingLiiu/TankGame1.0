package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;



public class DBUtil {

	//得到数据库连接
	public static Connection getConnection(){
		
		//1.得到properties文件
		InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
		Properties p = new Properties();
		Connection con = null;
		try {
			p.load(is);
			//2.加载驱动类
			Class.forName(p.getProperty("drivername"));
			//3.得到数据库连接
			con = DriverManager.getConnection(p.getProperty("url"),p.getProperty("username"),p.getProperty("password"));
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
	}
	
	//select * from user where id=?
	//select * from user where username=? and password=?
	public static List<Map<String,String>> executeQuery(String sql,Object[] params){
		Connection con = getConnection();
		ResultSet rs = null;
		List<Map<String,String>> resultlist = null;
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			//遍历传入的参数
			if(params!=null){
				if(params.length!=0){
					for(int i=0;i<params.length;i++){
						//将问号替换成参数
						ps.setObject(i+1, params[i]);
					}
				}
			}
			//执行查询
			rs = ps.executeQuery();
			//得到结果集元数据
			ResultSetMetaData md =  rs.getMetaData();
			resultlist = new ArrayList<Map<String,String>>();
			//遍历结果集
			while(rs.next()){
				//map对应一行记录
				Map<String,String> map = new HashMap<String, String>();
				for(int i=1;i<=md.getColumnCount();i++){
					map.put(md.getColumnName(i), rs.getString(i));
				}
				resultlist.add(map);
			}
			
			close(con,ps,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultlist;
		
	}
	
	public static boolean executeUpdate(String sql,Object[] params){
		Connection con = getConnection();
		boolean result = true;
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			//遍历传入的参数
			if(params!=null){
				if(params.length!=0){
					for(int i=0;i<params.length;i++){
						//将问号替换成参数
						ps.setObject(i+1, params[i]);
					}
				}
			}
			
			//执行更新
			int row = ps.executeUpdate();
			result = row>0;
			
			close(con,ps,null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	
	public static void close(Connection con,PreparedStatement ps,ResultSet rs){
		try {
			if(rs!=null){
					rs.close();
			}
			if(ps!=null){
					ps.close();
			}
			if(con!=null){
					con.close();
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
