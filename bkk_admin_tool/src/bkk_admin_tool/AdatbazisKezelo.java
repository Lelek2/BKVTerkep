/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bkk_admin_tool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author gazdi
 */
public class AdatbazisKezelo implements AdatbazisKapcsolat {

    protected static Connection kapcsolat;

    private static void kapcsolatNyit() {
        try {
            Class.forName(DRIVER);
            kapcsolat = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Hiba! Hiányzik a JDBC driver.");
        } catch (SQLException e) {
            System.out.println("Hiba! Nem sikerült megnyitni a kapcsolatot az adatbázis-szerverrel.");
        }
    }

    private static void kapcsolatZar() {
        try {
            kapcsolat.close();
        } catch (SQLException e) {
            System.out.println("Hiba! Nem sikerült lezárni a kapcsolatot az adatbázis-szerverrel.");
        }
    }

    /**
     * Tábla adatok beolvasása TXT file-okból, és azok feltöltése az adatbázis
     * szerverre.
     *
     * @param txt
     * @param tablaNev
     * @param dm
     * @throws IOException
     * @throws SQLException
     */
    private static void adatBeolvasas(String txt, String tablaNev) throws IOException {
        boolean trace = false;
        kapcsolatNyit();
        Statement stmt;
        try {
            stmt = kapcsolat.createStatement();
            String str = "";
            FileReader fr = new FileReader(txt);
            BufferedReader br = new BufferedReader(fr);
            str = br.readLine();
            String[] oszlopNevek = str.split(",");
            String prepareSql = getPrepareSql(oszlopNevek, tablaNev);
            PreparedStatement pr = kapcsolat.prepareStatement(prepareSql);
            str = br.readLine();
            sorokBeolvasasa(str, oszlopNevek, pr, br);

            pr.executeBatch();

            br.close();
            pr.close();
            stmt.close();
            kapcsolatZar();
            if (trace) {
                System.out.println(prepareSql);
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        consoleMsg(tablaNev + " Feltöltése elkészült");

    }

    private static void sorokBeolvasasa(String str, String[] oszlopNevek, PreparedStatement pr, BufferedReader br) throws SQLException, IOException {
        boolean trace = false;
        final int batchSize = 1000;
        int count = 0;
        while (str != null) {
            str = str.replace(", ", "|");
            String[] sorok = Arrays.copyOf(str.split(","), oszlopNevek.length);
            for (int i = 1; i < oszlopNevek.length + 1; i++) {
                pr.setString(i, sorok[i - 1]);
            }
            try {
                pr.addBatch();
            } catch (SQLException e) {
                System.out.println(str);
                e.printStackTrace();
            }
            if (++count % batchSize == 0) {
                pr.executeBatch();
            }
            str = br.readLine();
            if (trace) {
                System.out.println(str);
            }
        }
    }

    private static String getPrepareSql(String[] oszlopNevek, String tablaNev) {
        String oszlopKerdojel = "(";
        for (int i = 0; i < oszlopNevek.length; i++) {
            oszlopKerdojel += "?,";
        }
        oszlopKerdojel = oszlopKerdojel.substring(0, oszlopKerdojel.length() - 1);
        oszlopKerdojel += ")";
        String prepareSql = "INSERT INTO " + tablaNev + " VALUES " + oszlopKerdojel;
        return prepareSql;
    }

    protected static void consoleMsg(String str) {
        StringBuilder sb = new StringBuilder(new Timestamp(System.currentTimeMillis()).toString());
        sb.delete(0, 10);
        sb.delete(8, sb.length() - 1);
        System.out.println(sb + ": " + str);
    }

    /**
     * adatBeolvasas függvény felhívása minden egyes TXT file-ra.
     *
     * @see adatBeolvasas
     */
    public static void adatokBeolvasasa() {
        try {
            adatBeolvasas("adatok/agency.txt", "AGENCY");
            adatBeolvasas("adatok/shapes.txt", "SHAPES");
            adatBeolvasas("adatok/routes.txt", "ROUTES");
            adatBeolvasas("adatok/calendar.txt", "CALENDAR");
            adatBeolvasas("adatok/stops.txt", "STOPS");
            adatBeolvasas("adatok/trips.txt", "TRIPS");
            adatBeolvasas("adatok/stop_times.txt", "STOP_TIMES");
            adatBeolvasas("adatok/pathways.txt", "PATHWAYS");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Létrehozza a táblákat az adatbázis szerveren.
     */
    public static void tablakLetrehozasa() {
        kapcsolatNyit();
        boolean gotError = false;
        Statement stat;
        try {
            stat = kapcsolat.createStatement();

            try {
                stat.executeUpdate(
                        "CREATE TABLE  SHAPES (	"
                        + "SHAPE_ID VARCHAR2(100) NOT NULL,"
                        + "shape_pt_sequence VARCHAR2(200),"
                        + "shape_pt_lat VARCHAR2(200),"
                        + "shape_pt_lon VARCHAR2(200),"
                        + "shape_dist_traveled VARCHAR2(200))");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("TABLE SHAPES " + e.getMessage());
            }
            try {
                stat.executeUpdate(
                        "CREATE TABLE  AGENCY (	"
                        + "AGENCY_ID VARCHAR2(100) NOT NULL,"
                        + "AGENCY_NAME VARCHAR2(200),"
                        + "AGENCY_URL VARCHAR2(200),"
                        + "AGENCY_TIMEZONE VARCHAR2(200),"
                        + "AGENCY_LANG VARCHAR2(255),"
                        + "AGENCY_PHONE VARCHAR2(255),"
                        + "CONSTRAINT AGENCY_PK PRIMARY KEY (AGENCY_ID) ENABLE)");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("TABLE AGENCY " + e.getMessage());
            }
            try {

                stat.executeUpdate(
                        "CREATE TABLE  ROUTES (	"
                        + "AGENCY_ID VARCHAR2(100),"
                        + "ROUTE_ID VARCHAR2(100),"
                        + "ROUTE_SHORT_NAME VARCHAR2(255),"
                        + "ROUTE_LONG_NAME VARCHAR2(255),"
                        + "ROUTE_TYPE VARCHAR2(200)," // TODO number volt
                        + "ROUTE_DESC VARCHAR2(255),"
                        + "ROUTE_COLOR VARCHAR2(200),"
                        + "ROUTE_TEXT_COLOR VARCHAR2(200),"
                        + "CONSTRAINT ROUTES_PK PRIMARY KEY (ROUTE_ID) ENABLE,"
                        + "FOREIGN KEY (AGENCY_ID) REFERENCES AGENCY (AGENCY_ID))");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("TABLE ROUTES " + e.getMessage());
            }
            try {

                stat.executeUpdate(
                        "CREATE TABLE  CALENDAR ("
                        + "SERVICE_ID VARCHAR2(100), "
                        + "MONDAY VARCHAR2(200), "
                        + "TUESDAY VARCHAR2(200), "
                        + "WEDNESDAY VARCHAR2(200),"
                        + "THURSDAY VARCHAR2(200),"
                        + "FRIDAY VARCHAR2(200), "
                        + "SATURDAY VARCHAR2(200),"
                        + "SUNDAY VARCHAR2(200), "
                        + "START_DATE VARCHAR2(200),"
                        + "END_DATE VARCHAR2(200),"
                        + "CONSTRAINT CALENDAR_PK PRIMARY KEY (SERVICE_ID) ENABLE)");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("TABLE CALENDAR " + e.getMessage());
            }

            try {

                stat.executeUpdate(
                        "CREATE TABLE  STOPS ("
                        + "STOP_ID VARCHAR2(100),"
                        + "STOP_NAME VARCHAR2(255),"
                        + "STOP_LAT VARCHAR2(200),"
                        + "STOP_LON VARCHAR2(200),"
                        + "STOP_CODE VARCHAR2(200),"
                        + "LOCATION_TYPE VARCHAR2(10),"
                        + "PARENT_STATION VARCHAR2(100),"
                        + "WHEELCHAIR_BOARDING VARCHAR2(200),"
                        + "CONSTRAINT STOPS_PK PRIMARY KEY (STOP_ID) ENABLE)");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("TABLE STOPS " + e.getMessage());
            }
            try {

                stat.executeUpdate(
                        "CREATE TABLE  TRIPS ("
                        + "ROUTE_ID VARCHAR2(100),"
                        + "TRIP_ID VARCHAR2(100) NOT NULL,"
                        + "SERVICE_ID VARCHAR2(100),"
                        + "TRIP_HEADSIGN VARCHAR2(255),"
                        + "DIRECTION_ID VARCHAR2(200) NOT NULL,"
                        + "BLOCK_ID VARCHAR2(100),"
                        + "SHAPE_ID VARCHAR2(100),"
                        + "WHEELCHAIR_ACCESSIBLE VARCHAR2(200),"
                        + "BIKES_ALLOWED VARCHAR2(200),"
                        + "CONSTRAINT TRIP_ID_PK PRIMARY KEY (TRIP_ID) ENABLE,"
                        + "FOREIGN KEY (ROUTE_ID)  REFERENCES ROUTES (ROUTE_ID),"
                        + "FOREIGN KEY (SERVICE_ID)  REFERENCES CALENDAR (SERVICE_ID))");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("TABLE TRIPS " + e.getMessage());
            }
            try {

                stat.executeUpdate(
                        "CREATE TABLE  STOP_TIMES ("
                        + "TRIP_ID VARCHAR2(100),"
                        + "STOP_ID VARCHAR2(100),"
                        + "ARRIVAL_TIME VARCHAR2(100),"
                        + "DEPARTURE_TIME VARCHAR2(100),"
                        + "STOP_SEQUENCE VARCHAR2(200),"
                        + "SHAPE_DIST_TRAVELED VARCHAR2(200),"
                        + "FOREIGN KEY (TRIP_ID) REFERENCES TRIPS (TRIP_ID),"
                        + "FOREIGN KEY (STOP_ID) REFERENCES STOPS (STOP_ID))");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("TABLE STOP_TIMES " + e.getMessage());
            }
            try {
                stat.executeUpdate(
                        "CREATE TABLE  PATHWAYS ("
                        + "PATHWAY_ID VARCHAR2(100),"
                        + "PATHWAY_TYPE VARCHAR2(100),"
                        + "FROM_STOP_ID VARCHAR2(100),"
                        + "TO_STOP_ID VARCHAR2(100),"
                        + "TRAVERSAL_TIME VARCHAR2(200),"
                        + "WHEELCHAIR_TRAVERSAL_TIME VARCHAR2(200),"// + ")");
                        + "FOREIGN KEY (FROM_STOP_ID) REFERENCES STOPS (STOP_ID),"
                        + "FOREIGN KEY (TO_STOP_ID) REFERENCES STOPS (STOP_ID))");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("TABLE PATHWAYS " + e.getMessage());
            }
        } catch (SQLException e1) {
            gotError = true;
            e1.printStackTrace();
        }
        if (!gotError) {
            consoleMsg("Táblák sikeresen létrehozva");
        }
        kapcsolatZar();
    }

    /**
     * Törli a táblákat az adatbázis szerveren.
     */
    public static void tablakTorlese() {
        kapcsolatNyit();
        Statement stat;
        boolean gotError = false;
        try {
            stat = kapcsolat.createStatement();

            try {
                stat.executeUpdate("DROP TABLE STOP_TIMES");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("DROP TABLE STOP_TIMES: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE TRIPS");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("DROP TABLE TRIPS: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE PATHWAYS");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("DROP TABLE PATHWAYS: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE STOPS");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("DROP TABLE STOPS: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE CALENDAR");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("DROP TABLE CALENDAR: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE ROUTES");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("DROP TABLE ROUTES: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE AGENCY");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("DROP TABLE AGENCY: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE SHAPES");
            } catch (SQLException e) {
                gotError = true;
                System.out.println("DROP TABLE SHAPES: " + e.getMessage());
            }

        } catch (SQLException e) {
            gotError = true;
            System.out.println("Connection: " + e.getMessage());
        }
        if (!gotError) {
            consoleMsg("Táblák sikeresen törölve");
        }
        kapcsolatZar();
    }

}
