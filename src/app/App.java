package app;

import java.sql.SQLException;

import gui.InitialPage;

public class App {

    public static void main(String[] args) throws SQLException {
        // Login inicial comentado para a entrega do projeto.
        // LoginPage lp = new LoginPage();
        // lp.setVisible(true);

        InitialPage initialPage = new InitialPage();
        initialPage.setVisible(true);
    }

}
