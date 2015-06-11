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

    public static List<JaratBean> jaratokLekerdezese(Set<JaratTipus> jaratTipusok) {
        //boolean trace = false;
        List<JaratBean> jaratLista = new ArrayList<JaratBean>();
        AdatLekerdezo.kapcsolatNyit();
        //Object[][] obj = new Object[1][1];
        try {
            Statement stmt = kapcsolat.createStatement();
            //DefaultTableModel table = new DefaultTableModel();
            //table.addColumn("JaratSzam");
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

//            String sqlCount = "select COUNT(DISTINCT routes.route_short_name) "
//                    + "from routes "
//                    + "inner join TRIPS on routes.route_id = trips.route_id "
//                    + "inner join stop_times on trips.trip_id = stop_times.trip_id "
//                    + "inner join stops on stop_times.stop_id = stops.stop_id "
//                    + "where " + whereSql;
//            ResultSet rsCount = stmt.executeQuery(sqlCount);
//            rsCount.next();
            //obj = new Object[rsCount.getInt(1)][1];
            //int i=0;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                jaratLista.add(new JaratBean(rs.getString(1), JaratBean.GetJaratNev(rs.getString(2), rs.getString(3))));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        AdatLekerdezo.kapcsolatZar();
        //return obj;
        return jaratLista;
    }

    public static List<JaratBean> getBKVJaratLista() {
        return jaratokLekerdezese(EnumSet.of(
                JaratTipus.Villamos,
                JaratTipus.Metro,
                JaratTipus.Hev,
                JaratTipus.Busz));
    }

//    public static List<JaratMegalloBean> jaratokMegallokLekerdezese(String jarat) {
//        List<JaratMegalloBean> jaratMegallok = new ArrayList<JaratMegalloBean>();
////        boolean trace = false;
//        AdatLekerdezo.kapcsolatNyit();
//        try {
//            Statement stmt = AdatLekerdezo.kapcsolat.createStatement();
//            String sql = "SELECT stops.stop_id, stops.stop_name, stops.stop_lat, stops.stop_lon, MAX(stop_times.shape_dist_traveled) as MaxDist "
//                    + "FROM trips "
//                    + "INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id "
//                    + "INNER JOIN stops ON stops.stop_id = stop_times.stop_id "
//                    + "WHERE trips.ROUTE_ID= '" + jarat + "' AND trips.direction_id = 0 "
//                    + "GROUP BY stops.stop_id, stops.stop_name, stops.stop_lat, stops.stop_lon "
//                    + "ORDER BY MaxDist";
//
//            ResultSet rs = stmt.executeQuery(sql);
//            while (rs.next()) {
//                jaratMegallok.add(new JaratMegalloBean(rs.getString(2), rs.getInt(5), rs.getDouble(3), rs.getDouble(4)));
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        AdatLekerdezo.kapcsolatZar();
//
//        return jaratMegallok;
//    }
    
         
         
         
             public static List<JaratMegalloBean> jaratokMegallokLekerdezese(String jarat) {
        List<JaratMegalloBean> jaratMegallok = new ArrayList<JaratMegalloBean>();
//        boolean trace = false;
        AdatLekerdezo.kapcsolatNyit();
        try {
            Statement stmt = AdatLekerdezo.kapcsolat.createStatement();
            String sql = "select distinct shapes.shape_pt_lat as lat, shapes.shape_pt_lon as lon, shapes.shape_id as sid, routes.route_id, shapes.SHAPE_PT_SEQUENCE "
                    + "FROM routes "
                    + "inner join trips on routes.route_id = trips.route_id "
                    + "inner join SHAPES on trips.shape_id = SHAPES.shape_id "
                    + "where routes.ROUTE_ID = '" + jarat +"' "
                    + "and trips.TRIP_ID = (SELECT   MIN(trips.TRIP_ID) from trips inner join routes on trips.route_id = routes.route_id  where routes.ROUTE_ID = '" + jarat +"' and trips.DIRECTION_ID = '0' ) "
                    + "and trips.DIRECTION_ID = '0' "
                    + "order by shapes.SHAPE_PT_SEQUENCE";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                jaratMegallok.add(new JaratMegalloBean(rs.getString(3), rs.getInt(5), rs.getDouble(1), rs.getDouble(2)));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        AdatLekerdezo.kapcsolatZar();

        return jaratMegallok;
    }
         
         
         
    
//        public static List<JaratMegalloBean> jaratokMegallokLekerdezese(String jarat) {
//        List<JaratMegalloBean> jaratMegallok = new ArrayList<JaratMegalloBean>();
////        boolean trace = false;
//        AdatLekerdezo.kapcsolatNyit();
//        try {
//            Statement stmt = AdatLekerdezo.kapcsolat.createStatement();
//            String sql = "select distinct shapes.shape_id, shapes.shape_pt_lat, shapes.shape_pt_lon, shapes.shape_pt_sequence, routes.route_id "
//                    + "from SHAPES "
//                    + "join TRIPS on SHAPES.shape_id = trips.shape_id join routes on trips.route_id = routes.route_id "
//                    + "where routes.ROUTE_ID = '" + jarat + "' and trips.direction_id = 0 "
//                    + "order by shapes.shape_pt_sequence";
//              
//            String sqlCount = "select COUNT(DISTINCT stops.stop_name), COUNT(DISTINCT stops.stop_lat), COUNT(DISTINCT stops.stop_lon)"
//                    + "from routes "
//                    + "inner join TRIPS on routes.route_id = trips.route_id "
//                    + "inner join stop_times on trips.trip_id = stop_times.trip_id "
//                    + "inner join stops on stop_times.stop_id = stops.stop_id "
//                    + "where routes.ROUTE_SHORT_NAME= '" + jarat + "'";
//
//
//            ResultSet rsCount = stmt.executeQuery(sqlCount);
//            rsCount.next();
//            int rowNum = Math.max(rsCount.getInt(2), rsCount.getInt(3));
//            ResultSet rs = stmt.executeQuery(sql);
//            while (rs.next()) {
//                jaratMegallok.add(new JaratMegalloBean(rs.getString(1),rs.getDouble(3),rs.getDouble(2), rs.getInt(4)));
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        AdatLekerdezo.kapcsolatZar();
//
//        return jaratMegallok;
//    }
}
