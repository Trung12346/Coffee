/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;
import org.json.*;
import Model.JSON;
import Service.LoginDAO;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.lang.Thread;
import javax.swing.JOptionPane;
/**
 *
 * @author maith
 */
public class Main {
    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException, InterruptedException {
        boolean login = false;
        Login frameLogin = new Login();
        frameLogin.setVisible(true);
        
        while(!login) {
            Thread.sleep(500);
            // System.out.println(!ui.object.isEmpty());
            if(!frameLogin.object.isEmpty()) {
                // System.out.println("not empty anymore");
                JSONObject jo = JSON.parseJSON(frameLogin.object);
                // System.out.println(jo);
                if(LoginDAO.isExist(jo.getString("username"))) {
                    if(LoginDAO.checkCredens(jo.getString("username"), jo.getString("password"))) {
                        login = true;
                    } else {
                        JOptionPane.showMessageDialog(frameLogin, "ten nguoi dung hoac mat khau sai");
                        frameLogin.object = "";
                    }
                } else {
                    JOptionPane.showMessageDialog(frameLogin, "nguoi dung khong ton tai");
                    frameLogin.object = "";
                }
            }
        }
        
        frameLogin.setVisible(false);
        System.out.println("hi");
        
        Home frameHome = new Home();
        frameHome.setVisible(true);
    }
}
