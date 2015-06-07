/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BKVTerkep;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author gazdi
 */
@FacesConverter(value="jaratTipusConverter")
public class JaratTipusConverter extends EnumConverter {
    public JaratTipusConverter() {
        super(JaratTipus.class);
    }
}
