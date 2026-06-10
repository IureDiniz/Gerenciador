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
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL CHECK(length(nome) <= 100),
                email TEXT NOT NULL UNIQUE CHECK(length(email) <= 100),
                senha TEXT NOT NULL CHECK(length(senha) <= 255),
                tipo TEXT NOT NULL CHECK(tipo IN ('Funcionario', 'Supervisor', 'Gerente')),
                ativo INTEGER NOT NULL DEFAULT 1
            );
            """;

        String insertAdmin = """
            INSERT INTO usuarios (
                id,
                nome,
                email,
                senha,
                tipo,
                ativo
            )
            SELECT
                1,
                'Administrador',
                'admin@empresa.com',
                '123456',
                'Supervisor',
                1
            WHERE NOT EXISTS (
                SELECT 1
                FROM usuarios
                WHERE id = 1
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
