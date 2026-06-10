package dao;

import java.sql.Connection;
import connection.Database;
import model.Categoria;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class CategoriaDAO {
    
    private Connection con;

    public CategoriaDAO() {
        con = Database.getInstance().getConnection();
    }
    
    public Categoria pegarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM categorias WHERE id = ?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int i = rs.getInt("id");
            String nome = rs.getString("nome");

            Categoria e = new Categoria(i, nome);
            
            return e;

        } else {
            return null;
        }
    }
    
     public Categoria pegarTodos() throws SQLException {
        String sql = "SELECT * FROM categorias";

        PreparedStatement stmt = con.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            String nome = rs.getString("nome");

            Categoria e = new Categoria(id, nome);
            
            return e;

        } else {
            return null;
        }
    }
     
     public void inserir( Categoria categoria) throws SQLException{
        String sql = "INSERT INTO categorias VALUES(?)";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, categoria.getNome());

        ResultSet rs = stmt.executeQuery();
    }
    
    public void deletarPorId(int id) throws SQLException{
        String sql = "DELETE FROM categorias WHERE id = ?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
    }
    
    public void atualizar(Categoria categoria) throws SQLException{
        String sql = "UPDATE equipamentos SET nome = ? WHERE id = ?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, categoria.getNome());
        stmt.setInt(2, categoria.getId());

        ResultSet rs = stmt.executeQuery();
    }
}
