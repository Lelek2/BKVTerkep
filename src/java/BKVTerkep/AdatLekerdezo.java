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
                whereSql += "routes.route_type = '" + jt.ordinal() + "' or ";
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
            //obj = new Object[rsCount.getInt(1)][1];
            //int i=0;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                jaratLista.add(new JaratBean(rs.getString("ROUTE_SHORT_NAME")));
            }

            /*while(rs.next()){
             Object[] adatok = new Object[2];
             adatok[0] = rs.getObject("ROUTE_SHORT_NAME");
             obj[i][0] = rs.getObject(1);
             i++;
             table.addRow(adatok);
             }
             if (trace){
             System.out.println(sql);
             System.out.println("ROW num: " + rsCount.getString(1));
             }*/
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

    public static List<JaratMegalloBean> jaratokMegallokLekerdezese(String jarat) {
        List<JaratMegalloBean> jaratMegallok = new ArrayList<JaratMegalloBean>();

//        boolean trace = false;
        AdatLekerdezo.kapcsolatNyit();
//        String[][] obj = new String[1][1];
        try {
            Statement stmt = AdatLekerdezo.kapcsolat.createStatement();
            String sql = "select distinct stops.stop_lat,  stops.stop_lon, stops.stop_name"
                    + "from routes "
                    + "RIGHT join TRIPS on routes.route_id = trips.route_id "
                    + "RIGHT join stop_times on trips.trip_id = stop_times.trip_id "
                    + "RIGHT join stops on stop_times.stop_id = stops.stop_id "
                    + "where routes.ROUTE_SHORT_NAME= '" + jarat + "'";
            String sqlCount = "select COUNT(DISTINCT stops.stop_name), COUNT(DISTINCT stops.stop_lat), COUNT(DISTINCT stops.stop_lon)"
                    + "from routes "
                    + "inner join TRIPS on routes.route_id = trips.route_id "
                    + "inner join stop_times on trips.trip_id = stop_times.trip_id "
                    + "inner join stops on stop_times.stop_id = stops.stop_id "
                    + "where routes.ROUTE_SHORT_NAME= '" + jarat + "'";


            ResultSet rsCount = stmt.executeQuery(sqlCount);
            rsCount.next();
            int rowNum = Math.max(rsCount.getInt(2), rsCount.getInt(3));
//            obj = new String[rowNum][3];
//            int i = 0;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                jaratMegallok.add(new JaratMegalloBean(rs.getString(3),rs.getDouble(2),rs.getDouble(1)));
            }
//			while(rs.next()){
//				obj[i][0] = rs.getString(1);
//				obj[i][1] = rs.getString(2);
//				obj[i][2] = rs.getString(3);
//				i++;
//			}
//			if (trace){
//				System.out.println(sql);
//				System.out.println(sqlCount);
//				System.out.println("ROW num: " + rowNum);
//			}

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        AdatLekerdezo.kapcsolatZar();

        return jaratMegallok;
    }
}
