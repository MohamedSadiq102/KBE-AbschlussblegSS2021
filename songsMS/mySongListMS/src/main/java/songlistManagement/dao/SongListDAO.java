package songlistManagement.dao;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import songlistManagement.model.SongList;

import javax.persistence.*;
import java.util.List;

@Repository
public class SongListDAO implements ISongListDAO {

    @PersistenceContext
    private EntityManager em;

    public SongListDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public SongList findById(int id) {
        try {
            SongList songList = em.find(SongList.class, id);
            if (songList != null) {
                songList.getSongList().size();
            }
            return songList;
        } catch (PersistenceException | IllegalArgumentException p) {
            p.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    @Override
    @Transactional
    public List<SongList> findPublicListsOf(String user) {
        try {
            Query q = em.createQuery("select s from SongList s WHERE s.owner = :user AND s.isPrivate = false");
            q.setParameter("user", user);
            List<SongList> psl = q.getResultList();
            for (int i = 0; i < psl.size(); i++) {
                psl.get(i).getSongList().size();
            }
            return psl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    @Transactional
    public int saveSongList(SongList songList) {
        try {
            Query query = em.createQuery("SELECT s.id from Song s");
            List<Integer> ids = query.getResultList();
            boolean allsongsexisting = true;
            if (songList.getSongList() != null) {
                allsongsexisting = songList.getSongList().stream().allMatch(s -> ids.contains(s.getId()));
            }
            if (!allsongsexisting) {
                return -1;
            }
            em.persist(songList);
            return songList.getId();
        } catch (PersistenceException | NullPointerException p) {
            p.printStackTrace();
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    @Transactional
    public int deleteSongList(int id) {
        try {
            SongList songList = em.find(SongList.class, id);
            if (songList != null) {
                em.remove(songList);
                return 0;
            }
        } catch (PersistenceException p) {
            p.printStackTrace();
            return -1;

        } finally {
            if (em != null) {
                em.close();
            }
        }
        return -1;
    }

    @Override
    @Transactional
    public int updateSongList(SongList updater) {
        try {
            Query query = em.createQuery("SELECT s.id from Song s");
            List<Integer> ids = query.getResultList();
            boolean allsongsexisting = true;
            if (updater.getSongList() != null) {
                allsongsexisting = updater.getSongList().stream().allMatch(s -> ids.contains(s.getId()));
            }
            System.out.println(allsongsexisting);
            if (!allsongsexisting) {
                return -1;
            }
            SongList songList = em.find(SongList.class, updater.getId());
            if (songList != null) {
                songList.setPrivate(updater.isPrivate());
                songList.setSongList(updater.getSongList());
                songList.setName(updater.getName());
                return 0;
            }
        } catch (PersistenceException p) {
            p.printStackTrace();
            return -1;

        } finally {
            if (em != null) {
                em.close();
            }
        }
        return -1;
    }

    @Override
    @Transactional
    public List<SongList> findAllListsOf(String user) {
        try {
            Query q = em.createQuery("select s from SongList s WHERE s.owner = :user");
            q.setParameter("user", user);
            List<SongList> psl = q.getResultList();
            for (int i = 0; i < psl.size(); i++) {
                psl.get(i).getSongList().size();
            }
            return psl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
