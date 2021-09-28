package pictureManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pictureManagement.dao.pictureDAOInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/image")
public class PictureController implements pictureControllerInterface {

    @Autowired
    private pictureDAOInterface imageDAO;

    public PictureController(pictureDAOInterface imageDAO) {
        this.imageDAO = imageDAO;
    }

    @Override
    public byte[] getImageofSong(int id) {
        BufferedImage image = imageDAO.loadImage("" + id + ".jpeg");
        if (image != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", baos);
                return baos.toByteArray();
            } catch (IOException io) {
                io.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
