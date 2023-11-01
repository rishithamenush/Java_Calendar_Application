package edu.curtin.terminalgriddemo;


import edu.curtin.terminalgriddemo.Event;
import javax.swing.JOptionPane;

public class NotifyPlugin {
    private String text;

    public NotifyPlugin(String text) {
        this.text = text;
    }

    // This method checks for matching events and notifies the user.
    public void notifyForMatchingEvent(Event event) {
        if (event.getTitle().contains(text)) {
            // Console output
            System.out.println("Event Match Found: " + event.toString());

            // GUI output using Swing
            JOptionPane.showMessageDialog(null, "Event Match Found: " + event.toString());
        }
    }
}
