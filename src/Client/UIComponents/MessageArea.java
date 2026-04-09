package Client.UIComponents;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MessageArea extends JScrollPane {
    public MessageArea(){
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        setBorder(border);
        JTextArea messageTextArea = new JTextArea(20,70);

        setViewportView(messageTextArea);
        messageTextArea.setFocusable(false);
        messageTextArea.setLineWrap(true);
        messageTextArea.setEditable(false);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }
}
