package dao;

import java.sql.Connection;
import connection.Database;
import model.Categoria;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;

public class CategoriaDAO {
    
    private final Connection con;

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
    
     public ArrayList<Categoria> pegarTodos() throws SQLException {
        String sql = "SELECT * FROM categorias ORDER BY nome";

        PreparedStatement stmt = con.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        ArrayList<Categoria> categorias = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String nome = rs.getString("nome");

            categorias.add(new Categoria(id, nome));
        }

        return categorias;
    }
     
     public void inserir( Categoria categoria) throws SQLException{
        String sql = "INSERT INTO categorias (nome) VALUES (?)";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, categoria.getNome());

        stmt.executeUpdate();
    }
    
    public void deletarPorId(int id) throws SQLException{
        String sql = "DELETE FROM categorias WHERE id = ?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);

        stmt.executeUpdate();
    }
    
    public void atualizar(Categoria categoria) throws SQLException{
        String sql = "UPDATE categorias SET nome = ? WHERE id = ?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, categoria.getNome());
        stmt.setInt(2, categoria.getId());

        stmt.executeUpdate();
    }
}
