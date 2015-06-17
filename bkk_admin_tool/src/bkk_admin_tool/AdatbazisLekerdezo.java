/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bkk_admin_tool;

import static bkk_admin_tool.AdatbazisKapcsolat.DRIVER;
import static bkk_admin_tool.AdatbazisKapcsolat.PASSWORD;
import static bkk_admin_tool.AdatbazisKapcsolat.URL;
import static bkk_admin_tool.AdatbazisKapcsolat.USER;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author Robi
 */
public class AdatbazisLekerdezo {

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
     * Jaratok lekérdezése járatTipus alapján.
     *
     * @param jaratTipus
     * @return
     */
    public static AbstractListModel jaratLekerdezese(String jaratTipus) {

        boolean trace = false;
        kapcsolatNyit();
        ArrayList<String> jaratLista = new ArrayList<>();
        try {
            Statement stmt = kapcsolat.createStatement();
            String[] jaratTipusok = jaratTipus.split(",");
            String whereSql = "";
            for (String jaratTipusok1 : jaratTipusok) {
                whereSql += "routes.route_type = '" + jaratTipusok1 + "' or ";
            }
            whereSql = whereSql.substring(0, whereSql.length() - 4);
            String sql = "select DISTINCT routes.route_short_name "
                    + "from routes "
                    + "inner join TRIPS on routes.route_id = trips.route_id "
                    + "inner join stop_times on trips.trip_id = stop_times.trip_id "
                    + "inner join stops on stop_times.stop_id = stops.stop_id "
                    + "where " + whereSql + " order by routes.route_short_name";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                jaratLista.add(rs.getString(1));
            }
            AdatbazisKezelo.consoleMsg(sql);
            if (trace) {
                System.out.println(sql);
                System.out.println("jaratLista.size: " + jaratLista.size());
            }
        } catch (SQLException e) {
            System.out.println("SQL hiba járatok lekérdezésénél: " + e.getMessage());
        }
        kapcsolatZar();
        AbstractListModel abm = new AbstractListModel() {
            private final ArrayList<String> lista = jaratLista;

            @Override
            public int getSize() {
                return lista.size();
            }

            @Override
            public Object getElementAt(int index) {
                return lista.get(index);
            }
        };

        return abm;

    }

    /**
     * Adott járat megállóinak lekérdése koordinátával.
     *
     * @param jarat
     * @return Táblázatban a megállók nevét és koordinátáját adja vissza
     */
    public static String[][] jaratMegalloi(String jarat) {
        boolean trace = true;
        kapcsolatNyit();
        String[][] obj = new String[1][1];
        try {

            Statement stmt = AdatbazisKezelo.kapcsolat.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sql = "select distinct shapes.shape_pt_lat, shapes.shape_pt_lon, COUNT(shapes.shape_id), COUNT(shapes.SHAPE_PT_SEQUENCE) as aa "
                    + "from routes "
                    + "inner join trips on routes.route_id = trips.route_id "
                    + "inner join SHAPES on trips.shape_id = SHAPES.shape_id "
                    + "where routes.ROUTE_SHORT_NAME = '" + jarat + "'"
                    + "group by shapes.shape_pt_lat, shapes.shape_pt_lon "
                    + "order by aa";
            String sqlCount = "select COUNT(distinct shapes.shape_pt_lat), COUNT(distinct shapes.shape_pt_lon), COUNT(distinct shapes.shape_id) "
                    + "from SHAPES "
                    + "inner join TRIPS on SHAPES.shape_id = trips.shape_id "
                    + "inner join routes on trips.route_id = routes.route_id "
                    + "where routes.ROUTE_SHORT_NAME = '" + jarat + "'";

            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            rs.last();

            int rowNum = rs.getRow();
            obj = new String[rowNum - 1][3];
            int i = 0;
            rs.first();
            while (rs.next()) {
                obj[i][0] = rs.getString(1);
                obj[i][1] = rs.getString(2);
                obj[i][2] = rs.getString(3);
                i++;
            }
            if (trace) {
                System.out.println(sql);
                System.out.println("ROW num: " + rowNum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
