package songlistManagement.controller;

import songlistManagement.dao.ISongListDAO;
import songlistManagement.model.SongList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/songLists")
public class SongsListController {

    private final ISongListDAO songListDAO;

    public SongsListController(ISongListDAO songListDAO) {
        this.songListDAO = songListDAO;
    }

    @GetMapping(value = "/{id}", headers = "Accept=application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SongList> getSongListJsonWithPathVariable(@PathVariable(value = "id") Integer id, @RequestHeader("userId-Header") String userid) {
        SongList songList = songListDAO.findById(id);

        if (songList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (songList.getOwner().equals(userid) || !songList.isPrivate()) {
            return new ResponseEntity<SongList>(songList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(value = "/{id}", headers = "Accept=application/xml", produces = {MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SongList> getSongListXmlWithPathVariable(@PathVariable(value = "id") Integer id, @RequestHeader("userId-Header") String userid) {

        SongList songList = songListDAO.findById(id);

        if (songList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (songList.getOwner().equals(userid) || !songList.isPrivate()) {
            return new ResponseEntity<SongList>(songList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @GetMapping(headers = "Accept=application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<SongList>> getSongListofUserJson(@RequestParam(value = "userId") String u, @RequestHeader("userId-Header") String userid) {
        if (userid == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (userid.equals(u)) {
            List<SongList> all = songListDAO.findAllListsOf(userid);
            return new ResponseEntity<>(all, HttpStatus.OK);
        } else {
            List<SongList> allpublic = songListDAO.findPublicListsOf(u);
            return new ResponseEntity<>(allpublic, HttpStatus.OK);
        }
    }


    @GetMapping(headers = "Accept=application/xml", produces = {MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<SongList>> getSongListoFUserXml(@RequestParam(value = "userId") String u, @RequestHeader("userId-Header") String userid) {
        if (userid == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (userid.equals(u)) {
            List<SongList> all = songListDAO.findAllListsOf(userid);
            return new ResponseEntity<>(all, HttpStatus.OK);
        } else {
            List<SongList> allpublic = songListDAO.findPublicListsOf(u);
            return new ResponseEntity<>(allpublic, HttpStatus.OK);
        }
    }


    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> postSong(@RequestBody SongList newSongList, @RequestHeader("userId-Header") String userid) {
        if (newSongList.getName() == null || newSongList.getName().equals("") || newSongList.getName().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        newSongList.setOwner(userid);
        int id = songListDAO.saveSongList(newSongList);

        if (id != -1) {
            HttpHeaders header = new HttpHeaders();
            header.set("Location", "/songLists/" + id);
            return ResponseEntity.ok().headers(header).body("");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteSongList(@PathVariable Integer id, @RequestHeader("userId-Header") String userid) {
        int ret = -1;
        SongList l = songListDAO.findById(id);
        if (l != null && l.getOwner().equals(userid)) {
            ret = songListDAO.deleteSongList(id);
        }
        if (ret == 0) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity updateSongList(@RequestBody SongList updater, @PathVariable Integer id, @RequestHeader("userId-Header") String userid) {
        int ret = -1;
        SongList l = songListDAO.findById(id);
        if (l != null && l.getOwner().equals(userid) && id == updater.getId()) {
            ret = songListDAO.updateSongList(updater);
        }
        if (ret == 0) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
