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
public class JaratBean implements Serializable{

    private JaratTipus tipus;
    private String nev;

    public JaratTipus getTipus() {
        return tipus;
    }

    public void setTipus(JaratTipus tipus) {
        this.tipus = tipus;
    }

    public String getNev() {
        return nev;
    }
    
    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setNev(String nev, JaratTipus tipus) {
        this.nev = nev;
        this.tipus = tipus;
    }

//    /**
//     * Creates a new instance of JaratBean
//     */
//    public JaratBean() {
//    }
    
    /**
     * Creates a new instance of JaratBean
     */
    public JaratBean(String nev) {
        this.nev = nev;
    }
}
