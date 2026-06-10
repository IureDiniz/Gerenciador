package dao;

import connection.Database;
import java.sql.Connection;
import java.util.ArrayList;
import model.Equipamento;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class EquipamentoDAO {
    private Connection con;
    
    public EquipamentoDAO(){
        con = Database.getInstance().getConnection();
    }
        
    public ArrayList<Equipamento> listarPorNomes(String nome) throws SQLException{
        String sql = "SELECT * FROM equipamentos WHERE nome = ?";
        
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, nome);
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
           int id = rs.getInt("id");
           String nom = rs.getString("nome");
           String descricao = rs.getString("descricao");
           int quantidade = rs.getInt("quantidade");
           
        }
    }
}