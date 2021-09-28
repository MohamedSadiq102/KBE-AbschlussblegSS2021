package songlistManagement.dao;

import org.springframework.stereotype.Component;
import songlistManagement.model.Song;

import java.util.List;


public interface ISongDAO {

    public int saveSong(Song song);

    public List<Song> findAllSongs();

    public int deleteSong(int id);

    public Song findSong(int id);

    public int updateSong(Song updater);
}
