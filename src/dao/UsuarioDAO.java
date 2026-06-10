package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connection.Database;
import model.Usuario;

public class UsuarioDAO {
    private Connection con;
    
    public UsuarioDAO(){
        con = Database.getInstance().getConnection();
    }
    
    public Usuario pegarPorNome(String nome) throws SQLException{
        String sql = "SELECT * FROM usuarios WHERE nome = ?";
        
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, nome);
        
        ResultSet rs = stmt.executeQuery();
        
        
        if (rs.next()){
            int id = rs.getInt("id");
            String nom = rs.getString("nome");
            String senha = rs.getString("senha");
            String tipo = rs.getString("tipo");
            String email = rs.getString("email");
            boolean ativo;
            
            if(rs.getInt("ativo") == 0){
                ativo = false;
            } else{
                ativo = true;
            }
            
            Usuario u = new Usuario(id, nom, senha, email, tipo, ativo);
           
            return u;
            
        } else {
            return null;
        }
    }
}
