package unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dao.EquipamentoDAO;
import model.Equipamento;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class EquipamentoTest {

    private EquipamentoDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new EquipamentoDAO();
    }

    @Test
    public void testFluxoCompletoCRUD() throws SQLException {
        String nome = "Projetor Teste 123";
        Equipamento novo = new Equipamento(0, nome, "Projetor 4K para testes", 2, 1, "Sala 10", "Disponível");
        
        dao.inserir(novo);

        ArrayList<Equipamento> lista = dao.listarPorNomes("Projetor Teste");
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        
        Equipamento cadastrado = lista.get(0);
        int id = cadastrado.getId();

        Equipamento achado = dao.pegarPorId(id);
        assertNotNull(achado);
        
        assertEquals(nome, achado.getNome().trim());

        achado.setNome("Projetor teste ATUALIZADO");
        achado.setQuantidade(5);
        achado.setStatus("Em Uso");
        
        dao.atualizar(achado);
        
        Equipamento atualizado = dao.pegarPorId(id);
        assertEquals("Projetor teste ATUALIZADO", atualizado.getNome());
        assertEquals(5, atualizado.getQuantidade());
        assertEquals("Em Uso", atualizado.getStatus());

        dao.deletarPorId(id);
        
        Equipamento sumiu = dao.pegarPorId(id);
        assertNull(sumiu);
    }

    @Test
    public void testPegarPorIdInexistente() throws SQLException {
        int id = -999;
        Equipamento res = dao.pegarPorId(id);
        assertNull(res);
    }

    @Test
    public void testListarPorNomesInexistente() throws SQLException {
        String nome = "Nome_Inexistente_Teste";
        ArrayList<Equipamento> res = dao.listarPorNomes(nome);
        assertTrue(res == null || res.isEmpty());
    }

    @Test
    public void testListarPorCategoriaInexistente() throws SQLException {
        int catId = -888;
        ArrayList<Equipamento> res = dao.listarPorCategoria(catId);
        assertTrue(res == null || res.isEmpty());
    }

    @Test
    public void testListarTodosEListarPorCategoriaComDados() throws SQLException {
        ArrayList<Equipamento> todos = dao.listarTodos();
        
        if (todos != null && !todos.isEmpty()) {
            int catId = todos.get(0).getCategoria_id();
            ArrayList<Equipamento> porCat = dao.listarPorCategoria(catId);
            assertNotNull(porCat);
        }
    }
}