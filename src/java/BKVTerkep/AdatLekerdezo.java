/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BKVTerkep;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gazdi
 */
public class AdatLekerdezo extends AdatbazisKapcsolat {

    protected static Connection kapcsolat;

    public static void kapcsolatNyit() {
        try {
            AdatbazisKapcsolat.LoadDBConfig();
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

    public static List<JaratBean> jaratokLekerdezese(Set<JaratTipus> jaratTipusok) {
        List<JaratBean> jaratLista = new ArrayList<JaratBean>();
        AdatLekerdezo.kapcsolatNyit();
        try {
            Statement stmt = kapcsolat.createStatement();
            String whereSql = "";
            for (JaratTipus jt : jaratTipusok) {
                whereSql += "routes.route_type = '" + jt.getVal() + "' or ";
            }
            whereSql = whereSql.substring(0, whereSql.length() - 4);
            String sql = "select DISTINCT routes.route_id, routes.route_short_name, routes.route_desc "
                    + "from routes "
                    + "inner join TRIPS on routes.route_id = trips.route_id "
                    + "inner join stop_times on trips.trip_id = stop_times.trip_id "
                    + "inner join stops on stop_times.stop_id = stops.stop_id "
                    + "where " + whereSql + " order by routes.route_short_name, routes.route_id";
            
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                jaratLista.add(new JaratBean(rs.getString(1), JaratBean.GetJaratNev(rs.getString(2), rs.getString(3))));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        AdatLekerdezo.kapcsolatZar();
        return jaratLista;
    }

    public static List<JaratBean> getBKVJaratLista() {
        return jaratokLekerdezese(EnumSet.of(
                JaratTipus.Villamos,
                JaratTipus.Metro,
                JaratTipus.Hev,
                JaratTipus.Busz));
    }

    public static List<JaratMegalloBean> jaratokMegallokLekerdezese(String jarat, String directionId) {
        List<JaratMegalloBean> jaratMegallok = new ArrayList<JaratMegalloBean>();
        AdatLekerdezo.kapcsolatNyit();
        try {
            Statement stmt = AdatLekerdezo.kapcsolat.createStatement();
            String sql = "SELECT stops.stop_id, stops.stop_name, stops.stop_lat, stops.stop_lon, MAX(stop_times.shape_dist_traveled) as MaxDist "
                    + "FROM trips "
                    + "INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id "
                    + "INNER JOIN stops ON stops.stop_id = stop_times.stop_id "
                    + "WHERE trips.ROUTE_ID= '" + jarat + "' AND trips.direction_id = '" + directionId + "' "
                    + "GROUP BY stops.stop_id, stops.stop_name, stops.stop_lat, stops.stop_lon "
                    + "ORDER BY MaxDist";

            Integer megalloCounter = 1;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                jaratMegallok.add(new JaratMegalloBean(rs.getString(2), megalloCounter++, rs.getDouble(3), rs.getDouble(4)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        AdatLekerdezo.kapcsolatZar();

        return jaratMegallok;
    }
}
