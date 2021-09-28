package songlistManagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "songlist")
public class SongList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ownerid")
    private String owner;

    @Column(name = "name")
    private String name;

    @JsonProperty(value = "isPrivate")
    @Column(name = "privacy")
    private boolean isPrivate;

    @ManyToMany()
    @JoinTable(name = "songlist_song", joinColumns = {@JoinColumn(name = "songListId", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "songId", referencedColumnName = "id")})
    private List<Song> songList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        if (owner == null) {
            return "";
        }
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songs) {
        this.songList = songs;
    }

    @JsonProperty(value = "isPrivate")
    public boolean isPrivate() {
        return isPrivate;
    }

    @JsonProperty(value = "isPrivate")
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
