package unit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import model.Categoria;

public class CategoriaTest {

    @Test
    public void testCriacaoEGettersSetters() {
        Categoria categoria = new Categoria(1, "Ferramentas");

        assertEquals(1, categoria.getId());
        assertEquals("Ferramentas", categoria.getNome());

        categoria.setNome("Eletrônicos");
        assertEquals("Eletrônicos", categoria.getNome());
    }
}