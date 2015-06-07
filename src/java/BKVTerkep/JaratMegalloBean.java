/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BKVTerkep;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author gazdi
 */
@ManagedBean
@RequestScoped
public class JaratMegalloBean{

    private String nev;
    /**
     * longitude
     */
    private double hosszusag;
    /**
     * latitude
     */
    private double szelesseg;

    public String getNev() {
        return this.nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
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
        this.nev = nev;
        this.hosszusag = hosszusag;
        this.szelesseg = szelesseg;
    }
}
