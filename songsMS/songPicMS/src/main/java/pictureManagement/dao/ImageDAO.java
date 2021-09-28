package pictureManagement.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Repository
public class ImageDAO implements pictureDAOInterface {

    private final String path;

    public ImageDAO(@Value("/Users/mohamedsadiqfiware.org/Workspace/Abschlussbeleg/FinalBeleg/songsMS/songPicMS/src/main/resources/")String path) {
        this.path = path;
    }


    @Override
    public BufferedImage loadImage(String name) {
        try {
            BufferedImage image = ImageIO.read(new File(path + name));
            return image;
        }catch(IOException io){
            io.printStackTrace();
        }
        return null;
    }

    public String getPath() {
        return path;
    }
}
