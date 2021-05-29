package jsonFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {
	
	// JDBC driver name and database URL
		static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";//"com.mysql.jdbc.Driver";
		static final String DB_URL = "jdbc:mysql://localhost:3306/mindtreelearning";

		// Database credentials
		static final String USER = "root";
		static final String PASS = "mindtree@1";
		

		PreparedStatement ps = null;
		Statement stmt =null;
		ResultSet rs = null;
		int result  = 0;
		Connection con = null;

		public int insertData(int id, String title, String price, String authors) throws ClassNotFoundException, SQLException {

			
			try {
				// STEP 2: Register JDBC driver
				Class.forName(JDBC_DRIVER);

				// STEP 3: Open a connection
				System.out.println("Connecting to database...");
				con = DriverManager.getConnection(DB_URL, USER, PASS);
				String query = "insert into airline_reservation.customer\r\n" + 
						"(cust_first_name, cust_last_name,cust_age,cust_gender,flight_id)\r\n" + 
						"values(?,?,?,?,?);";
				
				ps =con.prepareStatement(query);
				ps.setInt(1, id);
				ps.setString(2, title);
				ps.setString(3, price);
				ps.setString(4, authors);
			
				
				result = ps.executeUpdate();
				
			}catch (Exception e) {
				// Handle errors for Class.forName
				e.printStackTrace();
			}finally {
				rs.close();;
				stmt.close();
				con.close();
			}
			
			return result;
		}

}
