package songlistManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import songlistManagement.dao.ISongDAO;
import songlistManagement.feignClientManagement.ImageClient;
import songlistManagement.model.Song;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping(value = "/songs")
public class SongController {

    @Autowired
    private ImageClient imageClient;

    private final ISongDAO songdao;

    public SongController(ISongDAO songdao) {
        this.songdao = songdao;
    }

    @GetMapping(value = "/{id}", headers = "Accept=application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Song> getSongJSON(@PathVariable(value = "id") Integer id) {
        Song song = songdao.findSong(id);
        byte[] cover = imageClient.getImageofSong(id);
        if (song != null) {
            return new ResponseEntity<Song>(song, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Song> getSongXML(@PathVariable(value = "id") Integer id) {
        Song song = songdao.findSong(id);
        if (song != null) {
            return new ResponseEntity<Song>(song, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(headers = "Accept=application/xml", produces = {MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Song>> getAllSongsXML() {
        List<Song> songs = songdao.findAllSongs();
        if (songs != null) {
            return new ResponseEntity<>(songs, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(headers = "Accept=application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Song>> getAllSongsJSON() {
        List<Song> songs = songdao.findAllSongs();
        if (songs != null) {
            return new ResponseEntity<>(songs, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> postSong(@RequestBody Song newsong) {
        if (newsong.getTitle() == null || newsong.getTitle().equals("") || newsong.getTitle().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        int id = -1;
        try {
            id = songdao.saveSong(newsong);
        } catch (UnexpectedRollbackException ure) {
            ure.printStackTrace();
        }
        if (id != -1) {
            HttpHeaders header = new HttpHeaders();
            header.set("Location", "/songs/" + id);
            return ResponseEntity.ok().headers(header).body("");
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateSong(@RequestBody Song updater, @PathVariable Integer id) {
        if (id != updater.getId() || updater.getTitle() == null || updater.getTitle().equals("") || updater.getTitle().trim().isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        } else {
            int ret = songdao.updateSong(updater);
            if (ret == 0) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteSong(@PathVariable Integer id) {
        int ret = songdao.deleteSong(id);
        if (ret == 0) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/cover/{id}")
    public ResponseEntity getCover(@PathVariable int id) {
        byte[] image = imageClient.getImageofSong(id);
        if (image != null) {
            return new ResponseEntity(image, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
