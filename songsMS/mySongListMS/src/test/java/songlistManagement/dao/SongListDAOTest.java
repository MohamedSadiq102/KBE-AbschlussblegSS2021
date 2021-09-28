package songlistManagement.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import songlistManagement.model.SongList;

//import java.awt.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "/test.properties")
class SongListDAOTest {

    @Autowired
    private SongListDAO songListDAO;

    @Test
    @Order(1)
    void findById_success() {
        SongList songList = songListDAO.findById(1);
        assertNotNull(songList);
    }

    @Test
    @Order(2)
    void findPublicListsOf_success() {
        List<SongList> songList = songListDAO.findPublicListsOf("mmuster");
        assertEquals(1, songList.size());
    }

    @Test
    @Order(3)
    void findPublicListsOf_doesnt_return_private_list() {
        SongList songList = new SongList();
        songList.setOwner("mmuster");
        songList.setPrivate(true);
        songList.setName("namedList");
        int ret = songListDAO.saveSongList(songList);
        assertEquals(songListDAO.findPublicListsOf("mmuster").size(), 1);
        songListDAO.deleteSongList(ret);

    }

    @Test
    @Order(5)
    void saveSongList_success() {
        SongList songList = new SongList();
        songList.setOwner("mmuster");
        songList.setPrivate(true);
        songList.setName("namedList");
        int ret = songListDAO.saveSongList(songList);
        assertNotEquals(-1, ret);
        assertNotNull(songListDAO.findById(ret));
        songListDAO.deleteSongList(ret);
    }

    @Test
    @Order(11)
    void deleteSongList_success() {
        int ret = songListDAO.deleteSongList(1);
        assertEquals(0, ret);
        assertEquals(songListDAO.findAllListsOf("mmuster").size(), 0);
    }

    @Test
    @Order(7)
    void deleteSongList_not_existing_song_returns_newOne() {
        int ret = songListDAO.deleteSongList(100000);
        assertEquals(-1, ret);
    }

    @Test
    @Order(8)
    void updateSongList_success() {
        SongList updater = new SongList();
        updater.setId(1);
        updater.setPrivate(true);
        updater.setName("newName");
        int ret = songListDAO.updateSongList(updater);
        SongList dbsongList = songListDAO.findById(1);
        assertEquals(updater.isPrivate(), dbsongList.isPrivate());
        assertEquals(updater.getName(), dbsongList.getName());
        assertEquals(0, ret);
    }

    @Test
    @Order(9)
    void updateSongList_not_existing_songList_fails() {
        SongList updater = new SongList();
        updater.setId(100000);
        int ret = songListDAO.updateSongList(updater);
        assertEquals(-1, ret);
    }

    @Test
    @Order(10)
    void findAllListsOf_success() {
        assertEquals(songListDAO.findAllListsOf("mmuster").size(), 1);
    }

    @Test
    @Order(6)
    void findAllListsOf_finds_public_and_private_lists() {
        SongList newList = new SongList();
        newList.setPrivate(true);
        newList.setName("newName");
        newList.setOwner("mmuster");
        int ret = songListDAO.saveSongList(newList);
        assertNotEquals(-1, ret);
        assertEquals(songListDAO.findAllListsOf("mmuster").size(), 2);
        songListDAO.deleteSongList(ret);
    }
}