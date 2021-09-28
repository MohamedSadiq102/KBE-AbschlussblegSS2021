package pictureManagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pictureManagement.dao.pictureDAOInterface;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ImageControllerTest {

    private ImageController imageController;
    private pictureDAOInterface mockedDAO = mock(pictureDAOInterface.class);
    private MockMvc mockMvc;

    @BeforeEach
    void initialise() {
        imageController = new ImageController(mockedDAO);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    void getImageofSong_success() {
        BufferedImage image = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            image = ImageIO.read(getClass().getResource("/1.jpeg"));
            ImageIO.write(image, "jpeg", baos);
            when(mockedDAO.loadImage(anyString())).thenReturn(image);
            mockMvc.perform(get("/image/1")).andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_JPEG)).andExpect(content().bytes(baos.toByteArray()));
        } catch (Exception io) {
            io.printStackTrace();
        }
    }

    @Test
    void getImageofSong_returns_empty_byte() {
        when(mockedDAO.loadImage(anyString())).thenReturn(null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            mockMvc.perform(get("/image/2")).andExpect(content().bytes(new byte[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}