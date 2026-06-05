package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connection.Database;
import model.User;

public class UserDAO {
private Connection con;
    
    public UserDAO(){
        con = Database.getInstance().getConnection();
    }
    
    public User getbyLogin(String login) throws SQLException{
        String sql = "SELECT * FROM tbUser WHERE USE_LOGIN = ?";
        
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, login);
        
        ResultSet rs = stmt.executeQuery();
        
        
        if (rs.next()){
            int id = rs.getInt("USE_ID");
            String logi = rs.getString("USE_LOGIN");
            String senha = rs.getString("USE_PASSWORD");
            String tipo = rs.getString("USE_TYPE");
            String email = rs.getString("USE_EMAIL");
            boolean ativo;
            
            if(rs.getInt("USE_ACTIVE") == 0){
                ativo = false;
            } else{
                ativo = true;
            }
            
            User u = new User(id, logi, senha, email, tipo, ativo);
           
            return u;
            
        } else {
            return null;
        }
    }
}
