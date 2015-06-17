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
public class AdatLekerdezo implements AdatbazisKapcsolat {

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

    private static void tablakezeloLog(JTextArea TablaKezeloLOG, String str) {
        TablaKezeloLOG.setText(TablaKezeloLOG.getText() + "\n"
                + new Timestamp(System.currentTimeMillis()) + ": " + str);
        TablaKezeloLOG.repaint();
    }

    public static void tablakTorlese(JTextArea TablaKezeloLOG) {
        kapcsolatNyit();
        Statement stat;
        boolean gotError = false;
        try {
            stat = kapcsolat.createStatement();

            try {
                stat.executeUpdate("DROP TABLE STOP_TIMES");
            } catch (SQLException e) {
                System.out.println("DROP TABLE STOP_TIMES: " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "DROP TABLE STOP_TIMES: " + e.getMessage());
                gotError = true;
            }
            try {
                stat.executeUpdate("DROP TABLE TRIPS");
            } catch (SQLException e) {
                System.out.println("DROP TABLE TRIPS: " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "DROP TABLE TRIPS: " + e.getMessage());
                gotError = true;
            }
            try {
                stat.executeUpdate("DROP TABLE STOPS");
            } catch (SQLException e) {
                System.out.println("DROP TABLE STOPS: " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "DROP TABLE STOPS: " + e.getMessage());
                gotError = true;
            }
            try {
                stat.executeUpdate("DROP TABLE CALENDAR");
            } catch (SQLException e) {
                System.out.println("DROP TABLE CALENDAR: " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "DROP TABLE CALENDAR: " + e.getMessage());
                gotError = true;
            }
            try {
                stat.executeUpdate("DROP TABLE ROUTES");
            } catch (SQLException e) {
                System.out.println("DROP TABLE ROUTES: " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "DROP TABLE ROUTES: " + e.getMessage());
                gotError = true;
            }
            try {
                stat.executeUpdate("DROP TABLE AGENCY");
            } catch (SQLException e) {
                System.out.println("DROP TABLE AGENCY: " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "DROP TABLE AGENCY: " + e.getMessage());
                gotError = true;
            }
            try {
                stat.executeUpdate("DROP TABLE SHAPES");
            } catch (SQLException e) {
                System.out.println("DROP TABLE SHAPES: " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "DROP TABLE SHAPES: " + e.getMessage());
                gotError = true;
            }
            try {
                stat.executeUpdate("DROP TABLE PATHWAYS");
            } catch (SQLException e) {
                System.out.println("DROP TABLE PATHWAYS: " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "DROP TABLE PATHWAYS: " + e.getMessage());
                gotError = true;
            }
        } catch (SQLException e) {
            System.out.println("Connection: " + e.getMessage());
            tablakezeloLog(TablaKezeloLOG, "Connection " + e.getMessage());
            gotError = true;
        }
        if (gotError == false) {
            tablakezeloLog(TablaKezeloLOG, "Táblák törölve");
        }
        kapcsolatZar();
    }

    /**
     * Létrehozza a táblákat az adatbázis szerveren.
     */
    public static void tablakLetrehozasa(JTextArea TablaKezeloLOG) {
        kapcsolatNyit();
        Statement stat;
        boolean gotError = false;
        try {
            stat = kapcsolat.createStatement();
            try {
                stat.executeUpdate(
                        "CREATE TABLE  SHAPES (	"
                        + "SHAPE_ID VARCHAR2(100) NOT NULL,"
                        + "shape_pt_lat VARCHAR2(200),"
                        + "shape_pt_lon VARCHAR2(200),"
                        + "shape_pt_sequence VARCHAR2(200),"
                        + "shape_dist_traveled VARCHAR2(200))");

            } catch (SQLException e) {
                System.out.println("TABLE SHAPES " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "TABLE SHAPES " + e.getMessage());
                gotError = true;
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
                System.out.println("TABLE AGENCY " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "TABLE AGENCY " + e.getMessage());
                gotError = true;
            }
            try {

                stat.executeUpdate(
                        "CREATE TABLE  ROUTES (	"
                        + "ROUTE_ID VARCHAR2(100),"
                        + "AGENCY_ID VARCHAR2(100) NOT NULL,"
                        + "ROUTE_SHORT_NAME VARCHAR2(255),"
                        + "ROUTE_LONG_NAME VARCHAR2(255),"
                        + "ROUTE_DESC VARCHAR2(255),"
                        + "ROUTE_TYPE VARCHAR2(200)," // TODO number volt
                        + "ROUTE_COLOR VARCHAR2(100),"
                        + "ROUTE_TEXT_COLOR VARCHAR2(100),"
                        + "CONSTRAINT ROUTES_PK PRIMARY KEY (ROUTE_ID) ENABLE,"
                        + "FOREIGN KEY (AGENCY_ID) REFERENCES AGENCY (AGENCY_ID))");
            } catch (SQLException e) {
                System.out.println("TABLE ROUTES " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "TABLE ROUTES " + e.getMessage());
                gotError = true;
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
                System.out.println("TABLE CALENDAR " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "TABLE CALENDAR " + e.getMessage());
                gotError = true;
            }
            try {
                stat.executeUpdate(
                        "CREATE TABLE  STOPS ("
                        + "STOP_ID VARCHAR2(100),"
                        + "STOP_NAME VARCHAR2(255),"
                        + "STOP_LAT VARCHAR2(200),"
                        + "STOP_LON VARCHAR2(200),"
                        + "STOP_CODE VARCHAR2(200),"
                        + "LOCATION_TYPE VARCHAR2(1),"
                        + "PARENT_STATION VARCHAR2(100),"
                        + "WHEELCHAIR_BOARDING VARCHAR2(200),"
                        + "CONSTRAINT STOPS_PK PRIMARY KEY (STOP_ID) ENABLE)");
            } catch (SQLException e) {
                System.out.println("TABLE STOPS " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "TABLE STOPS " + e.getMessage());
                gotError = true;
            }
            try {
                stat.executeUpdate(
                        "CREATE TABLE  TRIPS ("
                        + "ROUTE_ID VARCHAR2(100),"
                        + "SERVICE_ID VARCHAR2(100),"
                        + "TRIP_ID VARCHAR2(100) NOT NULL,"
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
                System.out.println("TABLE TRIPS " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "TABLE TRIPS " + e.getMessage());
                gotError = true;
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
                System.out.println("TABLE STOP_TIMES " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "TABLE STOP_TIMES " + e.getMessage());
                gotError = true;
            }
            try {
                stat.executeUpdate(
                        "CREATE TABLE  PATHWAYS ("
                        + "PATHWAY_ID VARCHAR2(100),"
                        + "PATHWAY_TYPE VARCHAR2(100),"
                        + "FROM_STOP_ID VARCHAR2(100),"
                        + "TO_STOP_ID VARCHAR2(100),"
                        + "TRAVERSAL_TIME VARCHAR2(200),"
                        + "WHEELCHAIR_TRAVERSAL_TIME VARCHAR2(200)" + ")");
                // + "FOREIGN KEY (FROM_STOP_ID) REFERENCES STOPS (STOP_ID),"
                // + "FOREIGN KEY (TO_STOP_ID) REFERENCES STOPS (STOP_ID))");
            } catch (SQLException e) {
                System.out.println("TABLE PATHWAYS " + e.getMessage());
                tablakezeloLog(TablaKezeloLOG, "TABLE PATHWAYS " + e.getMessage());
                gotError = true;
            }
        } catch (SQLException e1) {
            System.out.println("SQL statement hiba");
            tablakezeloLog(TablaKezeloLOG, "SQL statement hiba");
            gotError = true;
        }
        if (gotError == false) {
            tablakezeloLog(TablaKezeloLOG, "Táblák létrehozva");
        }
        kapcsolatZar();
    }

    public static void adatokBeolvasasa() {
        adatBeolvasas("adatok/agency.txt", "AGENCY");
        //    tablakezeloLog(TablaKezeloLOG, "AGENCY tábla feltöltve");
        adatBeolvasas("adatok/shapes.txt", "SHAPES");
        //     tablakezeloLog(TablaKezeloLOG, "SHAPES tábla feltöltve");
        adatBeolvasas("adatok/routes.txt", "ROUTES");
        //     tablakezeloLog(TablaKezeloLOG, "ROUTES tábla feltöltve");
        adatBeolvasas("adatok/calendar.txt", "CALENDAR");
        //    tablakezeloLog(TablaKezeloLOG, "CALENDAR tábla feltöltve");
        adatBeolvasas("adatok/stops.txt", "STOPS");
        //    tablakezeloLog(TablaKezeloLOG, "STOPS tábla feltöltve");
        adatBeolvasas("adatok/trips.txt", "TRIPS");
        //    tablakezeloLog(TablaKezeloLOG, "TRIPS tábla feltöltve");
        adatBeolvasas("adatok/stop_times.txt", "STOP_TIMES");
        //    tablakezeloLog(TablaKezeloLOG, "STOP_TIMES tábla feltöltve");
        adatBeolvasas("adatok/pathways.txt", "PATHWAYS");
        //   tablakezeloLog(TablaKezeloLOG, "PATHWAYS tábla feltöltve");
    }

    /**
     * Tábla adatok beolvasása TXT file-okból, és azok feltöltése az adatbázis
     * szerverre.
     *
     * @param txt
     * @param dm
     * @throws IOException
     * @throws SQLException
     */
    private static void adatBeolvasas(String txt, String tablaNev) {
        boolean trace = true;
        kapcsolatNyit();
        Statement stmt;
        final int batchSize = 1000;
        int count = 0;
        try {
            stmt = kapcsolat.createStatement();

            String str = "";
            FileReader fr = new FileReader(txt);

            BufferedReader br = new BufferedReader(fr);
            str = br.readLine();
            String[] oszlopNevek = str.split(",");
            String oszlopKerdojel = "(";
            for (int i = 0; i < oszlopNevek.length; i++) {
                oszlopKerdojel += "?,";
            }
            oszlopKerdojel = oszlopKerdojel.substring(0, oszlopKerdojel.length() - 1);
            oszlopKerdojel += ")";
            String prepareSql = "INSERT INTO " + tablaNev + " VALUES " + oszlopKerdojel;
            System.out.println(prepareSql);
            PreparedStatement pr = kapcsolat.prepareStatement(prepareSql);
            str = br.readLine();

            /*  FileReader fr = new FileReader(txt);
             BufferedReader br = new BufferedReader(fr);
             String str = br.readLine();
             String[] oszlopNevek = str.split(",");
             String prepareSql = getPrepareSql(oszlopNevek, tablaNev);
             PreparedStatement pr = kapcsolat.prepareStatement(prepareSql);
             str = br.readLine();*/
            // tablaFeltoltese(str, oszlopNevek, pr, br);
            System.out.println("STR: length:" + str.length() + " str: " + str);
            while (str != null) {
                str = str.replace(", ", "|");
                if (trace) {
                    System.out.println(str);
                }
                String[] sorok = Arrays.copyOf(str.split(","), oszlopNevek.length);
                for (int i = 1; i < oszlopNevek.length + 1; i++) {
                    pr.setString(i, sorok[i - 1]);
                }
                /*pr.addBatch();
                 System.out.println(count);
                 if (++count % batchSize == 0) {
                 pr.executeBatch();
                 }
                 */
                try {
                    pr.addBatch();
                } catch (SQLException e) {
                    System.out.println(str);
                    e.printStackTrace();
                }
                //stmt.execute(sql);
                System.out.println(count);
                if (++count % batchSize == 0) {
                    System.out.println("KILOVE");
                    pr.executeBatch();
                    count = 0;
                }
                str = br.readLine();
            }
            System.out.println(str + "  sssssstr " );
            pr.executeBatch();

            br.close();
            pr.close();
            stmt.close();
            kapcsolatZar();

        } catch (IOException | SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(AdatLekerdezo.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void tablaFeltoltese(String str, String[] oszlopNevek, PreparedStatement pr, BufferedReader br) throws SQLException, IOException {
        boolean trace = true;
        final int batchSize = 1000;
        int count = 0;
        while (str != null) {
            str = str.replace(", ", "|");
            if (trace) {
                System.out.println(str);
            }
            String[] sorok = Arrays.copyOf(str.split(","), oszlopNevek.length);
            for (int i = 1; i < oszlopNevek.length + 1; i++) {
                pr.setString(i, sorok[i - 1]);
            }
            pr.addBatch();
            if (++count % batchSize == 0) {
                pr.executeBatch();
            }
            str = br.readLine();
        }
        pr.executeBatch();
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

    /**
     * Jaratok lekérdezése járatTipus alapján.
     *
     * @param jaratTipus
     * @return
     */
    public static Object[][] jaratLekerdezese(String jaratTipus) {
        boolean trace = false;
        kapcsolatNyit();
        Object[][] obj = new Object[1][1];
        try {
            Statement stmt = kapcsolat.createStatement();
            String[] jaratTipusok = jaratTipus.split(",");
            String whereSql = "";
            for (int i = 0; i < jaratTipusok.length; i++) {
                whereSql += "routes.route_type = '" + jaratTipusok[i] + "' or ";
            }
            whereSql = whereSql.substring(0, whereSql.length() - 4);
            String sql = "select DISTINCT routes.route_short_name "
                    + "from routes "
                    + "inner join TRIPS on routes.route_id = trips.route_id "
                    + "inner join stop_times on trips.trip_id = stop_times.trip_id "
                    + "inner join stops on stop_times.stop_id = stops.stop_id "
                    + "where " + whereSql + " order by routes.route_short_name";
            String sqlCount = "select COUNT(DISTINCT routes.route_short_name) "
                    + "from routes "
                    + "inner join TRIPS on routes.route_id = trips.route_id "
                    + "inner join stop_times on trips.trip_id = stop_times.trip_id "
                    + "inner join stops on stop_times.stop_id = stops.stop_id "
                    + "where " + whereSql;
            ResultSet rsCount = stmt.executeQuery(sqlCount);
            rsCount.next();
            obj = new Object[rsCount.getInt(1)][1];
            int i = 0;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] adatok = new Object[2];
                adatok[0] = rs.getObject("ROUTE_SHORT_NAME");
                obj[i][0] = rs.getObject(1);
                i++;
            }
            if (trace) {
                System.out.println(sql);
                System.out.println("ROW num: " + rsCount.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("JaratLekerdezes statement hiba");
        }
        kapcsolatZar();
        return obj;

    }

    /*   String sql = "SELECT stops.stop_id, stops.stop_name, stops.stop_lat, stops.stop_lon, MAX(stop_times.shape_dist_traveled) as MaxDist "
     + "FROM trips "
     + "INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id "
     + "INNER JOIN stops ON stops.stop_id = stop_times.stop_id "
     + "WHERE trips.ROUTE_ID= '" + jarat + "' AND trips.direction_id = '" + directionId + "' "
     + "GROUP BY stops.stop_id, stops.stop_name, stops.stop_lat, stops.stop_lon "
     + "ORDER BY MaxDist"; */
}
