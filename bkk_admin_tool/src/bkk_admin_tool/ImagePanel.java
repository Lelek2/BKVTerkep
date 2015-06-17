/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bkk_admin_tool;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Robi
 */
public class ImagePanel extends JScrollPane {

    private BufferedImage image;
    private int x, y;

    public ImagePanel(String path, int x, int y) {
        try {
            this.x = x;
            this.y = y;
            image = ImageIO.read(new File(path));
        } catch (IOException ex) {
            System.out.println("Nem találtam a képet");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.drawImage(image, 0, 0, x, y, this);
        g.drawImage(image, 0, 0, null);           
    }
}
