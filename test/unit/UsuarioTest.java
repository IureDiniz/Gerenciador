package unit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import model.Usuario;

public class UsuarioTest {

    @Test
    public void testCriacaoEGettersSetters() {
        Usuario usuario = new Usuario(1, "admin", "senha123", "admin@teste.com", "Administrador", true);

        assertEquals(1, usuario.getId());
        assertEquals("admin", usuario.getLogin()); 
        assertEquals("senha123", usuario.getSenha());
        assertEquals("admin@teste.com", usuario.getEmail());
        assertEquals("Administrador", usuario.getTipo());
        assertTrue(usuario.isAtivo());
        
        String esperado = "ID - 1\nLogin - admin\nSenha - senha123\nEmail - admin@teste.com\nTipo - Administrador\nAtivo - true";
        assertEquals(esperado, usuario.toString());
    }
}