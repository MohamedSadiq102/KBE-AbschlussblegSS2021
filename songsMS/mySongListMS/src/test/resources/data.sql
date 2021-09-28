CREATE TABLE song (id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1 INCREMENT BY 1) PRIMARY KEY, artist VARCHAR(100), title VARCHAR(100) NOT NULL, label VARCHAR(100), released INTEGER);
CREATE TABLE songlist(id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1 INCREMENT BY 1) PRIMARY KEY, ownerid VARCHAR(50), name VARCHAR(100),privacy BOOLEAN);
CREATE TABLE songlist_song(SongListId INTEGER, songId INTEGER, PRIMARY KEY(songListId,songId), FOREIGN  KEY (songListId) REFERENCES songlist(id),FOREIGN KEY (songId) REFERENCES song(id));
delete from song;
delete from songlist_song;
delete from songlist;
insert into song (title,artist,label,released) values ('Titel_auto_insert','Maxime','Muster_records',2020);
insert into songlist(ownerid, name, privacy) values ('mmuster','Maximepublic', false);
insert into songlist_song(songListId,songId) values (1,1);