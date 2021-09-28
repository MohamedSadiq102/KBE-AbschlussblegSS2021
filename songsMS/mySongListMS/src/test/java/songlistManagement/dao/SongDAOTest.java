package songlistManagement.dao;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.TestPropertySource;
import songlistManagement.model.Song;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SongDAOTest {

    @Autowired
    private SongDAO songDAO;


    @Test
    @Order(5)
    void saveSong_success_saved_and_return_id() {
        Song s = new Song();
        s.setTitle("Titel");
        int ret = songDAO.saveSong(s);
        assertNotEquals(ret, -1);
        assertEquals(songDAO.findAllSongs().size(), 2);
        songDAO.deleteSong(ret);
    }

    @Test
    @Order(2)
    void saveSong_null_Title_fails() {
        Song s = new Song();
        s.setTitle(null);
        int ret = songDAO.saveSong(s);
        assertEquals(-1, ret);
        assertEquals(songDAO.findAllSongs().size(), 1);
    }

    @Test
    @Order(3)
    void findAllSongs_success() {
        List<Song> songs = songDAO.findAllSongs();
        assertEquals(1, songs.size());
    }

    @Test
    @Order(9)
    void deleteSong_success() {
        Song deleteSong = new Song();
        deleteSong.setTitle("delete");
        int idToDelete = songDAO.saveSong(deleteSong);
        int ret = songDAO.deleteSong(idToDelete);
        assertEquals(0, ret);
        assertEquals(songDAO.findAllSongs().size(), 1);
    }

    @Test
    @Order(1)
    void findSong_success() {
        Song song = songDAO.findSong(1);
        assertNotNull(song);
    }

    @Test
    @Order(6)
    void findSong_not_existing_song_returns_null() {
        Song song = songDAO.findSong(20000);
        assertEquals(null, song);
    }

    @Test
    @Order(8)
    void updateSong_success() {
        Song updater = new Song();
        updater.setTitle("updated_title");
        updater.setArtist("updated_artist");
        updater.setLabel("updated_label");
        updater.setReleased(0000);
        updater.setId(1);
        int ret = songDAO.updateSong(updater);
        Song dbsong = songDAO.findSong(1);
        assertEquals(0, ret);
        assertEquals(updater.getTitle(), dbsong.getTitle());
        assertEquals(updater.getArtist(), dbsong.getArtist());
        assertEquals(updater.getReleased(), dbsong.getReleased());
        assertEquals(updater.getLabel(), dbsong.getLabel());
    }

    @Test
    @Order(7)
    void updateSong_not_existing_song_returns_negOne() {
        Song updater = new Song();
        updater.setId(1000000);
        int ret = songDAO.updateSong(updater);
        assertEquals(-1, ret);
    }
}