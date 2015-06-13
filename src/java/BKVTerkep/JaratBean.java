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
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
    
    /**
     * Creates a new instance of JaratBean
     */
    public JaratBean(String nev) {
        this.nev = nev;
    }
    
     /**
     * Creates a new instance of JaratBean
     */
    public JaratBean(String id, String nev) {
        this.id = id;
        this.nev = nev;
    }
    
    public static String GetJaratNev(String shortName, String dsc)
    {
        return "<div style='border-bottom:1px solid gray;padding-bottom:2px'><span style='font-weight:bold;'>" + shortName + "</span><br/>" + JaratListaBean.removeDoubleQuotes(dsc) + "</div>";
    }
}
