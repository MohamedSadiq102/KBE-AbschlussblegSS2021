package songlistManagement.dao;


import org.springframework.stereotype.Component;
import songlistManagement.model.SongList;

import java.util.List;

@Component
public interface ISongListDAO {

    public SongList findById(int id);

    public List<SongList> findPublicListsOf(String user);

    public int saveSongList(SongList songList);

    public int deleteSongList(int id);

    public int updateSongList(SongList updater);

    public List<SongList> findAllListsOf(String user);
}
