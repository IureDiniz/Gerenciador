package model;

import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class EquipamentoTest {

    @Test
    public void testExisteEquipamentoComIdInexistente() throws SQLException {
        // Procura um ID que com certeza não existe no banco 
        int idInvalido = -1;

        boolean existe = Equipamento.existe_equipamento(idInvalido);


        assertFalse(existe, "O sistema não deveria encontrar um equipamento com ID negativo.");
    }

    @Test
    public void testCriacaoEGettersSetters() {
        // Garantia de cobertura
        Equipamento equipamento = new Equipamento(1, "Notebook", "Dell i7", 10, 2, "Sala 4", "Disponível");

        // Valida se o construtor e os Getters estão entregando os dados certos
        assertEquals(1, equipamento.getId());
        assertEquals("Notebook", equipamento.getNome());
        assertEquals("Dell i7", equipamento.getDescricao());
        assertEquals(10, equipamento.getQuantidade());
        assertEquals(2, equipamento.getCategoria_id());
        assertEquals("Sala 4", equipamento.getLocalizacao());
        assertEquals("Disponível", equipamento.getStatus());

        // Valida se os Setters conseguem alterar os dados
        equipamento.setQuantidade(15);
        equipamento.setStatus("Manutenção");
        
        assertEquals(15, equipamento.getQuantidade());
        assertEquals("Manutenção", equipamento.getStatus());
    }

    @Test
    public void testToStringDoEquipamento() {
        Equipamento equipamento = new Equipamento(1, "Monitor", "29 Polegadas", 5, 1, "TI", "Ativo");
        
        String textoDoEquipamento = equipamento.toString();
        
        // Garante que o método toString não está vazio e contém as informações principais
        assertNotNull(textoDoEquipamento);
        assertTrue(textoDoEquipamento.contains("Monitor"));
        assertTrue(textoDoEquipamento.contains("29 Polegadas"));
    }
}