package Client.UIComponents;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UsersArea extends JTextArea {

    public UsersArea(){
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        setRows(20);
        setColumns(20);
        setBorder(border);
        setFocusable(false);
    }
}
