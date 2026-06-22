package app;

import java.sql.SQLException;

import gui.LoginPage;

public class App {

    public static void main(String[] args) throws SQLException {
        LoginPage lp = new LoginPage();
        lp.setVisible(true);
    }

}
