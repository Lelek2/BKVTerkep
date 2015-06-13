/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BKVTerkep;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.LatLng;

/**
 *
 * @author gazdi
 */
public class JaratMegalloCircle extends Circle {
    
    private JaratMegalloBean displayedMegallo;

    public JaratMegalloBean getDisplayedMegallo() {
        return displayedMegallo;
    }

    public JaratMegalloCircle(LatLng center, double radius, JaratMegalloBean megallo) {
        super(center, radius);
        this.displayedMegallo = megallo;
    }
    
}
