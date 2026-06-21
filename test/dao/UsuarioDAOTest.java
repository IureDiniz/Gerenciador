package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Usuario;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDAOTest {

    private UsuarioDAO usuarioDAO;

    @BeforeEach
    public void setUp() {

        usuarioDAO = new UsuarioDAO();
    }

    @Test
    public void testPegarPorNomeUsuarioExistente() throws SQLException {

        String nomeExistente = "Administrador";

        Usuario usuario = usuarioDAO.pegarPorNome(nomeExistente);

        assertNotNull(usuario, "O usuário deveria ter sido encontrado no banco.");
        assertEquals(nomeExistente, usuario.getLogin(), "O nome retornado deve ser igual ao buscado.");

    }

    @Test
    public void testPegarPorNomeUsuarioInexistente() throws SQLException {

        String nomeInexistente = "usuario_fantasma_teste_999";

        Usuario usuario = usuarioDAO.pegarPorNome(nomeInexistente);

        assertNull(usuario, "O resultado deveria ser null para um usuário que não existe.");

    }
}