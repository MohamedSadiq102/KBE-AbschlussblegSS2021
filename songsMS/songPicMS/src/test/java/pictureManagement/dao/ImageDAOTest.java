package pictureManagement.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ImageDAOTest {

    private PictureDAO imageDAO;

    @BeforeEach
    void setUp() {
        String path = "src/test/resources/";
        File file = new File(path);
        imageDAO = new PictureDAO(file.getAbsolutePath() + "/");
    }

    @Test
    void loadImage_success() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage expected = null;
        byte[] expectedbytes = null;
        byte[] loadedbytes = null;
        try {
            expected = ImageIO.read(getClass().getResource("/1.jpeg"));
            ImageIO.write(expected, "jpeg", baos);
            expectedbytes = baos.toByteArray();
            baos.flush();
            baos.reset();
            ImageIO.write(imageDAO.loadImage("1.jpeg"), "jpeg", baos);
            loadedbytes = baos.toByteArray();
        } catch (IOException io) {
            io.printStackTrace();
        }
        assertArrayEquals(expectedbytes, loadedbytes);
    }

    @Test
    void loadImage_unknown_image_returns_null() {
        assertNull(imageDAO.loadImage("2.jpeg"));
    }
}