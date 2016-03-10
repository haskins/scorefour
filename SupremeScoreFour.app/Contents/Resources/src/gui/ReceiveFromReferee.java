/**
 * Class for the worker thread that loads images to memory
 * 
 * @author Devon Harker, Josh Haskins, Rafael Roman Otero, Vincent Tennant, Thanh Minh Vo
 * @version 03-15-2012
 */

package gui;

import javax.swing.SwingWorker;
import java.util.concurrent.LinkedBlockingQueue;

public class ReceiveFromReferee extends SwingWorker<String, Void> {

    private LinkedBlockingQueue<String> blockQueue;

    public ReceiveFromReferee(LinkedBlockingQueue<String> blockQueue) {
        this.blockQueue = blockQueue;
    }

    @Override
    protected String doInBackground() throws Exception {
        //block until msg is in queue
        return blockQueue.take();
    }

    @Override
    protected void done() {
        //nothing in particular
    }
}
