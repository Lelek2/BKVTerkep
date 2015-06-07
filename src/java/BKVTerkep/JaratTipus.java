/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BKVTerkep;

/**
 *
 * @author gazdi
 */
public enum JaratTipus {
    Villamos(0),
    Metro(1),
    Hev(2),
    Busz(3),
    Hajo(4);
    private final int val;
    private JaratTipus(int v) { val = v; }
    public int getVal() { return val; }
}
