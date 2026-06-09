package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Database {
	private static Database INSTANCE = null;
    
    Connection connection = null;
    

    
    public Database(){
        try{
          this.connection = DriverManager.getConnection("jdbc:sqlite:tools/Estoque.db");
          
          createDatabase();
          
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Ocorreu o seguinte erro ao tentar criar uma conexão: \n" + e);
        }
    }
    
    private void createDatabase() throws SQLException {

        String createTable = """
            CREATE TABLE IF NOT EXISTS tbUser (
                USE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                USE_LOGIN TEXT NOT NULL,
                USE_PASSWORD TEXT NOT NULL,
                USE_EMAIL TEXT,
                USE_TYPE TEXT NOT NULL,
                USE_ACTIVE INTEGER NOT NULL
            );
            """;

        String insertAdmin = """
            INSERT INTO tbUser (
                USE_ID,
                USE_LOGIN,
                USE_PASSWORD,
                USE_EMAIL,
                USE_TYPE,
                USE_ACTIVE
            )
            SELECT
                1,
                'admin',
                'admin',
                NULL,
                'GERENTE',
                1
            WHERE NOT EXISTS (
                SELECT 1
                FROM tbUser
                WHERE USE_ID = 1
            );
            """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTable);
            stmt.executeUpdate(insertAdmin);
        }
    }

    
    public static Database getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Database();
        }
        return INSTANCE;
    }
    
    
    public Connection getConnection(){
        return this.connection;
    }
    
    public void closeConnection() throws SQLException{
        this.connection.close();
    }

}
