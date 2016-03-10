/**
 * Class for the worker thread that loads images to memory.
 * 
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */

package gui;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

import java.awt.Image;
import java.util.concurrent.ExecutionException;

public class LoadImage extends SwingWorker<Image, String> {

    Image image;
    String filename;

    public LoadImage(String filename) {
        this.filename = filename;
    }

    @Override
    protected Image doInBackground() throws Exception {

        Image image = new ImageIcon(this.getClass().getResource(filename)).getImage();
        return image;
    }

    @Override
    protected void done() {
        //nothing in particular

    }
}
