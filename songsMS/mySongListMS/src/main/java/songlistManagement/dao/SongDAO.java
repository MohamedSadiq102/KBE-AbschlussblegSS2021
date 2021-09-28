package songlistManagement.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import songlistManagement.model.Song;

import javax.persistence.*;
import java.util.List;


@Repository
public class SongDAO implements ISongDAO {

    @PersistenceContext
    private EntityManager em;

    public SongDAO(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public int saveSong(Song song) {
        if (song.getTitle() == null) {
            return -1;
        }
        try {
            em.persist(song);
            return song.getId();
        } catch (Exception e) {
            e.printStackTrace();
            if (em != null) {
                em.close();
            }
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Transactional
    public List<Song> findAllSongs() {
        try {
            Query q = em.createQuery("SELECT s FROM Song s", Song.class);
            List<Song> res = q.getResultList();
            return res;
        } catch (Exception p) {
            p.printStackTrace();
            if (em != null) {
                em.close();
            }
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Transactional
    public int deleteSong(int id) {
        try {
            Song song = em.find(Song.class, id);
            if (song != null) {
                em.remove(song);
                return 0;
            }
        } catch (Exception p) {
            p.printStackTrace();
            if (em != null) {
                em.close();
            }
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return -1;
    }

    @Transactional
    public Song findSong(int id) {
        try {
            Song song = em.find(Song.class, id);
            return song;
        } catch (PersistenceException | IllegalArgumentException p) {
            p.printStackTrace();
            if (em != null) {
                em.close();
            }
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Transactional
    public int updateSong(Song updater) {
        try {
            Song dbsong = em.find(Song.class, updater.getId());
            int ret = 0;
            if (dbsong != null) {
                dbsong.setTitle(updater.getTitle());
                dbsong.setArtist(updater.getArtist());
                dbsong.setReleased(updater.getReleased());
                dbsong.setLabel(updater.getLabel());
            } else {
                ret = -1;
            }
            if (em != null) {
                em.close();
            }
            return ret;
        } catch (Exception p) {
            p.printStackTrace();
            if (em != null) {
                em.close();
            }
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
