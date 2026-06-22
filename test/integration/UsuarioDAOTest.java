package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dao.UsuarioDAO;
import model.Usuario;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDAOTest {

    private UsuarioDAO dao;

    @BeforeEach
    public void setUp() {
        dao = new UsuarioDAO();
    }

    @Test
    public void testPegarPorNomeUsuarioExistente() throws SQLException {
        String nome = "Administrador";
        Usuario user = dao.pegarPorNome(nome);

        assertNotNull(user);
        assertEquals(nome, user.getLogin());
    }

    @Test
    public void testPegarPorNomeUsuarioInexistente() throws SQLException {
        String nome = "usuario_inexistente_123";
        Usuario user = dao.pegarPorNome(nome);
        assertNull(user);
    }
}