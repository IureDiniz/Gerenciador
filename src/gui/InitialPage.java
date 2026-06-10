package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

public class InitialPage extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public InitialPage() {
        super("Gerenciador");
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createHomePanel(), "home");
        contentPanel.add(createUserListPanel(), "users");
        contentPanel.add(createUserFormPanel("Novo Usuario", false), "newUser");
        contentPanel.add(createUserFormPanel("Editor de Usuario", true), "editUser");
        contentPanel.add(createHistoryPanel(), "history");
        contentPanel.add(createMovementListPanel(), "movements");
        contentPanel.add(createMovementFormPanel("Nova Movimentacao", false), "newMovement");
        contentPanel.add(createMovementFormPanel("Editor de Movimentacao", true), "editMovement");
        contentPanel.add(createSelectedEquipmentPanel(), "selectedEquipment");
        contentPanel.add(createEquipmentListPanel(), "equipment");
        contentPanel.add(createEquipmentFormPanel("Novo Equipamento", false), "newEquipment");
        contentPanel.add(createEquipmentFormPanel("Editor de Equipamento", true), "editEquipment");
        contentPanel.add(createCategoryListPanel(), "categories");
        contentPanel.add(createReportPanel(), "reports");

        setContentPane(contentPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 520));
        setSize(820, 560);
        setLocationRelativeTo(null);
        showScreen("home");
    }

    private JPanel createHomePanel() {
        JPanel panel = createBasePanel("Gerenciador de Estoque");

        JPanel menu = new JPanel(new GridLayout(0, 1, 0, 12));
        menu.setBorder(BorderFactory.createEmptyBorder(24, 220, 24, 220));
        menu.add(createNavigationButton("Usuarios", "users"));
        menu.add(createNavigationButton("Historico", "history"));
        menu.add(createNavigationButton("Movimentacao", "movements"));
        menu.add(createNavigationButton("Equipamentos", "equipment"));
        menu.add(createNavigationButton("Relatorios", "reports"));

        panel.add(menu, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createUserListPanel() {
        JPanel panel = createBasePanel("Lista de Usuarios");
        String[] columns = {"Nome", "Status"};
        Object[][] rows = {
            {"Administrador", "Ativo"},
            {"Funcionario", "Ativo"},
            {"Supervisor", "Inativo"}
        };

        JTable table = createTable(columns, rows);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showScreen("editUser");
                }
            }
        });
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createNavigationButton("Novo", "newUser"));
        buttons.add(createNavigationButton("Voltar", "home"));
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createUserFormPanel(String title, boolean editor) {
        JPanel panel = createBasePanel(title);
        JPanel form = createFormPanel();

        addTextField(form, "Nome", editor ? "Administrador" : "");
        addPasswordField(form, "Senha", editor ? "123456" : "");
        addComboBox(form, "Tipo", new String[]{"FUNCIONARIO", "SUPERVISOR", "GERENTE"});

        panel.add(form, BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createNavigationButton("Salvar", "users"));
        if (editor) {
            buttons.add(createNavigationButton("Inativar", "users"));
            buttons.add(createNavigationButton("Excluir", "users"));
        }
        buttons.add(createNavigationButton("Cancelar", "users"));
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = createBasePanel("Historico");
        String[] columns = {"Usuario", "Hora", "Data", "Atividade"};
        Object[][] rows = {
            {"Administrador", "08:30", "09/06/2026", "Login no sistema"},
            {"Supervisor", "09:15", "09/06/2026", "Consulta de equipamentos"},
            {"Funcionario", "10:00", "09/06/2026", "Registro de movimentacao"}
        };
        panel.add(new JScrollPane(createTable(columns, rows)), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createNavigationButton("Voltar", "home"));
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMovementListPanel() {
        JPanel panel = createBasePanel("Lista de Movimentacoes");
        String[] columns = {"Tipo", "Data"};
        Object[][] rows = {
            {"ENTRADA", "09/06/2026"},
            {"SAIDA", "09/06/2026"}
        };

        JTable table = createTable(columns, rows);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showScreen("editMovement");
                }
            }
        });
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createNavigationButton("Novo", "newMovement"));
        buttons.add(createNavigationButton("Voltar", "home"));
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMovementFormPanel(String title, boolean editor) {
        JPanel panel = createBasePanel(title);
        JPanel form = createFormPanel();

        addTextField(form, "Tecnico", editor ? "Funcionario" : "");
        addTextField(form, "Equipamento", editor ? "Notebook" : "");
        addComboBox(form, "Tipo", new String[]{"ENTRADA", "SAIDA"});
        addTextField(form, "Data", editor ? "09/06/2026" : "");
        addTextField(form, "Localizacao ou Descricao", editor ? "Almoxarifado" : "");

        panel.add(form, BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createNavigationButton("Salvar", "movements"));
        if (editor) {
            buttons.add(createNavigationButton("Cancelar", "movements"));
            buttons.add(createNavigationButton("Excluir", "movements"));
        } else {
            buttons.add(createNavigationButton("Cancelar", "movements"));
        }
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSelectedEquipmentPanel() {
        JPanel panel = createBasePanel("Equipamentos Selecionados");
        String[] columns = {"Equipamento", "Quantidade"};
        Object[][] rows = {
            {"Notebook", 2},
            {"Monitor", 4}
        };
        panel.add(new JScrollPane(createTable(columns, rows)), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createNavigationButton("Novo", "equipment"));
        buttons.add(createNavigationButton("Salvar", "movements"));
        buttons.add(createNavigationButton("Voltar", "newMovement"));
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEquipmentListPanel() {
        JPanel panel = createBasePanel("Lista de Equipamentos");
        String[] columns = {"ID ou Nome", "Quantidade"};
        Object[][] rows = {
            {"Notebook", 8},
            {"Monitor", 12},
            {"Teclado", 20}
        };

        JTable table = createTable(columns, rows);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showScreen("editEquipment");
                }
            }
        });
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createNavigationButton("Novo", "newEquipment"));
        buttons.add(createNavigationButton("Voltar", "home"));
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEquipmentFormPanel(String title, boolean editor) {
        JPanel panel = createBasePanel(title);
        JPanel form = createFormPanel();

        addTextField(form, "Nome", editor ? "Notebook" : "");
        addTextField(form, "Categoria", editor ? "Informatica" : "");
        addTextField(form, "Quantidade", editor ? "8" : "");
        addTextArea(form, "Descricao", editor ? "Equipamento para uso interno" : "");

        panel.add(form, BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createNavigationButton("Salvar", "equipment"));
        buttons.add(createNavigationButton("Cancelar", "equipment"));
        if (editor) {
            buttons.add(createNavigationButton("Inativar", "equipment"));
            buttons.add(createNavigationButton("Excluir", "equipment"));
        }
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCategoryListPanel() {
        JPanel panel = createBasePanel("Lista de Categorias");
        JList<String> list = new JList<>(new String[]{"Informatica", "Ferramentas", "Escritorio"});
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(new JButton("Novo"));
        buttons.add(createNavigationButton("Voltar", "equipment"));
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportPanel() {
        JPanel panel = createBasePanel("Relatorios");

        JPanel form = createFormPanel();
        addTextField(form, "Periodo inicial", "");
        addTextField(form, "Periodo final", "");
        addComboBox(form, "Tipo", new String[]{"TODOS"});

        String[] columns = {"Resultado"};
        Object[][] rows = {
            {"Movimentacoes do periodo"},
            {"Equipamentos cadastrados"},
            {"Usuarios ativos"}
        };

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.add(form, BorderLayout.NORTH);
        center.add(new JScrollPane(createTable(columns, rows)), BorderLayout.CENTER);
        panel.add(center, BorderLayout.CENTER);

        JPanel buttons = createButtonPanel();
        buttons.add(createNavigationButton("Voltar", "home"));
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBasePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(8, 120, 8, 120));
        return form;
    }

    private JPanel createButtonPanel() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        return buttons;
    }

    private JButton createNavigationButton(String text, String screen) {
        JButton button = new JButton(text);
        button.addActionListener(evt -> showScreen(screen));
        return button;
    }

    private JTable createTable(String[] columns, Object[][] rows) {
        JTable table = new JTable(new DefaultTableModel(rows, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        return table;
    }

    private void addTextField(JPanel form, String label, String value) {
        JTextField field = new JTextField(value, 24);
        addFormRow(form, label, field);
    }

    private void addPasswordField(JPanel form, String label, String value) {
        JPasswordField field = new JPasswordField(value, 24);
        addFormRow(form, label, field);
    }

    private void addComboBox(JPanel form, String label, String[] values) {
        JComboBox<String> comboBox = new JComboBox<>(values);
        addFormRow(form, label, comboBox);
    }

    private void addTextArea(JPanel form, String label, String value) {
        JTextArea area = new JTextArea(value, 4, 24);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        addFormRow(form, label, new JScrollPane(area));
    }

    private void addFormRow(JPanel form, String labelText, java.awt.Component field) {
        int row = form.getComponentCount() / 2;

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.LINE_END;
        labelConstraints.insets = new Insets(6, 0, 6, 12);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(6, 0, 6, 0);

        form.add(new JLabel(labelText + ":"), labelConstraints);
        form.add(field, fieldConstraints);
    }

    private void showScreen(String screen) {
        cardLayout.show(contentPanel, screen);
    }
}
