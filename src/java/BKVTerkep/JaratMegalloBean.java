/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BKVTerkep;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author gazdi
 */
@ManagedBean
@SessionScoped
public class JaratMegalloBean implements Serializable{

    private String nev;
    /**
     * longitude
     */
    private double hosszusag;
    /**
     * latitude
     */
    private double szelesseg;
    
    private Integer sorSzam;

    public Integer getSorSzam() {
        return sorSzam;
    }

    public void setSorSzam(Integer sorSzam) {
        this.sorSzam = sorSzam;
    }

    public String getNev() {
        return this.nev;
    }

    public final void setNev(String nev) {
        this.nev = JaratListaBean.removeDoubleQuotes(nev);
    }

    public double getHosszusag() {
        return this.hosszusag;
    }

    public void setHosszusag(double hosszusag) {
        this.hosszusag = hosszusag;
    }

    public double getSzelesseg() {
        return this.szelesseg;
    }

    public void setSzelesseg(double szelesseg) {
        this.szelesseg = szelesseg;
    }
    
    public String getCoordinates()
    {
        return this.getSzelesseg() + ", " + this.getHosszusag();
    }

    /**
     * Creates a new instance of JaratMegalloBean
     */
    public JaratMegalloBean(String nev) {
        this.nev = nev;
    }
    
    /**
     * Creates a new instance of JaratMegalloBean
     */
    public JaratMegalloBean(String nev, double hosszusag, double szelesseg) {
        this.setNev(nev);
        this.hosszusag = hosszusag;
        this.szelesseg = szelesseg;
    }

    /**
     * Creates a new instance of JaratMegalloBean
     */
    public JaratMegalloBean(String nev, int sorSzam, double szelesseg, double hosszusag) {
        this.setNev(nev);
        this.sorSzam = sorSzam;
        this.szelesseg = szelesseg;
        this.hosszusag = hosszusag;
    }
}
