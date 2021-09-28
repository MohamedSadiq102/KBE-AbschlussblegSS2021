package songlistManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import songlistManagement.dao.ISongDAO;
import songlistManagement.model.Song;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/test.properties")
class SongControllerTest {

    @Autowired
    private MappingJackson2XmlHttpMessageConverter xmlConverter;


    private ISongDAO mockedSongDAO;
    private Song song;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        song = new Song();
        song.setTitle("Titel");
        song.setArtist("Artist");
        song.setLabel("Label");
        song.setReleased(2020);
        song.setId(1);
        mockedSongDAO = Mockito.mock(ISongDAO.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new SongController(mockedSongDAO)).build();
    }

    @Test
    void getSongJSON_success() {
        when(mockedSongDAO.findSong(anyInt())).thenReturn(song);
        try {
            mockMvc.perform(get("/songs/1").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(song.getId()))
                    .andExpect(jsonPath("$.title").value(song.getTitle()))
                    .andExpect(jsonPath("$.label").value(song.getLabel()))
                    .andExpect(jsonPath("$.artist").value(song.getArtist()))
                    .andExpect(jsonPath("$.released").value(song.getReleased()));
            verify(mockedSongDAO).findSong(anyInt());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getSongXML_success() {
        when(mockedSongDAO.findSong(anyInt())).thenReturn(song);
        try {
            ObjectMapper xmlmapper = xmlConverter.getObjectMapper();
            mockMvc.perform(get("/songs/1").accept(MediaType.APPLICATION_XML))
                    .andExpect(status().isOk())
                    .andExpect(content().string(xmlmapper.writeValueAsString(song)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllSongsXML_success() {
        List<Song> songs = List.of(song);
        when(mockedSongDAO.findAllSongs()).thenReturn(songs);
        try {
            ObjectMapper xmlmapper = xmlConverter.getObjectMapper();
            String expectedraw = xmlmapper.writeValueAsString(songs);
            String expected = expectedraw.replaceAll("12", "");
            mockMvc.perform(get("/songs").accept(MediaType.APPLICATION_XML))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expected));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllSongsJSON_success() {
        List<Song> songs = List.of(song);
        try {
            ObjectMapper mapper = new ObjectMapper();
            when(mockedSongDAO.findAllSongs()).thenReturn(songs);
            mockMvc.perform(get("/songs").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(mapper.writeValueAsString(songs)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void postSong_success() {
        when(mockedSongDAO.saveSong(any())).thenReturn(1);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mockMvc.perform(post("/songs").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(song)))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Location", "/songs/1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateSong_success() {
        when(mockedSongDAO.updateSong(any())).thenReturn(0);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mockMvc.perform(put("/songs/1").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(song)))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateSong_different_id_in_path_and_payload_not_accepted() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mockMvc.perform(put("/songs/2").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(song)))
                    .andExpect(status().isNotAcceptable());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteSong_success() {
        when(mockedSongDAO.deleteSong(anyInt())).thenReturn(0);
        try {
            mockMvc.perform(delete("/songs/1"))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}