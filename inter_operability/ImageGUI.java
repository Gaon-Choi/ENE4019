
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageGUI {
    public void imshow(String url) {
        BufferedImage img = null;

        try {
            URL url_ = new URL(url);
            img = ImageIO.read(url_);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        JFrame frame_ = new JFrame();
        JLabel label_ = new JLabel(new ImageIcon(img));

        frame_.add(label_);
        frame_.setSize(500, 500);
        frame_.setBounds(100, 100, 700, 500);
        frame_.setVisible(true);
    }
}
