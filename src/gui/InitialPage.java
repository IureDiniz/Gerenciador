package gui;

import dao.CategoriaDAO;
import dao.EquipamentoDAO;
import dao.UsuarioDAO;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.Icon;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Categoria;
import model.Equipamento;
import model.Usuario;

public class InitialPage extends JFrame {

    private static final Color BACKGROUND = new Color(245, 245, 245);
    private static final Color PANEL = Color.WHITE;
    private static final Color TITLE_BLUE = new Color(12, 94, 128);
    private static final Color TABLE_BLUE = new Color(16, 104, 137);
    private static final Color LIGHT_BLUE = new Color(183, 235, 255);
    private static final Color ACTION_BLUE = new Color(31, 157, 213);
    private static final Color SAVE_GREEN = new Color(0, 176, 80);
    private static final Color DELETE_RED = new Color(232, 20, 56);
    private static final Color DISABLED_GRAY = new Color(140, 140, 140);
    private static final Color TEXT = new Color(16, 65, 83);
    private static final Color MUTED = new Color(42, 83, 96);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setBorder(new EmptyBorder(42, 70, 70, 70));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        top.setOpaque(false);
        JPanel quickButtons = new JPanel(new GridLayout(2, 1, 0, 16));
        quickButtons.setOpaque(false);
        quickButtons.add(createHomeTopButton("Usuarios", "users"));
        quickButtons.add(createHomeTopButton("Historico", "history"));
        top.add(quickButtons);
        panel.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        JLabel title = new JLabel("Sistema de Estoque", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 66));
        title.setForeground(TITLE_BLUE);

        JPanel tiles = new JPanel(new GridLayout(1, 3, 110, 0));
        tiles.setOpaque(false);
        tiles.add(createHomeTile("Movimentacao", "movements", "movement"));
        tiles.add(createHomeTile("Equipamentos", "equipment", "equipment"));
        tiles.add(createHomeTile("Relatorios", "reports", "report"));

        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        titleConstraints.insets = new Insets(45, 0, 95, 0);
        center.add(title, titleConstraints);

        GridBagConstraints tileConstraints = new GridBagConstraints();
        tileConstraints.gridx = 0;
        tileConstraints.gridy = 1;
        center.add(tiles, tileConstraints);

        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    // Lista de usuarios
    private JPanel createUserListPanel() {
        JPanel panel = createScreen("Lista de Usuarios");
        panel.add(createTopContent(panel, createSearchBar("Pesquisar usuario", userSearchField, evt -> loadUsers())), BorderLayout.NORTH);
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
        panel.add(createTopContent(panel, createSearchBar("Pesquisar movimentacao", search, evt -> {
        })), BorderLayout.NORTH);

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

        panel.add(createTopContent(panel, createSearchBar("Pesquisar equipamento", search, evt -> loadMovementEquipment(search.getText()))), BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Lista de equipamentos
    private JPanel createEquipmentListPanel() {
        JPanel panel = createScreen("Lista de Equipamentos");
        panel.add(createTopContent(panel, createSearchBar("Pesquisar equipamento", equipmentSearchField, evt -> loadEquipment())), BorderLayout.NORTH);
        panel.add(wrapTable(equipmentTable), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createButton("Novo", evt -> {
            clearEquipmentForm();
            equipmentEditorMode = false;
            equipmentInactivateButton.setVisible(false);
            equipmentDeleteButton.setVisible(false);
            showScreen("equipmentForm");
        }));
        buttons.add(createButton("Editar", evt -> openSelectedEquipment()));
        buttons.add(createButton("Categorias", evt -> showScreen("categorySelect")));
        buttons.add(createButton("Voltar", evt -> showScreen("home")));
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Formulario de equipamento
    private JPanel createEquipmentFormPanel() {
        JPanel panel = createScreen("Novo ou Editar Equipamento");
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
        buttons.add(createButton("Salvar", evt -> saveEquipment(equipmentEditorMode)));
        buttons.add(createButton("Cancelar", evt -> showScreen("equipment")));
        equipmentInactivateButton = createButton("Inativar", evt -> inactivateEquipment());
        equipmentDeleteButton = createButton("Excluir", evt -> deleteEquipment());
        equipmentInactivateButton.setVisible(false);
        equipmentDeleteButton.setVisible(false);
        buttons.add(equipmentInactivateButton);
        buttons.add(equipmentDeleteButton);

        panel.add(form, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    // Lista de categorias
    private JPanel createCategoryListPanel() {
        JPanel panel = createScreen("Lista de Categorias");
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleList(categoryList);
        panel.add(wrapList(categoryList), BorderLayout.CENTER);

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
        styleList(selectList);
        panel.add(wrapList(selectList), BorderLayout.CENTER);

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
                    userEditorMode = true;
                    userInactivateButton.setVisible(true);
                    userDeleteButton.setVisible(true);
                    showScreen("userForm");
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
            equipmentEditorMode = true;
            equipmentInactivateButton.setVisible(true);
            equipmentDeleteButton.setVisible(true);
            showScreen("equipmentForm");
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
        titleLabel.setForeground(TITLE_BLUE);
        header.add(titleLabel, BorderLayout.WEST);
        panel.putClientProperty("header", header);
        panel.add(header, BorderLayout.PAGE_START);
        return panel;
    }

    // Cabecalho com conteudo abaixo
    private JPanel createTopContent(JPanel panel, Component component) {
        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        Object header = panel.getClientProperty("header");
        if (header instanceof Component) {
            top.add((Component) header, BorderLayout.NORTH);
        }
        top.add(component, BorderLayout.SOUTH);
        return top;
    }

    // Navegacao superior
    private JPanel createTopNavigation() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        nav.setOpaque(false);
        nav.add(createSmallButton("Historico", evt -> showScreen("history")));
        nav.add(createSmallButton("Relatorios", evt -> showScreen("reports")));
        return nav;
    }

    // Botao da tela inicial
    private JButton createHomeButton(String text, String screen) {
        JButton button = createButton(text, evt -> showScreen(screen));
        button.setPreferredSize(new Dimension(140, 95));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return button;
    }

    // Botao superior da tela inicial
    private JButton createHomeTopButton(String text, String screen) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(260, 64));
        button.setBackground(LIGHT_BLUE);
        button.setForeground(TITLE_BLUE);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 30));
        button.addActionListener(evt -> showScreen(screen));
        return button;
    }

    // Card da tela inicial
    private JPanel createHomeTile(String text, String screen, String iconType) {
        JPanel tile = new JPanel(new BorderLayout(0, 10));
        tile.setOpaque(false);
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton iconButton = new JButton(new HomeIcon(iconType));
        iconButton.setPreferredSize(new Dimension(260, 220));
        iconButton.setBackground(ACTION_BLUE);
        iconButton.setOpaque(true);
        iconButton.setFocusPainted(false);
        iconButton.setBorderPainted(false);
        iconButton.addActionListener(evt -> showScreen(screen));

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 32));
        label.setForeground(TITLE_BLUE);

        MouseAdapter clickAction = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                showScreen(screen);
            }
        };
        tile.addMouseListener(clickAction);
        label.addMouseListener(clickAction);

        tile.add(iconButton, BorderLayout.CENTER);
        tile.add(label, BorderLayout.SOUTH);
        return tile;
    }

    // Barra de pesquisa
    private JPanel createSearchBar(String placeholder, JTextField field, java.awt.event.ActionListener action) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        field.setFont(LABEL_FONT);
        field.setToolTipText(placeholder);
        styleInput(field);
        JButton searchButton = createButton("Pesquisar", action);
        panel.add(field, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.EAST);
        return panel;
    }

    // Painel de formulario
    private JPanel createFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL);
        form.setBorder(new EmptyBorder(34, 150, 34, 150));
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
        styleInput(field);
        form.add(field, fieldConstraints);
    }

    // Painel de botoes
    private JPanel createButtonPanel() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(6, 0, 0, 0));
        return buttons;
    }

    // Botao principal
    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(getButtonColor(text));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(new EmptyBorder(6, 14, 6, 14));
        button.addActionListener(action);
        return button;
    }

    // Botao pequeno
    private JButton createSmallButton(String text, java.awt.event.ActionListener action) {
        JButton button = createButton(text, action);
        button.setBackground(LIGHT_BLUE);
        button.setForeground(TITLE_BLUE);
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
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setBackground(LIGHT_BLUE);
        table.setForeground(new Color(15, 54, 67));
        table.setGridColor(TABLE_BLUE);
        table.setSelectionBackground(new Color(135, 218, 250));
        table.setSelectionForeground(TEXT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(TABLE_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setOpaque(true);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    component.setBackground(LIGHT_BLUE);
                    component.setForeground(new Color(15, 54, 67));
                }
                setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, TABLE_BLUE));
                return component;
            }
        });
        return table;
    }

    // Tabela com borda
    private JScrollPane wrapTable(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(TABLE_BLUE);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TABLE_BLUE, 10),
                BorderFactory.createLineBorder(TABLE_BLUE, 1)
        ));
        return scrollPane;
    }

    // Lista com estilo do Figma
    private void styleList(JList<?> list) {
        list.setFont(new Font("Segoe UI", Font.BOLD, 12));
        list.setBackground(LIGHT_BLUE);
        list.setForeground(TEXT);
        list.setSelectionBackground(new Color(135, 218, 250));
        list.setSelectionForeground(TEXT);
        list.setFixedCellHeight(28);
        list.setBorder(BorderFactory.createLineBorder(TABLE_BLUE, 2));
    }

    // Lista dentro do painel azul
    private JScrollPane wrapList(JList<?> list) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.getViewport().setBackground(TABLE_BLUE);
        scrollPane.setBorder(BorderFactory.createLineBorder(TABLE_BLUE, 10));
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

    // Campos azul claro
    private void styleInput(Component field) {
        if (field instanceof JTextField) {
            JTextField textField = (JTextField) field;
            textField.setBackground(LIGHT_BLUE);
            textField.setForeground(TEXT);
            textField.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        } else if (field instanceof JTextArea) {
            JTextArea textArea = (JTextArea) field;
            textArea.setBackground(LIGHT_BLUE);
            textArea.setForeground(TEXT);
            textArea.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        } else if (field instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) field;
            comboBox.setBackground(LIGHT_BLUE);
            comboBox.setForeground(TEXT);
        } else if (field instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) field;
            scrollPane.setBorder(BorderFactory.createLineBorder(LIGHT_BLUE));
            Component view = scrollPane.getViewport().getView();
            styleInput(view);
        }
    }

    // Cor do botao por acao
    private Color getButtonColor(String text) {
        if ("Salvar".equals(text)) {
            return SAVE_GREEN;
        }
        if ("Excluir".equals(text)) {
            return DELETE_RED;
        }
        if ("Inativar".equals(text)) {
            return DISABLED_GRAY;
        }
        return ACTION_BLUE;
    }

    // Mensagem de erro
    private void showError(String message, Exception e) {
        JOptionPane.showMessageDialog(this, message + "\n" + e.getMessage());
    }

    // Icones da tela inicial
    private static class HomeIcon implements Icon {
        private final String type;

        private HomeIcon(String type) {
            this.type = type;
        }

        @Override
        public int getIconWidth() {
            return 100;
        }

        @Override
        public int getIconHeight() {
            return 100;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.translate(x, y);

            if ("movement".equals(type)) {
                paintMovement(g);
            } else if ("equipment".equals(type)) {
                paintEquipment(g);
            } else {
                paintReport(g);
            }

            g.dispose();
        }

        private void paintMovement(Graphics2D g) {
            g.drawArc(18, 18, 64, 64, 30, 85);
            g.drawLine(82, 30, 90, 42);
            g.drawLine(82, 30, 68, 32);

            g.drawArc(18, 18, 64, 64, 150, 85);
            g.drawLine(18, 26, 34, 22);
            g.drawLine(18, 26, 22, 42);

            g.drawArc(18, 18, 64, 64, 270, 85);
            g.drawLine(38, 88, 30, 72);
            g.drawLine(38, 88, 54, 84);
        }

        private void paintEquipment(Graphics2D g) {
            g.drawRect(38, 36, 24, 24);
            g.drawOval(30, 28, 40, 40);
            g.drawLine(50, 12, 50, 28);
            g.drawLine(50, 68, 50, 86);
            g.drawLine(14, 50, 30, 50);
            g.drawLine(70, 50, 86, 50);

            g.drawLine(18, 78, 38, 58);
            g.drawLine(12, 72, 24, 84);
            g.drawLine(62, 58, 82, 78);
            g.drawLine(76, 84, 88, 72);

            g.drawRect(20, 12, 40, 14);
            g.drawLine(60, 19, 86, 19);
            g.drawLine(86, 15, 94, 19);
            g.drawLine(86, 23, 94, 19);
        }

        private void paintReport(Graphics2D g) {
            g.drawRect(24, 12, 48, 64);
            g.drawLine(24, 12, 38, 26);
            g.drawLine(38, 26, 38, 12);
            g.drawLine(24, 12, 38, 12);
            g.drawLine(34, 34, 62, 34);
            g.drawLine(34, 44, 62, 44);
            g.drawLine(34, 54, 62, 54);
            g.drawRect(48, 62, 14, 10);
            g.drawRect(36, 24, 48, 64);
        }
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
