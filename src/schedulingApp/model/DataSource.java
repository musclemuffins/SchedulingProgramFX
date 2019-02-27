package schedulingApp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class DataSource {

    private static Connection conn = null;
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB = "U03vAG";
    private static final String URL = "jdbc:mysql://52.206.157.109/" + DB;
    private static final String USER = "U03vAG";
    private static final String PASS = "53688095382";
    private static String userLoggedIn;

    public static void open() {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.print(e.getMessage());
        }
    }

    public static void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    public static String getUserLoggedIn() {
        return userLoggedIn;
    }

    public static void setUserLoggedIn(String userLoggedIn) {
        DataSource.userLoggedIn = userLoggedIn;
    }

    public static Connection getConnection() {
        return conn;
    }
}
