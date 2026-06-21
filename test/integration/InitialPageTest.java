package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import gui.InitialPage; 

public class InitialPageTest {

    private InitialPage initialPage;

    @BeforeEach
    public void setUp() {
        initialPage = new InitialPage();
    }

    @Test
    public void testLoadUsers() {
        try {
            Method loadUsersMethod = InitialPage.class.getDeclaredMethod("loadUsers");
            loadUsersMethod.setAccessible(true); 

            loadUsersMethod.invoke(initialPage);

            Field tableModelField = InitialPage.class.getDeclaredField("userTableModel");
            tableModelField.setAccessible(true);
            
            DefaultTableModel userTableModel = (DefaultTableModel) tableModelField.get(initialPage);

            assertNotNull(userTableModel, "O modelo da tabela de usuários não deveria ser nulo.");
            
            assertTrue(userTableModel.getRowCount() >= 0, "O método foi executado e a tabela foi acessada sem erros.");
            
            System.out.println("Linhas carregadas do banco real: " + userTableModel.getRowCount());

        } catch (Exception e) {
            fail("Ocorreu um erro ao tentar testar a tela com o banco de dados: " + e.getMessage());
        }
    }

    @Test
    public void testLoadCategories() {
        try {
            Method loadCategoriesMethod = InitialPage.class.getDeclaredMethod("loadCategories");
            loadCategoriesMethod.setAccessible(true);
            loadCategoriesMethod.invoke(initialPage);

            Field listModelField = InitialPage.class.getDeclaredField("categoryListModel");
            listModelField.setAccessible(true);
            DefaultListModel<?> categoryListModel = (DefaultListModel<?>) listModelField.get(initialPage);

            assertNotNull(categoryListModel, "O modelo da lista de categorias não deveria ser nulo.");
            assertTrue(categoryListModel.getSize() >= 0, "A consulta de categorias deve executar sem falhar.");

        } catch (Exception e) {
            fail("Erro ao testar o carregamento de categorias: " + e.getMessage());
        }
    }

    @Test
    public void testSimularInsercaoDeEquipamentoNovo() {
        try {
            Method loadCategoriesMethod = InitialPage.class.getDeclaredMethod("loadCategories");
            loadCategoriesMethod.setAccessible(true);
            loadCategoriesMethod.invoke(initialPage);

            Field nameField = InitialPage.class.getDeclaredField("equipmentNameField");
            nameField.setAccessible(true);
            JTextField equipmentNameField = (JTextField) nameField.get(initialPage);
            equipmentNameField.setText("Projetor de Teste Integração");

            Field qtyField = InitialPage.class.getDeclaredField("equipmentQuantityField");
            qtyField.setAccessible(true);
            JTextField equipmentQuantityField = (JTextField) qtyField.get(initialPage);
            equipmentQuantityField.setText("5"); // Quantidade válida

            Field locField = InitialPage.class.getDeclaredField("equipmentLocationField");
            locField.setAccessible(true);
            JTextField equipmentLocationField = (JTextField) locField.get(initialPage);
            equipmentLocationField.setText("Sala de Reuniões");

            Field descField = InitialPage.class.getDeclaredField("equipmentDescriptionArea");
            descField.setAccessible(true);
            JTextArea equipmentDescriptionArea = (JTextArea) descField.get(initialPage);
            equipmentDescriptionArea.setText("Projetor utilizado para testes automatizados.");

            Field catComboField = InitialPage.class.getDeclaredField("equipmentCategoryCombo");
            catComboField.setAccessible(true);
            JComboBox<?> equipmentCategoryCombo = (JComboBox<?>) catComboField.get(initialPage);
            if (equipmentCategoryCombo.getItemCount() > 0) {
                equipmentCategoryCombo.setSelectedIndex(0);
            } else {
                fail("Não há categorias no banco de dados para testar a inserção de equipamento.");
            }

            Method saveEquipmentMethod = InitialPage.class.getDeclaredMethod("saveEquipment", boolean.class);
            saveEquipmentMethod.setAccessible(true);
            saveEquipmentMethod.invoke(initialPage, false);

            Method loadEquipmentMethod = InitialPage.class.getDeclaredMethod("loadEquipment");
            loadEquipmentMethod.setAccessible(true);
            loadEquipmentMethod.invoke(initialPage);

            Field equipTableModelField = InitialPage.class.getDeclaredField("equipmentTableModel");
            equipTableModelField.setAccessible(true);
            DefaultTableModel equipmentTableModel = (DefaultTableModel) equipTableModelField.get(initialPage);

            boolean encontrou = false;
            for (int i = 0; i < equipmentTableModel.getRowCount(); i++) {
                String nomeEquipamento = (String) equipmentTableModel.getValueAt(i, 1); // Coluna 1 é o Nome
                if ("Projetor de Teste Integração".equals(nomeEquipamento)) {
                    encontrou = true;
                    break;
                }
            }

            assertTrue(encontrou, "O equipamento inserido deveria aparecer na tabela após recarregá-la.");

        } catch (Exception e) {
            fail("Erro ao simular a gravação de um equipamento: " + e.getMessage());
        }
    }

    @Test
    public void testClearEquipmentForm() {
        try {
            // 1. Escrevemos algo num campo de equipamento
            Field nameField = InitialPage.class.getDeclaredField("equipmentNameField");
            nameField.setAccessible(true);
            JTextField equipmentNameField = (JTextField) nameField.get(initialPage);
            equipmentNameField.setText("Monitor Velho");

            // 2. Invocamos o método que limpa o formulário
            Method clearFormMethod = InitialPage.class.getDeclaredMethod("clearEquipmentForm");
            clearFormMethod.setAccessible(true);
            clearFormMethod.invoke(initialPage);

            // 3. Verificamos se o texto desapareceu
            assertEquals("", equipmentNameField.getText(), "O campo de nome deveria estar vazio após limpar o formulário.");

        } catch (Exception e) {
            fail("Erro ao testar a limpeza do formulário: " + e.getMessage());
        }
    }

    @Test
    public void testPesquisarEquipamento() {
        try {
            Field searchField = InitialPage.class.getDeclaredField("equipmentSearchField");
            searchField.setAccessible(true);
            JTextField equipmentSearchField = (JTextField) searchField.get(initialPage);
            
            equipmentSearchField.setText("Cabo");

            Method loadEquipmentMethod = InitialPage.class.getDeclaredMethod("loadEquipment");
            loadEquipmentMethod.setAccessible(true);
            loadEquipmentMethod.invoke(initialPage);

            Field equipTableModelField = InitialPage.class.getDeclaredField("equipmentTableModel");
            equipTableModelField.setAccessible(true);
            DefaultTableModel equipmentTableModel = (DefaultTableModel) equipTableModelField.get(initialPage);

            assertNotNull(equipmentTableModel, "O modelo da tabela não deveria ser nulo após a pesquisa.");
            System.out.println("Resultados encontrados na pesquisa por 'Cabo': " + equipmentTableModel.getRowCount());

        } catch (Exception e) {
            fail("Erro ao testar a pesquisa de equipamentos: " + e.getMessage());
        }
    }

    @Test
    public void testInativarEquipamento() {
        try {
            Method loadEquipmentMethod = InitialPage.class.getDeclaredMethod("loadEquipment");
            loadEquipmentMethod.setAccessible(true);
            loadEquipmentMethod.invoke(initialPage);

            Field tableField = InitialPage.class.getDeclaredField("equipmentTable");
            tableField.setAccessible(true);
            JTable equipmentTable = (JTable) tableField.get(initialPage);

            if (equipmentTable.getRowCount() > 0) {
                equipmentTable.setRowSelectionInterval(0, 0);
                Method openSelectedMethod = InitialPage.class.getDeclaredMethod("openSelectedEquipment");
                openSelectedMethod.setAccessible(true);
                openSelectedMethod.invoke(initialPage);

                Method inactivateMethod = InitialPage.class.getDeclaredMethod("inactivateEquipment");
                inactivateMethod.setAccessible(true);
                inactivateMethod.invoke(initialPage);

                String statusAtualizado = (String) equipmentTable.getValueAt(0, 4);
                assertEquals("Inativo", statusAtualizado, "O status do equipamento deveria ser 'Inativo' após a inativação.");
            }

        } catch (Exception e) {
            fail("Erro ao testar a inativação do equipamento: " + e.getMessage());
        }
    }
}