CREATE TABLE musica (
                      idmusica		 INTEGER,
                      nome		 VARCHAR2(512) NOT NULL,
                      data		 DATE,
                      letras		 VARCHAR2(512),
                      ficheiro		 VARCHAR2(512) UNIQUE,
                      PRIMARY KEY(idmusica)
);

CREATE TABLE album (
                     idalbum		 INTEGER,
                     nome		 VARCHAR2(512) NOT NULL,
                     genero		 VARCHAR2(512),
                     data		 DATE,
                     editora		 VARCHAR2(512),
                     PRIMARY KEY(idalbum)
);

CREATE TABLE artista (
                       idartista INTEGER,
                       nome	 VARCHAR2(512) UNIQUE NOT NULL,
                       biografia VARCHAR2(512),
                       data	 DATE,
                       PRIMARY KEY(idartista)
);

CREATE TABLE musico (
                      nomemusico	 VARCHAR2(512) NOT NULL,
                      instrumento VARCHAR2(512),
                      PRIMARY KEY(nomemusico)
);

CREATE TABLE playlist (
                        idplaylist		 INTEGER,
                        nome		 VARCHAR2(512) NOT NULL,
                        PRIMARY KEY(idplaylist)
);

CREATE TABLE utilizador (
                          username VARCHAR2(512),
                          password VARCHAR2(512) NOT NULL,
                          editor	 SMALLINT NOT NULL,
                          PRIMARY KEY(username)
);

CREATE TABLE critica (
                       idcritica		 INTEGER,
                       texto		 VARCHAR2(512),
                       pontuacao		 INTEGER,
                       PRIMARY KEY(idcritica)
);

CREATE TABLE pesquisa (
                        utilizador_username VARCHAR2(512) UNIQUE NOT NULL,
                        musica_idmusica	 INTEGER,
                        PRIMARY KEY(musica_idmusica)
);

CREATE TABLE concerto (
                        localizacao VARCHAR2(512),
                        data	 DATE,
                        PRIMARY KEY(localizacao, data)
);

CREATE TABLE compositor (
                          nome VARCHAR2(512),
                          PRIMARY KEY(nome)
);

CREATE TABLE critica_utilizador (
                                  utilizador_username VARCHAR2(512),
                                  critica_idcritica INTEGER,
                                  PRIMARY KEY(utilizador_username, critica_idcritica)
);

CREATE TABLE critica_album (
                              album_idalbum INTEGER,
                              critica_idcritica INTEGER,
                              PRIMARY KEY(album_idalbum, critica_idcritica)
);

CREATE TABLE compositor_musica (
                                 musica_idmusica INTEGER,
                                 compositor_nome VARCHAR2(512),
                                 PRIMARY KEY(musica_idmusica, compositor_nome)
);

CREATE TABLE musico_artista (
                              artista_idartista INTEGER,
                              musico_nome VARCHAR2(512),
                              PRIMARY KEY(artista_idartista, musico_nome)
);

CREATE TABLE concerto_musica (
                               musica_idmusica INTEGER,
                               concerto_localizacao VARCHAR2(512),
                               concerto_data DATE,
                               PRIMARY KEY(musica_idmusica, concerto_localizacao, concerto_data)
);

CREATE TABLE utilizador_playlist (
	                                 utilizador_username VARCHAR2(512),
                                   playlist_idplaylist INTEGER,
                                   PRIMARY KEY (utilizador_username, playlist_idplaylist)
);


CREATE TABLE playlist_musica (
                               playlist_idplaylist INTEGER,
                               musica_idmusica	 INTEGER,
                               PRIMARY KEY(playlist_idplaylist,musica_idmusica)
);

CREATE TABLE musica_album (
                            musica_idmusica INTEGER,
                            album_idalbum	 INTEGER,
                            PRIMARY KEY(musica_idmusica,album_idalbum)
);

CREATE TABLE musica_artista (
	                             musica_idmusica INTEGER,	
                               artista_idartista INTEGER,	
                               PRIMARY KEY(musica_idmusica, artista_idartista)
);

CREATE TABLE album_artista (
	                           album_idalbum INTEGER,
                             artista_idartista INTEGER,
                             PRIMARY KEY(album_idalbum, artista_idartista)
);

ALTER TABLE critica ADD CONSTRAINT limitePontuacao CHECK (pontuacao <= 5 AND pontuacao > 0);
ALTER TABLE pesquisa ADD CONSTRAINT pesquisa_fk1 FOREIGN KEY (utilizador_username) REFERENCES utilizador(username);
ALTER TABLE pesquisa ADD CONSTRAINT pesquisa_fk2 FOREIGN KEY (musica_idmusica) REFERENCES musica(idmusica);
ALTER TABLE compositor_musica ADD CONSTRAINT compositor_musica_fk1 FOREIGN KEY (musica_idmusica) REFERENCES musica(idmusica);
ALTER TABLE compositor_musica ADD CONSTRAINT compositor_musica_fk2 FOREIGN KEY (compositor_nome) REFERENCES compositor(nome);
ALTER TABLE musico_artista ADD CONSTRAINT musico_artista_fk1 FOREIGN KEY (artista_idartista) REFERENCES artista(idartista);
ALTER TABLE musico_artista ADD CONSTRAINT musico_artista_fk2 FOREIGN KEY (musico_nome) REFERENCES musico(nomemusico);
ALTER TABLE critica_utilizador ADD CONSTRAINT critica_utilizador_fk1 FOREIGN KEY (utilizador_username) REFERENCES utilizador(username);
ALTER TABLE critica_utilizador ADD CONSTRAINT critica_utilizador_fk2 FOREIGN KEY (critica_idcritica) REFERENCES critica(idcritica);
ALTER TABLE critica_album ADD CONSTRAINT critica_album_fk1 FOREIGN KEY (album_idalbum) REFERENCES album(idalbum);
ALTER TABLE critica_album ADD CONSTRAINT critica_album_fk2 FOREIGN KEY (critica_idcritica) REFERENCES critica(idcritica);
ALTER TABLE concerto_musica ADD CONSTRAINT concerto_musica_fk1 FOREIGN KEY (musica_idmusica) REFERENCES musica(idmusica);
ALTER TABLE utilizador_playlist ADD CONSTRAINT utilizador_playlist_fk1 FOREIGN KEY (utilizador_username) REFERENCES utilizador(username);
ALTER TABLE utilizador_playlist ADD CONSTRAINT utilizador_playlist_fk2 FOREIGN KEY (playlist_idplaylist) REFERENCES playlist(idplaylist);
ALTER TABLE playlist_musica ADD CONSTRAINT playlist_musica_fk1 FOREIGN KEY (playlist_idplaylist) REFERENCES playlist(idplaylist);
ALTER TABLE playlist_musica ADD CONSTRAINT playlist_musica_fk2 FOREIGN KEY (musica_idmusica) REFERENCES musica(idmusica);
ALTER TABLE musica_album ADD CONSTRAINT musica_album_fk1 FOREIGN KEY (musica_idmusica) REFERENCES musica(idmusica);
ALTER TABLE musica_album ADD CONSTRAINT musica_album_fk2 FOREIGN KEY (album_idalbum) REFERENCES album(idalbum);
ALTER TABLE musica_artista ADD CONSTRAINT musica_artista_fk1 FOREIGN KEY (musica_idmusica) REFERENCES musica(idmusica);
ALTER TABLE musica_artista ADD CONSTRAINT musica_artista_fk2 FOREIGN KEY (artista_idartista) REFERENCES artista(idartista);
ALTER TABLE album_artista ADD CONSTRAINT album_artista_fk1 FOREIGN KEY (album_idalbum) REFERENCES album(idalbum);
ALTER TABLE album_artista ADD CONSTRAINT album_artista_fk2 FOREIGN KEY (artista_idartista) REFERENCES artista(idartista);

CREATE SEQUENCE idmusica_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE idalbum_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE idartista_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE idplaylist_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE idcritica_seq START WITH 1 INCREMENT BY 1;