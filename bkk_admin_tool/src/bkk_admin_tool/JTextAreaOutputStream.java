/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bkk_admin_tool;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Robi
 */
public class JTextAreaOutputStream extends OutputStream {

    private final JTextArea destination;

    public JTextAreaOutputStream(JTextArea destination) {
        if (destination == null) {
            throw new IllegalArgumentException("Destination is null");
        }

        this.destination = destination;
    }

    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException {
        final String text = new String(buffer, offset, length);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                destination.append(text);
            }
        });
    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[]{(byte) b}, 0, 1);
    }

}
