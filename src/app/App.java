package app;

import java.sql.SQLException;

import dao.UserDAO;
import gui.LoginPage;
import model.User;

public class App {

	public static void main(String[] args) throws SQLException {
            //System.out.println("oi");
            

            LoginPage lp = new LoginPage();
            lp.setVisible(true);
            //System.out.println("oi");
                
                
            
            //User user = userDAO.getbyLogin("admin");
            //System.out.println(user.toString());

                
	}

}
