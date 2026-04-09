package Client.UIComponents;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class InputField extends JTextField{

    public InputField(){
        Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        setBorder(border);
    }
}
