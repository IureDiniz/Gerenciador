package connection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Database {
    private static Database INSTANCE = null;

    private Connection connection = null;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");

            Path databasePath = prepareDatabasePath();
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);

            createDatabase();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu o seguinte erro ao tentar criar uma conexao: \n" + e);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver do banco de dados nao encontrado: \n" + e);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Nao foi possivel preparar o arquivo do banco de dados: \n" + e);
        }
    }

    private Path prepareDatabasePath() throws IOException {
        Path appDataDir = getAppDataDirectory();
        Files.createDirectories(appDataDir);

        Path targetDatabase = appDataDir.resolve("Estoque.db");
        if (!Files.exists(targetDatabase)) {
            Path sourceDatabase = findBundledDatabase();
            if (sourceDatabase != null) {
                Files.copy(sourceDatabase, targetDatabase);
            }
        }

        return targetDatabase.toAbsolutePath();
    }

    private Path getAppDataDirectory() {
        String appData = System.getenv("APPDATA");
        if (appData != null && !appData.isBlank()) {
            return Paths.get(appData, "GerenciadorEstoque");
        }

        return Paths.get(System.getProperty("user.home"), ".gerenciador-estoque");
    }

    private Path findBundledDatabase() {
        Path[] candidates = {
            Paths.get("tools", "Estoque.db"),
            Paths.get("app", "tools", "Estoque.db"),
            Paths.get(System.getProperty("user.dir"), "tools", "Estoque.db"),
            Paths.get(System.getProperty("user.dir"), "app", "tools", "Estoque.db")
        };

        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private void createDatabase() throws SQLException {

        String createTable = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL CHECK(length(nome) <= 100),
                    email TEXT NOT NULL UNIQUE CHECK(length(email) <= 100),
                    senha TEXT NOT NULL CHECK(length(senha) <= 255),
                    tipo TEXT NOT NULL CHECK(tipo IN ('Funcionario', 'Supervisor', 'Gerente')),
                    ativo INTEGER NOT NULL DEFAULT 1
                );
                """;

        String insertAdmin = """
                INSERT INTO usuarios (
                    id,
                    nome,
                    email,
                    senha,
                    tipo,
                    ativo
                )
                SELECT
                    1,
                    'Administrador',
                    'admin@empresa.com',
                    '123456',
                    'Supervisor',
                    1
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM usuarios
                    WHERE id = 1
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTable);
            stmt.executeUpdate(insertAdmin);
        }
    }

    public static Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }
}
