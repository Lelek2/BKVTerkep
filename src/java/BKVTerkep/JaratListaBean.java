/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BKVTerkep;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polyline;

/**
 *
 * @author gazdi
 */
@ManagedBean(name = "JaratListaBean", eager = true)
@SessionScoped
public class JaratListaBean implements Serializable {

    
    /**
     * 2 a 19-en
     */
    public static final int MAX_ZOOM_PRODUCT = 524288;
    private List<JaratBean> jaratLista;
    private List<JaratMegalloBean> jaratMegalloLista;
    private List<JaratTipus> jaratTipusSzuro;
    private String selectedJaratNev;
    private MapModel jaratMegalloModel;
    private Integer jaratMapZoom = 12;
    private String jaratMapCenter = "47.498355, 19.04053";

    public Integer getJaratMapZoom() {
        return this.jaratMapZoom;
    }

    public void setJaratMapZoom(Integer jaratMapZoom) {
        this.jaratMapZoom = jaratMapZoom;
    }

    public String getJaratMapCenter() {
        return this.jaratMapCenter;
    }

    public void setJaratMapCenter(String jaratMapCenter) {
        this.jaratMapCenter = jaratMapCenter;
    }

    public MapModel getJaratMegalloModel() {
        return this.jaratMegalloModel;
    }

    public String getSelectedJaratNev() {
        return this.selectedJaratNev;
    }

    public void setSelectedJaratNev(String selectedJaratNev) {
        this.selectedJaratNev = selectedJaratNev;
        this.JaratMegalloListaFrissitese();
        this.JaratMegalloModellFrissitese();
    }

    public List<JaratTipus> getJaratTipusSzuro() {
        return this.jaratTipusSzuro;
    }

    public void setJaratTipusSzuro(List<JaratTipus> jaratTipusSzuro) {
        this.jaratTipusSzuro = jaratTipusSzuro;
        this.JaratListaFrissitese();
    }

    public List<JaratTipus> getOsszesJaratTipus() {
        return Arrays.asList(JaratTipus.values());
    }

    public List<JaratBean> getJaratLista() {
        return this.jaratLista;
    }

    public List<JaratMegalloBean> getJaratMegalloLista() {
        return jaratMegalloLista;
    }

    public void JaratListaFrissitese() {
        if (this.jaratLista != null) {
            this.jaratLista.clear();
        }
        if ((this.jaratTipusSzuro != null) && (this.jaratTipusSzuro.size() > 0)) {
            this.jaratLista = AdatLekerdezo.jaratokLekerdezese(EnumSet.copyOf(this.jaratTipusSzuro));
        }
    }

    public void JaratMegalloListaFrissitese() {
        if (this.jaratMegalloLista != null) {
            this.jaratMegalloLista.clear();
        }
        if ((this.selectedJaratNev != null) && (this.selectedJaratNev.length() > 0)) {
            this.jaratMegalloLista = AdatLekerdezo.jaratokMegallokLekerdezese(this.selectedJaratNev);
        }
    }

    public void JaratMegalloModellFrissitese() {
        if (this.jaratMegalloLista != null) {
            this.jaratMegalloModel = new DefaultMapModel();

            Polyline polyline = new Polyline();
            List<LatLng> polyPaths = polyline.getPaths();
            
            for (JaratMegalloBean megallo : this.jaratMegalloLista) {
//                this.jaratMegalloModel.addOverlay(
//                        JaratListaBean.CircleFactory(
//                        new LatLng(megallo.getSzelesseg(), megallo.getHosszusag()),
//                        "blue",
//                        "blue",
//                        0.7,
//                        0.5,
//                        this.GetCircleRadius()));
                
                polyPaths.add(new LatLng(megallo.getSzelesseg(), megallo.getHosszusag()));
            }

            polyline.setStrokeWeight(3);
            polyline.setStrokeColor("blue");
            polyline.setStrokeOpacity(0.7);

            this.jaratMegalloModel.addOverlay(polyline);
        }
    }

    public static Circle CircleFactory(LatLng coord, String strokeColor, String fillColor, double strokeOpacity, double fillOpacity, double radius) {
        Circle returnCircle = new Circle(coord, radius);
        returnCircle.setStrokeColor(strokeColor);
        returnCircle.setFillColor(fillColor);
        returnCircle.setStrokeOpacity(strokeOpacity);
        returnCircle.setFillOpacity(fillOpacity);
        return returnCircle;
    }
    
    public double GetCircleRadius()
    {
        return (MAX_ZOOM_PRODUCT / Math.pow(2, this.jaratMapZoom)) + (this.jaratMapZoom / 3);
    }

    public void onMapStateChange(StateChangeEvent event) {
        if (event != null) {
            this.jaratMapZoom = event.getZoomLevel();
            System.out.println(this.jaratMapZoom);
            this.jaratMapCenter = event.getCenter().getLat() + ", " + event.getCenter().getLng();
            this.JaratMegalloModellFrissitese();
        }
    }

    /**
     * Creates a new instance of JaratListaBean
     */
    public JaratListaBean() {
        this.JaratListaFrissitese();
//        this.jaratLista = AdatLekerdezo.getBKVJaratLista();
    }
    
    public static String RemoveDoubleQuotes(String input){
        return input.replaceAll("^\"|\"$", "");
    }
}
