package integration;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import dao.CategoriaDAO;
import model.Categoria;

public class CategoriaDAOTest {

    private CategoriaDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new CategoriaDAO();
    }

    @Test
    public void testCicloCategoria() throws SQLException {
        Categoria novaCategoria = new Categoria(0, "Categoria Teste Automatizado");
        dao.inserir(novaCategoria);

        ArrayList<Categoria> lista = dao.pegarTodos();
        Categoria aux = null;
        
        for (Categoria c : lista) {
            if (c.getNome().equals("Categoria Teste Automatizado")) {
                aux = c;
                break;
            }
        }
        
        assertNotNull(aux, "Erro ao inserir");

        int id = aux.getId();
        Categoria doBanco = dao.pegarPorId(id);
        assertEquals("Categoria Teste Automatizado", doBanco.getNome());

        doBanco.setNome("Categoria Atualizada com Sucesso");
        dao.atualizar(doBanco);

        Categoria atualizada = dao.pegarPorId(id);
        assertEquals("Categoria Atualizada com Sucesso", atualizada.getNome());

        dao.deletarPorId(id);
        Categoria deletada = dao.pegarPorId(id);
        assertNull(deletada, "Erro ao deletar");
    }
}