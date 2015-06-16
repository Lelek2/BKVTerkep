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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gazdi
 */
public class AdatLekerdezo implements AdatbazisKapcsolat {

    protected static Connection kapcsolat;

    public static void kapcsolatNyit() {
        try {
            Class.forName(DRIVER);
            kapcsolat = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Hiba! Hiányzik a JDBC driver.");
        } catch (SQLException e) {
            System.out.println("Hiba! Nem sikerült megnyitni a kapcsolatot az adatbázis-szerverrel.");
        }
    }

    public static void kapcsolatZar() {
        try {
            kapcsolat.close();
        } catch (SQLException e) {
            System.out.println("Hiba! Nem sikerült lezárni a kapcsolatot az adatbázis-szerverrel.");
        }
    }

    public static void tablakTorlese() {
        kapcsolatNyit();
        Statement stat;
        try {
            stat = kapcsolat.createStatement();

            try {
                stat.executeUpdate("DROP TABLE STOP_TIMES");
            } catch (SQLException e) {
                System.out.println("DROP TABLE STOP_TIMES: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE TRIPS");
            } catch (SQLException e) {
                System.out.println("DROP TABLE TRIPS: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE STOPS");
            } catch (SQLException e) {
                System.out.println("DROP TABLE STOPS: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE CALENDAR");
            } catch (SQLException e) {
                System.out.println("DROP TABLE CALENDAR: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE ROUTES");
            } catch (SQLException e) {
                System.out.println("DROP TABLE ROUTES: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE AGENCY");
            } catch (SQLException e) {
                System.out.println("DROP TABLE AGENCY: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE SHAPES");
            } catch (SQLException e) {
                System.out.println("DROP TABLE SHAPES: " + e.getMessage());
            }
            try {
                stat.executeUpdate("DROP TABLE PATHWAYS");
            } catch (SQLException e) {
                System.out.println("DROP TABLE PATHWAYS: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection: " + e.getMessage());
        }
        kapcsolatZar();
    }

    /**
     * Létrehozza a táblákat az adatbázis szerveren.
     */
    public static void tablakLetrehozasa() {
        kapcsolatNyit();
        Statement stat;
        try {
            stat = kapcsolat.createStatement();
            tablakTorlese();
            try {
                stat.executeUpdate(
                        "CREATE TABLE  SHAPES (	"
                        + "SHAPE_ID VARCHAR2(100) NOT NULL,"
                        + "shape_pt_lat VARCHAR2(200),"
                        + "shape_pt_lon VARCHAR2(200),"
                        + "shape_pt_sequence NUMBER,"
                        + "shape_dist_traveled VARCHAR2(200),"
                        + "shape_bkk_ref VARCHAR2(255))");
                //+ "CONSTRAINT SHAPE_PK PRIMARY KEY (SHAPE_ID) ENABLE)");
            } catch (SQLException e) {
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
                System.out.println("TABLE AGENCY " + e.getMessage());
            }
            try {

                stat.executeUpdate(
                        "CREATE TABLE  ROUTES (	"
                        + "ROUTE_ID VARCHAR2(100),"
                        + "AGENCY_ID VARCHAR2(100),"
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
            }
            try {
                stat.executeUpdate(
                        "CREATE TABLE  STOPS ("
                        + "STOP_ID VARCHAR2(100),"
                        + "STOP_NAME VARCHAR2(255),"
                        + "STOP_LAT VARCHAR2(200),"
                        + "STOP_LON VARCHAR2(200),"
                        + "LOCATION_TYPE VARCHAR2(1),"
                        + "PARENT_STATION VARCHAR2(100),"
                        + "WHEELCHAIR_BOARDING VARCHAR2(200),"
                        + "CONSTRAINT STOPS_PK PRIMARY KEY (STOP_ID) ENABLE)");
            } catch (SQLException e) {
                System.out.println("TABLE STOPS " + e.getMessage());
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
                        + "TRIPS_BKK_REF VARCHAR2(100),"
                        + "CONSTRAINT TRIP_ID_PK PRIMARY KEY (TRIP_ID) ENABLE,"
                        + "FOREIGN KEY (ROUTE_ID)  REFERENCES ROUTES (ROUTE_ID),"
                        //	+ "FOREIGN KEY (SHAPE_ID)  REFERENCES SHAPES (SHAPE_ID),"
                        + "FOREIGN KEY (SERVICE_ID)  REFERENCES CALENDAR (SERVICE_ID))");
            } catch (SQLException e) {
                System.out.println("TABLE TRIPS " + e.getMessage());
            }
            try {
                stat.executeUpdate(
                        "CREATE TABLE  STOP_TIMES ("
                        + "TRIP_ID VARCHAR2(100),"
                        + "ARRIVAL_TIME VARCHAR2(100),"
                        + "DEPARTURE_TIME VARCHAR2(100),"
                        + "STOP_ID VARCHAR2(100),"
                        + "STOP_SEQUENCE VARCHAR2(200),"
                        + "SHAPE_DIST_TRAVELED VARCHAR2(200),"
                        + "FOREIGN KEY (TRIP_ID) REFERENCES TRIPS (TRIP_ID),"
                        + "FOREIGN KEY (STOP_ID) REFERENCES STOPS (STOP_ID))");
            } catch (SQLException e) {
                System.out.println("TABLE STOP_TIMES " + e.getMessage());
            }
            try {
                stat.executeUpdate(
                        "CREATE TABLE  PATHWAYS ("
                        + "PATHWAY_ID VARCHAR2(100),"
                        + "PATHWAY_TYPE VARCHAR2(100),"
                        + "FROM_STOP_ID VARCHAR2(100),"
                        + "TO_STOP_ID VARCHAR2(100),"
                        + "STOP_SEQUENCE VARCHAR2(200),"
                        + "TRAVERSAL_TIME VARCHAR2(200),"
                        + "WHEELCHAIR_TRAVERSAL_TIME VARCHAR2(200)" + ")");
                // + "FOREIGN KEY (FROM_STOP_ID) REFERENCES STOPS (STOP_ID),"
                // + "FOREIGN KEY (TO_STOP_ID) REFERENCES STOPS (STOP_ID))");
            } catch (SQLException e) {
                System.out.println("TABLE PATHWAYS " + e.getMessage());
            }
        } catch (SQLException e1) {
            System.out.println("SQL statement hiba");
        }
        kapcsolatZar();
    }

    public static void adatokBeolvasasa() {
        adatBeolvasas("adatok/agency.txt", "AGENCY");
        adatBeolvasas("adatok/shapes.txt", "SHAPES");
        adatBeolvasas("adatok/routes.txt", "ROUTES");
        adatBeolvasas("adatok/calendar.txt", "CALENDAR");
        adatBeolvasas("adatok/stops.txt", "STOPS");
        adatBeolvasas("adatok/trips.txt", "TRIPS");
        adatBeolvasas("adatok/stop_times.txt", "STOP_TIMES");
        adatBeolvasas("adatok/pathways.txt", "PATHWAYS");
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

        try {
            stmt = kapcsolat.createStatement();
            
            FileReader fr = new FileReader(txt);
            BufferedReader br = new BufferedReader(fr);
            String str = br.readLine();
            String[] oszlopNevek = str.split(",");
            String prepareSql = getPrepareSql(oszlopNevek, tablaNev);
            PreparedStatement pr = kapcsolat.prepareStatement(prepareSql);
            str = br.readLine();
            tablaFeltoltese(str, oszlopNevek, pr, br);

            br.close();
            pr.close();
            stmt.close();
            kapcsolatZar();
        } catch (IOException | SQLException ex ) {
            Logger.getLogger(AdatLekerdezo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void tablaFeltoltese(String str, String[] oszlopNevek, PreparedStatement pr, BufferedReader br) throws SQLException, IOException {
        boolean trace = false;
        final int batchSize = 1000;
        int count = 0;
        while (str != null) {
            str = str.replace(", ", "|");
            String[] sorok = Arrays.copyOf(str.split(","), oszlopNevek.length);
            for (int i = 1; i < oszlopNevek.length + 1; i++) {
                pr.setString(i, sorok[i - 1]);
            }
            pr.addBatch();
            if (++count % batchSize == 0) {
                pr.executeBatch();
            }
            str = br.readLine();
            if (trace) {
                System.out.println(str);
            }
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
}
