package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public ArrayList<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        ArrayList<Usuario> usuarios = new ArrayList<>();

        while (rs.next()) {
            usuarios.add(criarUsuario(rs));
        }

        return usuarios;
    }

    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha, tipo, ativo) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, usuario.getLogin());
        stmt.setString(2, usuario.getEmail());
        stmt.setString(3, usuario.getSenha());
        stmt.setString(4, usuario.getTipo());
        stmt.setInt(5, usuario.isAtivo() ? 1 : 0);
        stmt.executeUpdate();
    }

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, tipo = ?, ativo = ? WHERE id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, usuario.getLogin());
        stmt.setString(2, usuario.getEmail());
        stmt.setString(3, usuario.getSenha());
        stmt.setString(4, usuario.getTipo());
        stmt.setInt(5, usuario.isAtivo() ? 1 : 0);
        stmt.setInt(6, usuario.getId());
        stmt.executeUpdate();
    }

    public void inativarPorId(int id) throws SQLException {
        String sql = "UPDATE usuarios SET ativo = 0 WHERE id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public void deletarPorId(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    private Usuario criarUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String senha = rs.getString("senha");
        String tipo = rs.getString("tipo");
        String email = rs.getString("email");
        boolean ativo = rs.getInt("ativo") != 0;

        return new Usuario(id, nome, senha, email, tipo, ativo);
    }
}
