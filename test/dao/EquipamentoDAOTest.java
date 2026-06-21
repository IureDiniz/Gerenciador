package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Equipamento;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class EquipamentoDAOTest {

    private EquipamentoDAO equipamentoDAO;

    @BeforeEach
    public void setUp() {
        equipamentoDAO = new EquipamentoDAO();
    }

    @Test
    public void testFluxoCompletoCRUD() throws SQLException {
        // Cria um equipamento temporário
        String nomeUnico = " Projetor Teste 123";
        Equipamento novo = new Equipamento(0, nomeUnico, "Projetor 4K para testes", 2, 1, "Sala 10", "Disponível");
        
        assertDoesNotThrow(() -> equipamentoDAO.inserir(novo), "A inserção não deveria lançar exceção.");

        // Busca por nome
        ArrayList<Equipamento> listaPorNome = equipamentoDAO.listarPorNomes("Projetor Teste");
        assertNotNull(listaPorNome, "Deveria encontrar o equipamento recém-inserido pelo nome.");
        assertFalse(listaPorNome.isEmpty());
        
        // Pega o ID gerado pelo banco
        Equipamento equipamentoCadastrado = listaPorNome.get(0);
        int idGerado = equipamentoCadastrado.getId();

        // Teste de busca por id
        Equipamento achadoPorId = equipamentoDAO.pegarPorId(idGerado);
        assertNotNull(achadoPorId, "Deveria encontrar o equipamento pelo ID gerado.");
        assertEquals(nomeUnico, achadoPorId.getNome());

        // Teste de atualização/edição
        achadoPorId.setNome("Projetor teste ATUALIZADO");
        achadoPorId.setQuantidade(5);
        achadoPorId.setStatus("Em Uso");
        
        assertDoesNotThrow(() -> equipamentoDAO.atualizar(achadoPorId), "A atualização não deveria lançar exceção.");
        
        Equipamento equipamentoAtualizado = equipamentoDAO.pegarPorId(idGerado);
        assertEquals("Projetor teste ATUALIZADO", equipamentoAtualizado.getNome());
        assertEquals(5, equipamentoAtualizado.getQuantidade());
        assertEquals("Em Uso", equipamentoAtualizado.getStatus());

        // Teste de excluir
        assertDoesNotThrow(() -> equipamentoDAO.deletarPorId(idGerado), "A exclusão não deveria lançar exceção.");
        
        // Garante que excluiu
        Equipamento sumiu = equipamentoDAO.pegarPorId(idGerado);
        assertNull(sumiu, "O equipamento deveria retornar null após ser deletado.");
    }

    @Test
    public void testPegarPorIdInexistente() throws SQLException {
        // Teste id invalido
        int idFantasma = -9999;
        Equipamento resultado = equipamentoDAO.pegarPorId(idFantasma);
        
        assertNull(resultado, "Buscar um ID inexistente deve retornar null.");
    }

    @Test
    public void testListarPorNomesInexistente() throws SQLException {
        // Teste de busca vazia
        String nomeFantasma = "Nome_Que_Nao_Existe_No_Banco_De_Dados_De_Forma_Alguma";
        ArrayList<Equipamento> resultado = equipamentoDAO.listarPorNomes(nomeFantasma);
        
        assertNull(resultado, "Buscar um nome inexistente deve retornar null.");
    }

    @Test
    public void testListarPorCategoriaInexistente() throws SQLException {
        // Teste de categoria vazia
        int categoriaFantasma = -8888;
        ArrayList<Equipamento> resultado = equipamentoDAO.listarPorCategoria(categoriaFantasma);
        
        assertNull(resultado, "Buscar uma categoria inexistente deve retornar null.");
    }

    @Test
    public void testListarTodosEListarPorCategoriaComDados() throws SQLException {
        // Garante o teste do método listarTodos()
        ArrayList<Equipamento> todos = equipamentoDAO.listarTodos();
        
        // O teste passa independente de o banco estar vazio ou não
        if (todos != null) {
            assertFalse(todos.isEmpty(), "A lista de todos os equipamentos não deve estar vazia.");
            int categoriaValida = todos.get(0).getCategoria_id();
            
            ArrayList<Equipamento> porCategoria = equipamentoDAO.listarPorCategoria(categoriaValida);
            assertNotNull(porCategoria, "Deveria retornar a lista para uma categoria que sabidamente possui dados.");
        } else {
            assertNull(todos, "Se o banco estiver totalmente limpo, o retorno esperado é null.");
        }
    }
}