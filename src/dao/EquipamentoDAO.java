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

    public EquipamentoDAO() {
        con = Database.getInstance().getConnection();
    }

    public ArrayList<Equipamento> listarPorNomes(String nome) throws SQLException {
        String sql = "SELECT * FROM equipamentos WHERE nome = ?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, nome);

        ResultSet rs = stmt.executeQuery();
        ArrayList<Equipamento> equips = new ArrayList<Equipamento>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String nom = rs.getString("nome");
            String descricao = rs.getString("descricao");
            int quantidade = rs.getInt("quantidade");
            int categoria = rs.getInt("categoria_id");
            String localizacao = rs.getString("localizacao");
            String status = rs.getString("status");

            Equipamento e = new Equipamento(id, nom, descricao, quantidade, categoria, localizacao, status);

            equips.add(e);
        }

        if (equips.isEmpty()) {
            return null;
        }

        return equips;
    }

    public Equipamento pegarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM equipamentos WHERE id = ?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int i = rs.getInt("id");
            String nom = rs.getString("nome");
            String descricao = rs.getString("descricao");
            int quantidade = rs.getInt("quantidade");
            int categoria = rs.getInt("categoria_id");
            String localizacao = rs.getString("localizacao");
            String status = rs.getString("status");

            Equipamento e = new Equipamento(id, nom, descricao, quantidade, categoria, localizacao, status);
            
            return e;

        } else {
            return null;
        }
    }
}
