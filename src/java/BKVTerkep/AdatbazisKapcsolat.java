/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BKVTerkep;

import java.util.ResourceBundle;

public class AdatbazisKapcsolat {

    static String URL = "url";
    static String USER = "user";
    static String PASSWORD = "password";
    static String DRIVER = "driver";
    static Boolean isConfigAlreadyLoaded = false;

    public static void LoadDBConfig() {
        if (!isConfigAlreadyLoaded) {
            try {
                ResourceBundle myResources = ResourceBundle.getBundle("DBconfig");
                URL = (String) myResources.getObject("DBurl");
                USER = (String) myResources.getObject("DBuser");
                PASSWORD = (String) myResources.getObject("DBpassword");
                DRIVER = (String) myResources.getObject("DBdriver");
                
                isConfigAlreadyLoaded = true;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
