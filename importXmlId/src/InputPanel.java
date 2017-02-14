import javax.swing.*;

/**
 * Created by houlin.jiang on 17-2-14.
 */
public class InputPanel {
    private JPanel panel1;
    private JTextField textField1;


    public JPanel getComponent() {
        return panel1;
    }

    public String getTextPixel() {
        return textField1.getText();
    }
}
