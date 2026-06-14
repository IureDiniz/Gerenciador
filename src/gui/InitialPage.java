package gui;

import dao.CategoriaDAO;
import dao.EquipamentoDAO;
import dao.UsuarioDAO;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Categoria;
import model.Equipamento;
import model.Usuario;

public class InitialPage extends JFrame {

    private static final Color BACKGROUND = new Color(244, 247, 250);
    private static final Color PANEL = Color.WHITE;
    private static final Color PRIMARY = new Color(22, 111, 96);
    private static final Color PRIMARY_DARK = new Color(12, 82, 72);
    private static final Color TEXT = new Color(32, 41, 55);
    private static final Color MUTED = new Color(98, 110, 126);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EquipamentoDAO equipamentoDAO = new EquipamentoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private final DefaultTableModel userTableModel = createModel("ID", "Nome", "Tipo", "Status");
    private final JTable userTable = createTable(userTableModel);
    private final JTextField userSearchField = new JTextField(22);
    private final JTextField userNameField = new JTextField(24);
    private final JTextField userEmailField = new JTextField(24);
    private final JPasswordField userPasswordField = new JPasswordField(24);
    private final JComboBox<String> userTypeCombo = new JComboBox<>(new String[]{"Funcionario", "Supervisor", "Gerente"});
    private Usuario editingUser;
    private boolean userEditorMode;
    private JButton userInactivateButton;
    private JButton userDeleteButton;

    private final DefaultTableModel equipmentTableModel = createModel("ID", "Nome", "Categoria", "Quantidade", "Status");
    private final JTable equipmentTable = createTable(equipmentTableModel);
    private final JTextField equipmentSearchField = new JTextField(22);
    private final JTextField equipmentNameField = new JTextField(24);
    private final JTextField equipmentQuantityField = new JTextField(24);
    private final JTextField equipmentLocationField = new JTextField(24);
    private final JTextArea equipmentDescriptionArea = new JTextArea(4, 24);
    private final JComboBox<CategoryItem> equipmentCategoryCombo = new JComboBox<>();
    private final JComboBox<String> equipmentStatusCombo = new JComboBox<>(new String[]{"Disponivel", "Inativo"});
    private Equipamento editingEquipment;
    private boolean equipmentEditorMode;
    private JButton equipmentInactivateButton;
    private JButton equipmentDeleteButton;

    private final DefaultListModel<CategoryItem> categoryListModel = new DefaultListModel<>();
    private final JList<CategoryItem> categoryList = new JList<>(categoryListModel);
    private CategoryItem selectedCategoryFilter;

    private final DefaultTableModel selectedEquipmentModel = createModel("ID", "Equipamento", "Quantidade");
    private final DefaultTableModel movementEquipmentModel = createModel("ID", "Equipamento", "Quantidade");

    // Janela principal
    public InitialPage() {
        super("Gerenciador");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 620));
        setSize(1080, 680);
        setLocationRelativeTo(null);
        setContentPane(contentPanel);

        contentPanel.add(createHomePanel(), "home");
        contentPanel.add(createUserListPanel(), "users");
        contentPanel.add(createUserFormPanel(), "userForm");
        contentPanel.add(createHistoryPanel(), "history");
        contentPanel.add(createMovementListPanel(), "movements");
        contentPanel.add(createMovementFormPanel(false), "newMovement");
        contentPanel.add(createMovementFormPanel(true), "editMovement");
        contentPanel.add(createMovementEquipmentPanel(), "movementEquipment");
        contentPanel.add(createEquipmentListPanel(), "equipment");
        contentPanel.add(createEquipmentFormPanel(), "equipmentForm");
        contentPanel.add(createCategoryListPanel(), "categories");
        contentPanel.add(createCategorySelectPanel(), "categorySelect");
        contentPanel.add(createReportPanel(), "reports");

        showScreen("home");
    }

    // Tela inicial
    private JPanel createHomePanel() {
        JPanel panel = createScreen("Gerenciador de Estoque");
        JPanel menu = new JPanel(new GridLayout(2, 3, 16, 16));
        menu.setOpaque(false);
        menu.setBorder(new EmptyBorder(30, 120, 120, 120));

        menu.add(createHomeButton("Usuarios", "users"));
        menu.add(createHomeButton("Historico", "history"));
        menu.add(createHomeButton("Movimentacao", "movements"));
        menu.add(createHomeButton("Equipamentos", "equipment"));
        menu.add(createHomeButton("Relatorios", "reports"));

        panel.add(menu, BorderLayout.CENTER);
        return panel;
    }

    // Lista de usuarios
    private JPanel createUserListPanel() {
        JPanel panel = createScreen("Lista de Usuarios");
        panel.add(createSearchBar("Pesquisar usuario", userSearchField, evt -> loadUsers()), BorderLayout.NORTH);
        panel.add(wrapTable(userTable), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Novo", evt -> {
            clearUserForm();
            userEditorMode = false;
            userInactivateButton.setVisible(false);
            userDeleteButton.setVisible(false);
            showScreen("userForm");
        }));
        buttons.add(createButton("Editar", evt -> openSelectedUser()));
        buttons.add(createButton("Voltar", evt -> showScreen("home")));
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Formulario de usuario
    private JPanel createUserFormPanel() {
        JPanel panel = createScreen("Cadastrar ou Editar Usuario");
        JPanel form = createFormPanel();

        addFormRow(form, "Nome", userNameField);
        addFormRow(form, "Email", userEmailField);
        addFormRow(form, "Senha", userPasswordField);
        addFormRow(form, "Tipo", userTypeCombo);

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Salvar", evt -> saveUser(userEditorMode)));
        userInactivateButton = createButton("Inativar", evt -> inactivateUser());
        userDeleteButton = createButton("Excluir", evt -> deleteUser());
        userInactivateButton.setVisible(false);
        userDeleteButton.setVisible(false);
        buttons.add(userInactivateButton);
        buttons.add(userDeleteButton);
        buttons.add(createButton("Cancelar", evt -> showScreen("users")));

        panel.add(form, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Tela de historico
    private JPanel createHistoryPanel() {
        JPanel panel = createScreen("Historico");
        DefaultTableModel model = createModel("Usuario", "Hora", "Data", "Atividade");
        model.addRow(new Object[]{"Sistema", "08:00", LocalDate.now(), "Abertura do sistema"});
        model.addRow(new Object[]{"Administrador", "08:30", LocalDate.now(), "Consulta de dados"});
        panel.add(wrapTable(createTable(model)), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Voltar", evt -> showScreen("home")));
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Lista de movimentacoes
    private JPanel createMovementListPanel() {
        JPanel panel = createScreen("Movimentacao");
        JTextField search = new JTextField(22);
        panel.add(createSearchBar("Pesquisar movimentacao", search, evt -> {
        }), BorderLayout.NORTH);

        DefaultTableModel model = createModel("Tipo", "Data", "Descricao");
        model.addRow(new Object[]{"ENTRADA", LocalDate.now(), "Entrada de equipamentos"});
        model.addRow(new Object[]{"SAIDA", LocalDate.now(), "Saida de equipamentos"});
        JTable table = createTable(model);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showScreen("editMovement");
                }
            }
        });
        panel.add(wrapTable(table), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Novo", evt -> showScreen("newMovement")));
        buttons.add(createButton("Editar", evt -> showScreen("editMovement")));
        buttons.add(createButton("Voltar", evt -> showScreen("home")));
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Formulario de movimentacao
    private JPanel createMovementFormPanel(boolean editor) {
        JPanel panel = createScreen(editor ? "Editar Movimentacao" : "Nova Movimentacao");
        JPanel form = createFormPanel();
        JTextField tecnico = new JTextField(24);
        JTextField data = new JTextField(LocalDate.now().toString(), 24);
        JTextField localizacao = new JTextField(24);
        JComboBox<String> tipo = new JComboBox<>(new String[]{"ENTRADA", "SAIDA"});

        addFormRow(form, "Tecnico", tecnico);
        addFormRow(form, "Tipo", tipo);
        addFormRow(form, "Data", data);
        addFormRow(form, "Localizacao ou Descricao", localizacao);

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Selecionar Equipamentos", evt -> showScreen("movementEquipment")));
        buttons.add(createButton("Salvar", evt -> showScreen("movements")));
        buttons.add(createButton("Cancelar", evt -> showScreen("movements")));
        if (editor) {
            buttons.add(createButton("Excluir", evt -> showScreen("movements")));
        }

        panel.add(form, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Selecao de equipamentos da movimentacao
    private JPanel createMovementEquipmentPanel() {
        JPanel panel = createScreen("Selecionar Equipamentos");
        JTextField search = new JTextField(22);
        JTable sourceTable = createTable(movementEquipmentModel);
        JTable selectedTable = createTable(selectedEquipmentModel);

        JPanel center = new JPanel(new GridLayout(1, 2, 14, 0));
        center.setOpaque(false);
        center.add(createTitledTable("Equipamentos", sourceTable));
        center.add(createTitledTable("Selecionados", selectedTable));

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Adicionar", evt -> addSelectedEquipment(sourceTable)));
        buttons.add(createButton("Remover", evt -> removeSelectedEquipment(selectedTable)));
        buttons.add(createButton("Salvar", evt -> showScreen("newMovement")));
        buttons.add(createButton("Voltar", evt -> showScreen("newMovement")));

        panel.add(createSearchBar("Pesquisar equipamento", search, evt -> loadMovementEquipment(search.getText())), BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Lista de equipamentos
    private JPanel createEquipmentListPanel() {
        JPanel panel = createScreen("Lista de Equipamentos");
        panel.add(createSearchBar("Pesquisar equipamento", equipmentSearchField, evt -> loadEquipment()), BorderLayout.NORTH);
        panel.add(wrapTable(equipmentTable), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Novo", evt -> {
            clearEquipmentForm();
            showScreen("newEquipment");
        }));
        buttons.add(createButton("Editar", evt -> openSelectedEquipment()));
        buttons.add(createButton("Categorias", evt -> showScreen("categorySelect")));
        buttons.add(createButton("Voltar", evt -> showScreen("home")));
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Formulario de equipamento
    private JPanel createEquipmentFormPanel(boolean editor) {
        JPanel panel = createScreen(editor ? "Editar Equipamento" : "Novo Equipamento");
        JPanel form = createFormPanel();
        equipmentDescriptionArea.setLineWrap(true);
        equipmentDescriptionArea.setWrapStyleWord(true);

        addFormRow(form, "Nome", equipmentNameField);
        addFormRow(form, "Categoria", equipmentCategoryCombo);
        addFormRow(form, "Quantidade", equipmentQuantityField);
        addFormRow(form, "Localizacao", equipmentLocationField);
        addFormRow(form, "Status", equipmentStatusCombo);
        addFormRow(form, "Descricao", new JScrollPane(equipmentDescriptionArea));

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Salvar", evt -> saveEquipment(editor)));
        buttons.add(createButton("Cancelar", evt -> showScreen("equipment")));
        if (editor) {
            buttons.add(createButton("Inativar", evt -> inactivateEquipment()));
            buttons.add(createButton("Excluir", evt -> deleteEquipment()));
        }

        panel.add(form, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Lista de categorias
    private JPanel createCategoryListPanel() {
        JPanel panel = createScreen("Lista de Categorias");
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(categoryList), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Novo", evt -> createCategory()));
        buttons.add(createButton("Voltar", evt -> showScreen("equipment")));
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Selecao de categoria
    private JPanel createCategorySelectPanel() {
        JPanel panel = createScreen("Selecionar Categoria");
        JList<CategoryItem> selectList = new JList<>(categoryListModel);
        selectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(selectList), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Aplicar", evt -> {
            selectedCategoryFilter = selectList.getSelectedValue();
            showScreen("equipment");
        }));
        buttons.add(createButton("Limpar", evt -> {
            selectedCategoryFilter = null;
            showScreen("equipment");
        }));
        buttons.add(createButton("Categorias", evt -> showScreen("categories")));
        buttons.add(createButton("Voltar", evt -> showScreen("equipment")));
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Tela de relatorios
    private JPanel createReportPanel() {
        JPanel panel = createScreen("Relatorios");
        JPanel form = createFormPanel();
        JTextField start = new JTextField(24);
        JTextField end = new JTextField(24);
        JComboBox<String> type = new JComboBox<>(new String[]{"TODOS"});
        addFormRow(form, "Periodo inicial", start);
        addFormRow(form, "Periodo final", end);
        addFormRow(form, "Tipo", type);

        DefaultTableModel reportModel = createModel("Resultado");
        JTable reportTable = createTable(reportModel);
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(form, BorderLayout.NORTH);
        center.add(wrapTable(reportTable), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Atualizar", evt -> loadReports(reportModel)));
        buttons.add(createButton("Voltar", evt -> showScreen("home")));
        panel.add(center, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Carrega usuarios
    private void loadUsers() {
        userTableModel.setRowCount(0);
        String search = userSearchField.getText().trim().toLowerCase();
        try {
            for (Usuario usuario : usuarioDAO.listarTodos()) {
                if (search.isEmpty() || usuario.getLogin().toLowerCase().contains(search)) {
                    userTableModel.addRow(new Object[]{
                        usuario.getId(),
                        usuario.getLogin(),
                        usuario.getTipo(),
                        usuario.isAtivo() ? "Ativo" : "Inativo"
                    });
                }
            }
        } catch (Exception e) {
            showError("Nao foi possivel carregar usuarios.", e);
        }
    }

    // Abre usuario selecionado
    private void openSelectedUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um usuario.");
            return;
        }

        int id = (int) userTableModel.getValueAt(row, 0);
        try {
            for (Usuario usuario : usuarioDAO.listarTodos()) {
                if (usuario.getId() == id) {
                    editingUser = usuario;
                    userNameField.setText(usuario.getLogin());
                    userEmailField.setText(usuario.getEmail());
                    userPasswordField.setText(usuario.getSenha());
                    userTypeCombo.setSelectedItem(usuario.getTipo());
                    showScreen("editUser");
                    return;
                }
            }
        } catch (Exception e) {
            showError("Nao foi possivel abrir o usuario.", e);
        }
    }

    // Salva usuario
    private void saveUser(boolean editor) {
        try {
            int id = editor && editingUser != null ? editingUser.getId() : 0;
            boolean active = !editor || editingUser == null || editingUser.isAtivo();
            Usuario usuario = new Usuario(
                    id,
                    userNameField.getText().trim(),
                    new String(userPasswordField.getPassword()),
                    userEmailField.getText().trim(),
                    (String) userTypeCombo.getSelectedItem(),
                    active
            );

            if (editor) {
                usuarioDAO.atualizar(usuario);
            } else {
                usuarioDAO.inserir(usuario);
            }

            showScreen("users");
        } catch (Exception e) {
            showError("Nao foi possivel salvar o usuario.", e);
        }
    }

    // Inativa usuario
    private void inactivateUser() {
        if (editingUser == null) {
            return;
        }
        try {
            usuarioDAO.inativarPorId(editingUser.getId());
            showScreen("users");
        } catch (Exception e) {
            showError("Nao foi possivel inativar o usuario.", e);
        }
    }

    // Exclui usuario
    private void deleteUser() {
        if (editingUser == null) {
            return;
        }
        try {
            usuarioDAO.deletarPorId(editingUser.getId());
            showScreen("users");
        } catch (Exception e) {
            showError("Nao foi possivel excluir o usuario.", e);
        }
    }

    // Limpa formulario de usuario
    private void clearUserForm() {
        editingUser = null;
        userNameField.setText("");
        userEmailField.setText("");
        userPasswordField.setText("");
        userTypeCombo.setSelectedIndex(0);
    }

    // Carrega equipamentos
    private void loadEquipment() {
        equipmentTableModel.setRowCount(0);
        try {
            List<Equipamento> equipments = selectedCategoryFilter != null
                    ? equipamentoDAO.listarPorCategoria(selectedCategoryFilter.id)
                    : getEquipmentBySearch(equipmentSearchField.getText());

            if (equipments == null) {
                return;
            }

            for (Equipamento equipamento : equipments) {
                equipmentTableModel.addRow(new Object[]{
                    equipamento.getId(),
                    equipamento.getNome(),
                    getCategoryName(equipamento.getCategoria_id()),
                    equipamento.getQuantidade(),
                    equipamento.getStatus()
                });
            }
        } catch (Exception e) {
            showError("Nao foi possivel carregar equipamentos.", e);
        }
    }

    // Pesquisa equipamentos
    private List<Equipamento> getEquipmentBySearch(String search) throws SQLException {
        if (search == null || search.trim().isEmpty()) {
            return equipamentoDAO.listarTodos();
        }
        return equipamentoDAO.listarPorNomes(search.trim());
    }

    // Abre equipamento selecionado
    private void openSelectedEquipment() {
        int row = equipmentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um equipamento.");
            return;
        }

        int id = (int) equipmentTableModel.getValueAt(row, 0);
        try {
            editingEquipment = equipamentoDAO.pegarPorId(id);
            if (editingEquipment == null) {
                return;
            }
            equipmentNameField.setText(editingEquipment.getNome());
            equipmentQuantityField.setText(String.valueOf(editingEquipment.getQuantidade()));
            equipmentLocationField.setText(editingEquipment.getLocalizacao());
            equipmentDescriptionArea.setText(editingEquipment.getDescricao());
            equipmentStatusCombo.setSelectedItem(editingEquipment.getStatus());
            selectCategoryCombo(editingEquipment.getCategoria_id());
            showScreen("editEquipment");
        } catch (Exception e) {
            showError("Nao foi possivel abrir o equipamento.", e);
        }
    }

    // Salva equipamento
    private void saveEquipment(boolean editor) {
        try {
            CategoryItem category = (CategoryItem) equipmentCategoryCombo.getSelectedItem();
            if (category == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma categoria.");
                return;
            }

            int id = editor && editingEquipment != null ? editingEquipment.getId() : 0;
            Equipamento equipamento = new Equipamento(
                    id,
                    equipmentNameField.getText().trim(),
                    equipmentDescriptionArea.getText().trim(),
                    Integer.parseInt(equipmentQuantityField.getText().trim()),
                    category.id,
                    equipmentLocationField.getText().trim(),
                    (String) equipmentStatusCombo.getSelectedItem()
            );

            if (editor) {
                equipamentoDAO.atualizar(equipamento);
            } else {
                equipamentoDAO.inserir(equipamento);
            }

            showScreen("equipment");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade deve ser um numero.");
        } catch (Exception e) {
            showError("Nao foi possivel salvar o equipamento.", e);
        }
    }

    // Inativa equipamento
    private void inactivateEquipment() {
        if (editingEquipment == null) {
            return;
        }
        try {
            editingEquipment.setStatus("Inativo");
            equipamentoDAO.atualizar(editingEquipment);
            showScreen("equipment");
        } catch (Exception e) {
            showError("Nao foi possivel inativar o equipamento.", e);
        }
    }

    // Exclui equipamento
    private void deleteEquipment() {
        if (editingEquipment == null) {
            return;
        }
        try {
            equipamentoDAO.deletarPorId(editingEquipment.getId());
            showScreen("equipment");
        } catch (Exception e) {
            showError("Nao foi possivel excluir o equipamento.", e);
        }
    }

    // Limpa formulario de equipamento
    private void clearEquipmentForm() {
        editingEquipment = null;
        equipmentNameField.setText("");
        equipmentQuantityField.setText("");
        equipmentLocationField.setText("");
        equipmentDescriptionArea.setText("");
        equipmentStatusCombo.setSelectedIndex(0);
        if (equipmentCategoryCombo.getItemCount() > 0) {
            equipmentCategoryCombo.setSelectedIndex(0);
        }
    }

    // Carrega categorias
    private void loadCategories() {
        try {
            ArrayList<Categoria> categories = categoriaDAO.pegarTodos();
            DefaultComboBoxModel<CategoryItem> comboModel = new DefaultComboBoxModel<>();
            categoryListModel.clear();

            for (Categoria categoria : categories) {
                CategoryItem item = new CategoryItem(categoria.getId(), categoria.getNome());
                comboModel.addElement(item);
                categoryListModel.addElement(item);
            }

            equipmentCategoryCombo.setModel(comboModel);
        } catch (Exception e) {
            showError("Nao foi possivel carregar categorias.", e);
        }
    }

    // Cria categoria simples
    private void createCategory() {
        String name = JOptionPane.showInputDialog(this, "Nome da categoria:");
        if (name == null || name.trim().isEmpty()) {
            return;
        }
        try {
            categoriaDAO.inserir(new Categoria(0, name.trim()));
            loadCategories();
        } catch (Exception e) {
            showError("Nao foi possivel salvar a categoria.", e);
        }
    }

    // Seleciona categoria no combo
    private void selectCategoryCombo(int id) {
        for (int i = 0; i < equipmentCategoryCombo.getItemCount(); i++) {
            CategoryItem item = equipmentCategoryCombo.getItemAt(i);
            if (item.id == id) {
                equipmentCategoryCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    // Nome da categoria
    private String getCategoryName(int id) {
        for (int i = 0; i < categoryListModel.size(); i++) {
            CategoryItem item = categoryListModel.get(i);
            if (item.id == id) {
                return item.name;
            }
        }
        return String.valueOf(id);
    }

    // Carrega equipamentos para movimentacao
    private void loadMovementEquipment(String search) {
        movementEquipmentModel.setRowCount(0);
        try {
            List<Equipamento> equipments = getEquipmentBySearch(search);
            if (equipments == null) {
                return;
            }
            for (Equipamento equipamento : equipments) {
                movementEquipmentModel.addRow(new Object[]{
                    equipamento.getId(),
                    equipamento.getNome(),
                    equipamento.getQuantidade()
                });
            }
        } catch (Exception e) {
            showError("Nao foi possivel carregar equipamentos.", e);
        }
    }

    // Adiciona equipamento na movimentacao
    private void addSelectedEquipment(JTable sourceTable) {
        int row = sourceTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um equipamento.");
            return;
        }
        selectedEquipmentModel.addRow(new Object[]{
            movementEquipmentModel.getValueAt(row, 0),
            movementEquipmentModel.getValueAt(row, 1),
            1
        });
    }

    // Remove equipamento da movimentacao
    private void removeSelectedEquipment(JTable selectedTable) {
        int row = selectedTable.getSelectedRow();
        if (row >= 0) {
            selectedEquipmentModel.removeRow(row);
        }
    }

    // Carrega relatorios
    private void loadReports(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            int users = usuarioDAO.listarTodos().size();
            List<Equipamento> equipments = equipamentoDAO.listarTodos();
            int equipmentCount = equipments == null ? 0 : equipments.size();
            model.addRow(new Object[]{"Usuarios cadastrados: " + users});
            model.addRow(new Object[]{"Equipamentos cadastrados: " + equipmentCount});
            model.addRow(new Object[]{"Categoria selecionada: " + (selectedCategoryFilter == null ? "TODOS" : selectedCategoryFilter.name)});
        } catch (Exception e) {
            showError("Nao foi possivel carregar relatorios.", e);
        }
    }

    // Troca de tela
    private void showScreen(String screen) {
        if ("users".equals(screen)) {
            loadUsers();
        } else if ("equipment".equals(screen)) {
            loadCategories();
            loadEquipment();
        } else if ("categories".equals(screen) || "categorySelect".equals(screen)) {
            loadCategories();
        } else if ("movementEquipment".equals(screen)) {
            loadMovementEquipment("");
        }
        cardLayout.show(contentPanel, screen);
    }

    // Painel base
    private JPanel createScreen(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(22, 26, 22, 26));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT);
        header.add(titleLabel, BorderLayout.WEST);
        header.add(createTopNavigation(), BorderLayout.EAST);
        panel.add(header, BorderLayout.PAGE_START);
        return panel;
    }

    // Navegacao superior
    private JPanel createTopNavigation() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        nav.setOpaque(false);
        nav.add(createSmallButton("Inicio", evt -> showScreen("home")));
        nav.add(createSmallButton("Usuarios", evt -> showScreen("users")));
        nav.add(createSmallButton("Movimentacao", evt -> showScreen("movements")));
        nav.add(createSmallButton("Equipamentos", evt -> showScreen("equipment")));
        nav.add(createSmallButton("Relatorios", evt -> showScreen("reports")));
        return nav;
    }

    // Botao da tela inicial
    private JButton createHomeButton(String text, String screen) {
        JButton button = createButton(text, evt -> showScreen(screen));
        button.setPreferredSize(new Dimension(180, 90));
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        return button;
    }

    // Barra de pesquisa
    private JPanel createSearchBar(String placeholder, JTextField field, java.awt.event.ActionListener action) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        field.setFont(LABEL_FONT);
        field.setToolTipText(placeholder);
        JButton searchButton = createButton("Pesquisar", action);
        panel.add(field, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.EAST);
        return panel;
    }

    // Painel de formulario
    private JPanel createFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 235)),
                new EmptyBorder(24, 120, 24, 120)
        ));
        return form;
    }

    // Linha do formulario
    private void addFormRow(JPanel form, String labelText, Component field) {
        int row = form.getComponentCount() / 2;

        JLabel label = new JLabel(labelText + ":");
        label.setFont(LABEL_FONT);
        label.setForeground(MUTED);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.LINE_END;
        labelConstraints.insets = new Insets(8, 0, 8, 12);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(8, 0, 8, 0);

        form.add(label, labelConstraints);
        form.add(field, fieldConstraints);
    }

    // Painel de botoes
    private JPanel createButtonPanel() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(4, 0, 0, 0));
        return buttons;
    }

    // Botao principal
    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(new EmptyBorder(8, 14, 8, 14));
        button.addActionListener(action);
        return button;
    }

    // Botao pequeno
    private JButton createSmallButton(String text, java.awt.event.ActionListener action) {
        JButton button = createButton(text, action);
        button.setBackground(PRIMARY_DARK);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return button;
    }

    // Modelo de tabela
    private DefaultTableModel createModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // Tabela padrao
    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(232, 238, 244));
        table.getTableHeader().setForeground(TEXT);
        return table;
    }

    // Tabela com borda
    private JScrollPane wrapTable(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 229, 235)));
        return scrollPane;
    }

    // Tabela com titulo
    private JPanel createTitledTable(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(TEXT);
        panel.add(label, BorderLayout.NORTH);
        panel.add(wrapTable(table), BorderLayout.CENTER);
        return panel;
    }

    // Mensagem de erro
    private void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(this, message + "\n" + e.getMessage());
    }

    // Item de categoria
    private static class CategoryItem {
        private final int id;
        private final String name;

        private CategoryItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
