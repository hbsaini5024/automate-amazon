package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DBUtils {
	
	 public static int getCartId(Connection conn, int userId, String date) throws SQLException {
	        String query = "SELECT cart_id FROM carts WHERE user_id = ? AND cart_date = ?";
	        PreparedStatement stmt = conn.prepareStatement(query);
	        stmt.setInt(1, userId);
	        stmt.setString(2, date);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("cart_id");
	        }
	        return -1; // not found
	    }
	 
	 public static List<String> getCartProducts(Connection conn, int cartId) throws SQLException {
	        List<String> products = new ArrayList<>();
	        String query = "SELECT product_id, quantity FROM cart_products WHERE cart_id = ?";
	        PreparedStatement stmt = conn.prepareStatement(query);
	        stmt.setInt(1, cartId);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            products.add("ProductID=" + rs.getInt("product_id") + ", Qty=" + rs.getInt("quantity"));
	        }
	        return products;
	    }

}
