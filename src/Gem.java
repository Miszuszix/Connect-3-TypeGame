import javax.swing.*;
import java.awt.*;

public class Gem {
    private String path;
    private final JLabel label;

    public Gem(int x, int y, String path) {
        this.path = path;
        label = new JLabel();
        label.setBounds(x, y, 64, 64);
        label.setVisible(true);
        label.setLayout(null);
        Image image = new ImageIcon(path).getImage();
        label.setIcon(new ImageIcon(image));
    }

    public void setIcon(String path){
        Image image = new ImageIcon(path).getImage();
        label.setIcon(new ImageIcon(image));
        this.path = path;
    }

    public JLabel getLabel(){
        return label;
    }

    public String getPath() {
        return path;
    }
}
