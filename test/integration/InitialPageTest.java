package integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
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
    public void testInsercaoDeUsuarioNovo() {
        try {
            Field nameField = InitialPage.class.getDeclaredField("userNameField");
            nameField.setAccessible(true);
            JTextField userNameField = (JTextField) nameField.get(initialPage);
            userNameField.setText("Usuario Teste Integracao");

            Field emailField = InitialPage.class.getDeclaredField("userEmailField");
            emailField.setAccessible(true);
            JTextField userEmailField = (JTextField) emailField.get(initialPage);
            userEmailField.setText("teste@integracao.com");

            Field passField = InitialPage.class.getDeclaredField("userPasswordField");
            passField.setAccessible(true);
            JPasswordField userPasswordField = (JPasswordField) passField.get(initialPage);
            userPasswordField.setText("senha123");

            Field typeComboField = InitialPage.class.getDeclaredField("userTypeCombo");
            typeComboField.setAccessible(true);
            JComboBox<String> userTypeCombo = (JComboBox<String>) typeComboField.get(initialPage);
            userTypeCombo.setSelectedItem("Funcionario");

            Method saveUserMethod = InitialPage.class.getDeclaredMethod("saveUser", boolean.class);
            saveUserMethod.setAccessible(true);
            
            saveUserMethod.invoke(initialPage, false);

            Method loadUsersMethod = InitialPage.class.getDeclaredMethod("loadUsers");
            loadUsersMethod.setAccessible(true);
            loadUsersMethod.invoke(initialPage);

            Field userTableModelField = InitialPage.class.getDeclaredField("userTableModel");
            userTableModelField.setAccessible(true);
            DefaultTableModel userTableModel = (DefaultTableModel) userTableModelField.get(initialPage);


            boolean encontrou = false;
            for (int i = 0; i < userTableModel.getRowCount(); i++) {
                String nomeTabela = (String) userTableModel.getValueAt(i, 1);
                if ("Usuario Teste Integracao".equals(nomeTabela)) {
                    encontrou = true;
                    break;
                }
            }

            assertTrue(encontrou, "O utilizador inserido via formulário deveria aparecer na tabela após recarregá-la.");

        } catch (Exception e) {
            fail("Erro ao simular a gravação de um utilizador: " + e.getMessage());
        }
    }
}