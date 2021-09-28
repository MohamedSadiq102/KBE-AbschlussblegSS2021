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
import songlistManagement.dao.ISongListDAO;
import songlistManagement.model.Song;
import songlistManagement.model.SongList;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource("/test.properties")
class SongsListControllerTest {

    @Autowired
    private MappingJackson2XmlHttpMessageConverter xmlConverter;


    private MockMvc mockMvc;
    private Song song;
    private SongList songList;
    private SongList privateList;
    private SongList multiuse;
    private ISongListDAO mockedSongListDAO;

    @BeforeEach
    void setUp(){
        song=new Song();
        song.setTitle("Titel");
        song.setArtist("Artist");
        song.setLabel("Label");
        song.setReleased(2020);
        song.setId(1);
        songList=new SongList();
        songList.setOwner("mmuster");
        songList.setName("Maximepublic");
        songList.setPrivate(false);
        songList.setId(1);
        privateList= new SongList();
        privateList.setOwner("mmuster");
        privateList.setId(2);
        privateList.setSongList(List.of(song));
        privateList.setPrivate(true);
        privateList.setName("Maximeprivate");
        multiuse= new SongList();
        multiuse.setOwner("mmuster");
        multiuse.setId(3);
        multiuse.setSongList(List.of(song));
        multiuse.setPrivate(true);
        multiuse.setName("Multiuse");
        mockedSongListDAO=mock(ISongListDAO.class);
        mockMvc= MockMvcBuilders.standaloneSetup(new SongsListController(mockedSongListDAO)).build();
    }

    @Test
    void getSongListJsonWithPathVariable_success() {
        when(mockedSongListDAO.findById(anyInt())).thenReturn(songList);
        try{
            mockMvc.perform(get("/songLists/1").accept(MediaType.APPLICATION_JSON).header("userId-Header","mmuster"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Maximepublic"))
                    .andExpect(jsonPath("$.owner").value("mmuster"))
                    .andExpect(jsonPath("$.isPrivate").value(false))
                    .andExpect(jsonPath("$.songList").value(songList.getSongList()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getSongListJsonWithPathVariable_private_list_of_foreign_user_access_denied(){
        when(mockedSongListDAO.findById(anyInt())).thenReturn(privateList);
        try{
            mockMvc.perform(get("/songLists/1").accept(MediaType.APPLICATION_JSON).header("userId-Header","jemand"))
                    .andExpect(status().isForbidden());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getSongListXmlWithPathVariable_success() {
        when(mockedSongListDAO.findById(anyInt())).thenReturn(songList);
        try{
            ObjectMapper xmlmapper=xmlConverter.getObjectMapper();
            mockMvc.perform(get("/songLists/1").accept(MediaType.APPLICATION_XML).header("userId-Header","mmuster"))
                    .andExpect(content().contentType(MediaType.APPLICATION_XML))
                    .andExpect(content().xml(xmlmapper.writeValueAsString(songList)));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getSongListXMLWithPathVariable_private_list_of_foreign_user_access_denied(){
        when(mockedSongListDAO.findById(anyInt())).thenReturn(privateList);
        try{
            mockMvc.perform(get("/songLists/1").accept(MediaType.APPLICATION_XML).header("userId-Header","jemand"))
                    .andExpect(status().isForbidden());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getSongListofUserJson_with_request_of_own_lists_returns_all() {
        when(mockedSongListDAO.findAllListsOf(anyString())).thenReturn(List.of(songList,privateList));
        try{
            ObjectMapper mapper=new ObjectMapper();
            mockMvc.perform(get("/songLists?userId=mmuster").accept(MediaType.APPLICATION_JSON).header("userId-Header","mmuster"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(mapper.writeValueAsString(List.of(songList,privateList))));
            verify(mockedSongListDAO).findAllListsOf("mmuster");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getSongListOfUserJson_request_of_foreign_user_returns_only_public_lists(){
        when(mockedSongListDAO.findPublicListsOf(anyString())).thenReturn(List.of(songList));
        try{
            ObjectMapper mapper=new ObjectMapper();
            mockMvc.perform(get("/songLists?userId=mmuster").accept(MediaType.APPLICATION_JSON).header("userId-Header","jemand"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(mapper.writeValueAsString(List.of(songList))));
            verify(mockedSongListDAO).findPublicListsOf("mmuster");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getSongListOfUserXml_with_request_of_own_lists_returns_all() {
        when(mockedSongListDAO.findAllListsOf(anyString())).thenReturn(List.of(songList,privateList));
        try{
            ObjectMapper xmlmapper=xmlConverter.getObjectMapper();
            String expectedraw=xmlmapper.writeValueAsString(List.of(songList,privateList));
            String expected=expectedraw.replaceAll("12","");
            mockMvc.perform(get("/songLists?userId=mmuster").accept(MediaType.APPLICATION_XML).header("userId-Header","mmuster"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_XML))
                    .andExpect(content().string(expected));
            verify(mockedSongListDAO).findAllListsOf("mmuster");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void getSongListOfUserXml_request_of_foreign_user_returns_only_public_lists(){
        when(mockedSongListDAO.findPublicListsOf(anyString())).thenReturn(List.of(songList));
        try{
            ObjectMapper xmlmapper=xmlConverter.getObjectMapper();
            String expectedraw=xmlmapper.writeValueAsString(List.of(songList));
            String expected=expectedraw.replaceAll("12","");
            mockMvc.perform(get("/songLists?userId=mmuster").accept(MediaType.APPLICATION_XML).header("userId-Header","jemand"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_XML))
                    .andExpect(content().string(expected));
            verify(mockedSongListDAO).findPublicListsOf("mmuster");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void postSong_success() {
        when(mockedSongListDAO.saveSongList(any())).thenReturn(3);
        try{
            ObjectMapper mapper=new ObjectMapper();
            mockMvc.perform(post("/songLists").header("userId-Header","mmuster").content(mapper.writeValueAsString(multiuse)).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Location","/songLists/3"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void deleteSongList_own_list_deleted_successfully() {
        when(mockedSongListDAO.findById(anyInt())).thenReturn(songList);
        when(mockedSongListDAO.deleteSongList(anyInt())).thenReturn(0);
        try{
            mockMvc.perform(delete("/songLists/1").header("userId-Header","mmuster"))
                    .andExpect(status().isNoContent());
            verify(mockedSongListDAO).deleteSongList(1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void deleteSongList_list_of_different_user_cant_be_deleted(){
        when(mockedSongListDAO.findById(anyInt())).thenReturn(songList);
        when(mockedSongListDAO.deleteSongList(anyInt())).thenReturn(0);
        try{
            mockMvc.perform(delete("/songLists/1").header("userId-Header","jemand"))
                    .andExpect(status().isBadRequest());
            verify(mockedSongListDAO,times(0)).deleteSongList(1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void updateSongList_own_list_successfully() {
        multiuse.setId(1);
        when(mockedSongListDAO.findById(anyInt())).thenReturn(multiuse);
        when(mockedSongListDAO.updateSongList(any())).thenReturn(0);
        try{
            ObjectMapper mapper=new ObjectMapper();
            mockMvc.perform(put("/songLists/1").header("userId-Header","mmuster").content(mapper.writeValueAsString(multiuse)).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}