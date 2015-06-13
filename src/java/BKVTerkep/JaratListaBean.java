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
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
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
    public static final String CIRCLE_COLOR = "blue";
    public static final String CIRCLE_SELECTED_COLOR = "white";
    private List<JaratBean> jaratLista;
    private List<JaratMegalloBean> jaratMegalloLista;
    private List<JaratTipus> jaratTipusSzuro;
    private JaratMegalloBean selectedMegallo;
    private JaratMegalloCircle selectedCircle;
    private String selectedJaratNev;
    private MapModel jaratMegalloModel;
    private Integer jaratMapZoom = 11;
    private String jaratMapCenter = "47.498355, 19.04053";
    private String directionId = "0";
    private Boolean isCenteringEnabled = false;
    private Boolean isOverlayEnabled = true;

    public Boolean getIsOverlayEnabled() {
        return isOverlayEnabled;
    }

    public void setIsOverlayEnabled(Boolean isOverlayEnabled) {
        this.isOverlayEnabled = isOverlayEnabled;
        this.refreshJaratMegalloModell();
    }

    public Boolean getIsCenteringEnabled() {
        return isCenteringEnabled;
    }

    public void setIsCenteringEnabled(Boolean isCenteringEnabled) {
        this.isCenteringEnabled = isCenteringEnabled;
        this.refreshJaratMapCenter();
    }

    public String getDirectionId() {
        return directionId;
    }

    public void setDirectionId(String directionId) {
        this.directionId = directionId;
        this.refreshJaratMegalloLista();
        this.refreshJaratMegalloModell();
    }

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
        this.refreshJaratMegalloLista();
        this.refreshJaratMegalloModell();
    }

    public List<JaratTipus> getJaratTipusSzuro() {
        return this.jaratTipusSzuro;
    }

    public void setJaratTipusSzuro(List<JaratTipus> jaratTipusSzuro) {
        this.jaratTipusSzuro = jaratTipusSzuro;
        this.refreshJaratLista();
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

    public JaratMegalloBean getSelectedMegallo() {
        return selectedMegallo;
    }

    public void setSelectedMegallo(JaratMegalloBean selectedMegallo) {
        this.selectedMegallo = selectedMegallo;
    }

    private void refreshJaratLista() {
        if (this.jaratLista != null) {
            this.jaratLista.clear();
        }
        if ((this.jaratTipusSzuro != null) && (this.jaratTipusSzuro.size() > 0)) {
            this.jaratLista = AdatLekerdezo.jaratokLekerdezese(EnumSet.copyOf(this.jaratTipusSzuro));
        }
    }

    private void refreshJaratMegalloLista() {
        if (this.jaratMegalloLista != null) {
            this.jaratMegalloLista.clear();
        }
        this.selectedMegallo = null;
        this.selectedCircle = null;

        if ((this.selectedJaratNev != null) && (this.selectedJaratNev.length() > 0)) {
            this.jaratMegalloLista = AdatLekerdezo.jaratokMegallokLekerdezese(this.selectedJaratNev, this.directionId);
        }
    }

    private void refreshJaratMegalloMeret() {
        for (Circle megalloCircle : this.jaratMegalloModel.getCircles()) {
            if (megalloCircle != null) {
                megalloCircle.setRadius(this.getCircleRadius(this.selectedCircle == megalloCircle));
            }
        }
    }

    private void refreshJaratMegalloModell() {
        this.jaratMegalloModel = null;
        
        if (this.isOverlayEnabled && (this.jaratMegalloLista != null)) {
            this.jaratMegalloModel = new DefaultMapModel();

            Polyline polyline = new Polyline();
            List<LatLng> polyPaths = polyline.getPaths();

            for (JaratMegalloBean megallo : this.jaratMegalloLista) {
                if (megallo != null) {
                    boolean isSelected = megallo == this.selectedMegallo;
                    
                    JaratMegalloCircle circle = JaratListaBean.circleFactory(
                            megallo,
                            new LatLng(megallo.getSzelesseg(), megallo.getHosszusag()),
                            CIRCLE_COLOR,
                            isSelected ? CIRCLE_SELECTED_COLOR : CIRCLE_COLOR,
                            0.8,
                            0.5,
                            this.getCircleRadius(isSelected));

                    this.jaratMegalloModel.addOverlay(circle);

                    if (isSelected)
                    {
                        this.selectedCircle = circle;
                    }
//                polyPaths.add(new LatLng(megallo.getSzelesseg(), megallo.getHosszusag()));
                }
            }

//            polyline.setStrokeWeight(3);
//            polyline.setStrokeColor("blue");
//            polyline.setStrokeOpacity(0.7);
//
//            this.jaratMegalloModel.addOverlay(polyline);
        }
    }

    private static JaratMegalloCircle circleFactory(JaratMegalloBean megallo, LatLng coord, String strokeColor, String fillColor, double strokeOpacity, double fillOpacity, double radius) {
        JaratMegalloCircle returnCircle = new JaratMegalloCircle(coord, radius, megallo);
        returnCircle.setFillColor(fillColor);
        returnCircle.setFillOpacity(fillOpacity);
        returnCircle.setStrokeColor(strokeColor);
        returnCircle.setStrokeOpacity(strokeOpacity);
        returnCircle.setStrokeWeight(1);
        return returnCircle;
    }

    private double getCircleRadius(boolean isSelected) {
        double circleRadius = (MAX_ZOOM_PRODUCT / Math.pow(2, this.jaratMapZoom)) + this.jaratMapZoom;
        return isSelected ? 1.7 * circleRadius : circleRadius;
    }

    public void onMapStateChange(StateChangeEvent event) {
        if (event != null) {
            this.jaratMapZoom = event.getZoomLevel();
            this.jaratMapCenter = event.getCenter().getLat() + ", " + event.getCenter().getLng();
            this.refreshJaratMegalloMeret();
        }
    }

    public void onOverlaySelect(OverlaySelectEvent event) {
        if (event != null) {
            this.setSelectedOrUnselectedCircleStyle((JaratMegalloCircle) event.getOverlay());
        }
    }

    private void setSelectedOrUnselectedCircleStyle(JaratMegalloCircle currentSelectedCircle) {
        if (currentSelectedCircle == null) {
            return;
        }

        if (currentSelectedCircle == this.selectedCircle) {
            // Unselect
            this.selectedMegallo = null;
            this.selectedCircle.setRadius(this.getCircleRadius(false));
            this.selectedCircle = null;
            currentSelectedCircle.setFillColor(CIRCLE_COLOR);

        } else {
            // Select
            this.selectedMegallo = currentSelectedCircle.getDisplayedMegallo();

            // Reset previous selection
            if (this.selectedCircle != null) {
                this.selectedCircle.setRadius(this.getCircleRadius(false));
                this.selectedCircle.setFillColor(CIRCLE_COLOR);
            }

            // Set current selection
            this.selectedCircle = currentSelectedCircle;
            this.selectedCircle.setFillColor(CIRCLE_SELECTED_COLOR);
            this.selectedCircle.setRadius(this.getCircleRadius(true));
            this.refreshJaratMapCenter();
        }
    }

    private void refreshJaratMapCenter() {
        if (this.isCenteringEnabled && (this.selectedMegallo != null)) {
            this.jaratMapCenter = this.selectedMegallo.getCoordinates();
        }
    }

    public void onRowSelect(SelectEvent event) {
        JaratMegalloBean megallo = (JaratMegalloBean) event.getObject();
        if (megallo != null) {
            JaratMegalloCircle circleToSelect = this.FindJaratMegalloCircle(megallo);
            if (circleToSelect != null) {
                this.setSelectedOrUnselectedCircleStyle(circleToSelect);
            }
        }
    }

    private JaratMegalloCircle FindJaratMegalloCircle(JaratMegalloBean megallo) {
        JaratMegalloCircle returnValue = null;
        if (this.jaratMegalloModel != null) {
            for (Circle circle : this.jaratMegalloModel.getCircles()) {
                JaratMegalloCircle megalloCircle = (JaratMegalloCircle) circle;
                if ((megalloCircle != null) && (megalloCircle.getDisplayedMegallo() == megallo)) {
                    returnValue = megalloCircle;
                    break;
                }
            }
        }
        return returnValue;
    }

    /**
     * Creates a new instance of JaratListaBean
     */
    public JaratListaBean() {
        this.refreshJaratLista();
    }

    public static String removeDoubleQuotes(String input) {
        return input.replaceAll("^\"|\"$", "");
    }
}
