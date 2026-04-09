package Client.UIComponents;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class InputField extends JTextField{
    String name;

    public InputField(String name){
        this.name = name;
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        setBorder(border);
    }
}
