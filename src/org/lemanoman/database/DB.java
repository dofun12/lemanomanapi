package org.lemanoman.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DB {
	private Connection connection = null;
	private String user = "";

	public DB(String host,String port,String database,String user,String passwd) {
		initConnection(host,port,database,user,passwd);
	}
	
	public DB(String host,String database,String user,String passwd) {
		initConnection(host,"3306",database,user,passwd);
	}
	
	public DB() {
		initConnection("192.168.25.104","3306","magicmanager","teste","teste");
	}

	public void initConnection(String host,String port,String database,String user,String passwd) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			this.connection = DriverManager.getConnection(
					"jdbc:mysql://"+host+":"+port+"/"+database+"?characterEncoding=UTF-8&useSSL=false",user,
					passwd);

		} catch (SQLException sqlE) {
			sqlE.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String sql) {
		try {
			if (connection != null && (!connection.isClosed())) {
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				statement.close();
				return rs;
			} else {
				System.err.println("Conexao Fechada");
			}
		} catch (SQLException e) {
			System.err.println("Erro ao executar a query " + sql);
			e.printStackTrace();
		}
		return null;
	}

	public boolean executeUpdate(String query, Map<String, Object> params) {
		PreparedStatement ps = null;
		try {

			Pattern p = Pattern.compile(":(.*?)[^A-z0-9]");
			Matcher m = p.matcher(query);
			connection.setAutoCommit(false);

			String tempQuery = query.replaceAll(":(.*?)[^A-z0-9]", "?,");
			tempQuery = tempQuery.replaceAll(",\\)",")");
			ps = connection.prepareStatement(tempQuery);

			int index = 0;
			
			Map<Integer,String> indexes = new HashMap<Integer,String>();
			while (m.find()) {
				String param = m.group(1);
				index++;
				indexes.put(index, param);
			}
			
			for(Integer i:indexes.keySet()){
				String param = indexes.get(i);
				Object value = params.get(param);
				if (value instanceof String) {
					ps.setString(i, (String)value);
				}else if(value instanceof Long){
					ps.setLong(i, (Long)value);
				}else if(value instanceof Integer){
					ps.setInt(i, (Integer)value);
				}
			}
			ps.executeUpdate();
			connection.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			
		}
		return false;
	}

	public void closeConnection() {
		try {
			if (this.connection != null && (!connection.isClosed())) {
				connection.close();
			}
		} catch (SQLException e) {
			System.err.println("Erro ao fechar a connection");
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		return connection;
	}
}
