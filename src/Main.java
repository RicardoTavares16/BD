import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    Statement stmt;
    public void connect() {
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e) {

            System.out.println("Driver not found, check if the jar is reachable !");
            e.printStackTrace();
            return;

        }

        System.out.println("JDBC Driver works .. attempting connection");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "bd",
                    "bd");
        } catch (SQLException e) {
            System.out.println("Connection failed error:");
            e.printStackTrace();
            return;

        }

        if (connection != null) {
            System.out.println("Connected with success");
        } else {
            System.out.println("Connection not established");
        }

        try {

            if(connection.createStatement() == null){
                connection = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:xe",
                        "bd",
                        "bd");
            }

            if((stmt = connection.createStatement()) == null) {
                System.out.println("Erro nao foi possível criar uma statement ou retornou null");
                System.exit(-1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean login(String user, String pass) {
        String user1 = "";
        String query = String.format("SELECT USERNAME FROM UTILIZADOR WHERE USERNAME like '%s' AND PASSWORD like '%s'", user, pass);
        try {

            ResultSet res = stmt.executeQuery(query);

            while (res.next()) {
                user1 = res.getString("USERNAME");
            }
            if (user1.equals(user)) {
                return true;
            } else
                return false;
        } catch (SQLException e) {
        }
        return false;
    }

    public boolean registo(String user, String pass, int editor) {
        String query = String.format("INSERT INTO utilizador (username, password, editor) VALUES ('%s', '%s', %d)", user, pass, editor);
        try {
            stmt.executeQuery(query);
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("UTILIZADOR JA REGISTADO");
            return false;
        }
        return true;
    }

    public Boolean addMusic(String user, String name, String data) {

    if(checkEditor(user)) {
        String query = String.format("INSERT INTO musica (idmusica, nome, data) VALUES (idmusica_seq.nextval, '%s', TO_DATE('%s', 'dd/mm/yyyy'))", name, data);
        try {
            stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar musica.");
            return false;
        }
        return true;
    }
    else
        return false;
    }

    public Boolean addAlbum(String user, String name, String genero, String data){

        if(checkEditor(user)) {
            String query = String.format("INSERT INTO album (idalbum, nome, genero, data) VALUES (idalbum_seq.nextval, '%s', '%s', TO_DATE('%s', 'dd/mm/yyyy'))", name, genero, data);
            try {
                stmt.executeQuery(query);
            } catch (SQLException e) {
                System.out.println("Não foi possível inserir album");
                return false;
            }
            return true;
        }
        else
            return false;
    }

    public Boolean addArtist(String user, String name, String bio, String data){

        if(checkEditor(user)) {
            String query = String.format("INSERT INTO artista (idartista, nome, biografia, data) VALUES (idartista_seq.nextval, '%s', '%s', TO_DATE('%s', 'dd/mm/yyyy'))", name, bio, data);
            try {
                stmt.executeQuery(query);
            } catch (SQLException e) {
                System.out.println("Erro ao adicionar artista.");
                return false;
            }
            return true;
        }
        else
            return false;
    }

    public void getAlbuns() {
        String query = "SELECT * FROM album";
        try {
            ResultSet res = stmt.executeQuery(query);
            while(res.next()) {
                System.out.println(res.getString("nome"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter todos os albuns.");
        }
    }

    public void getAlbunsByArtist(String nameArtist) {
        String query = String.format("SELECT a.nome FROM artista art, album a, album_artista aa WHERE aa.album_idalbum = a.idalbum AND aa.artista_idartista = art.idartista AND art.nome like '%s'", nameArtist);
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                System.out.println("Nome: " + res.getString("NOME"));
            }
        } catch (SQLException e) {
            System.out.println("Erro");
        }
    }

    public void getMusicByArtist(String nameArtist){

        String query = String.format("SELECT m.nome FROM ARTISTA a, MUSICA m, MUSICA_ARTISTA ma WHERE m.IDMUSICA = ma.MUSICA_IDMUSICA AND a.IDARTISTA = ma.ARTISTA_IDARTISTA AND a.IDARTISTA = %d", getArtistID(nameArtist));
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                System.out.println("Nome: " + res.getString("NOME"));
            }
        } catch (SQLException e) {
            System.out.println("Erro");
        }
    }

    public void getMusicians(String name){

        String query = String.format("SELECT m.nomemusico FROM MUSICO m, ARTISTA a, MUSICO_ARTISTA ma WHERE m.NOMEMUSICO = ma.MUSICO_NOME AND a.IDARTISTA = ma.ARTISTA_IDARTISTA AND a.NOME = '%s'", name);
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                System.out.println("Nome: " + res.getString("NOMEMUSICO"));
            }
        } catch (SQLException e) {
            System.out.println("Erro");
        }
    }

    public void getAlbumData(String x){
        String query = String.format("SELECT nome, genero, data FROM album WHERE nome like '%s'", x);
        String queryReviews = String.format("SELECT a.nome, u.USERNAME, c.TEXTO, c.PONTUACAO from ALBUM a, CRITICA c, UTILIZADOR u,CRITICA_ALBUM ca, CRITICA_UTILIZADOR cu WHERE ca.ALBUM_IDALBUM = a.IDALBUM AND ca.CRITICA_IDCRITICA = c.IDCRITICA AND cu.UTILIZADOR_USERNAME = u.USERNAME AND cu.CRITICA_IDCRITICA = c.IDCRITICA AND a.IDALBUM = %d", getAlbumID(x));
        try {
            ResultSet res = stmt.executeQuery(query);
            while(res.next()) {
                System.out.println("Nome: " + res.getString("nome"));
                System.out.println("Género: " + res.getString("genero"));
                System.out.println("Data de lançamento: " + res.getString("data"));
            }

            System.out.println("-----Musicas-----");
            musicByAlbum(x);

            System.out.println("-----CRITICAS-----");
            ResultSet res1 = stmt.executeQuery(queryReviews);
            while(res1.next()) {
                System.out.print("Username: " + res1.getString("USERNAME"));
                System.out.print(" Texto: " + res1.getString("TEXTO"));
                System.out.print(" Pontuação: " + res1.getString("PONTUACAO"));
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println("Erro ao obter informações de album");
        }
    }

    public void getArtistData(String x){

        String query = String.format("SELECT nome, biografia, data FROM artista WHERE nome like '%s'", x);

        try {
            ResultSet res = stmt.executeQuery(query);
            while(res.next()) {
                System.out.println("Nome: " + res.getString("nome"));
                System.out.println("Biografia: " + res.getString("biografia"));
                System.out.println("Data de lançamento: " + res.getString("data"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter informações do artista");
        }

        System.out.println("Musicos da Banda: ");
        getMusicians(x);

        System.out.println("Alguns lançados:");
        getAlbunsByArtist(x);

        System.out.println("Musicas associadas:");
        getMusicByArtist(x);

    }

    public Boolean editAlbumDetails (String user, String nomeAlbum, String details){

        if(checkEditor(user)) {
            String query = String.format("UPDATE ALBUM SET GENERO = '%s' WHERE NOME like '%s'", details, nomeAlbum);
            try {
                stmt.executeQuery(query);
            } catch (SQLException e) {
                System.out.println("Erro ao editar album.");
                return false;
            }
            return true;
        }
        else
            return false;
    }

    public Boolean editArtistDetails (String user, String nome, String details){

        if(checkEditor(user)) {
            String query = String.format("UPDATE ARTISTA SET BIOGRAFIA = '%s' WHERE NOME like '%s'", details, nome);
            try {
                stmt.executeQuery(query);
            } catch (SQLException e) {
                System.out.println("Erro ao editar artista.");
                return false;
            }
            return true;
        }
        else
            return false;
    }

    public Boolean addReview(String user, String album, String review, int score){

        String query = String.format("INSERT INTO CRITICA (IDCRITICA, TEXTO, PONTUACAO) VALUES (idcritica_seq.nextval, '%s', %d)", review, score);
        String query1 = String.format("INSERT INTO CRITICA_ALBUM (album_idalbum, critica_idcritica)  SELECT a.IDALBUM, c.IDCRITICA FROM CRITICA c, ALBUM a WHERE a.IDALBUM = %d AND c.IDCRITICA = (SELECT MAX(IDCRITICA) FROM CRITICA)", getAlbumID(album));
        String query2 = String.format("INSERT INTO CRITICA_UTILIZADOR (utilizador_username, critica_idcritica) SELECT u.USERNAME, c.IDCRITICA FROM UTILIZADOR u, CRITICA c WHERE u.USERNAME like '%s' AND c.IDCRITICA = (SELECT MAX(IDCRITICA) FROM CRITICA)", user);
        try {
            stmt.executeQuery(query);
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Não foi possível adicionar review");
            return false;
        }
        try {
            stmt.executeQuery(query1);
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Não foi possível adicionar review");
            return false;
        }
        try {
            stmt.executeQuery(query2);
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Não foi possível adicionar review");
            return false;
        }
        return true;
    }

    public boolean makeEditor( String user1, String user2){

        if(checkEditor(user1)){
            String query = String.format("UPDATE UTILIZADOR SET EDITOR = 1 WHERE USERNAME like '%s'", user2);
            try {
                stmt.executeQuery(query);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
            return false;

    return false;
    }

    public boolean checkEditor(String user){

        int user1 = 2;
        String query = String.format("SELECT EDITOR FROM UTILIZADOR WHERE USERNAME like '%s'", user);
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                user1 = res.getInt("EDITOR");
            }
            if (user1 == 1) {
                return true;
            } else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean makePlaylist(String user, String nome, ArrayList<String> lista){

        String query = String.format("INSERT INTO PLAYLIST (IDPLAYLIST, NOME) VALUES (IDPLAYLIST_SEQ.nextval, '%s')", nome);
        String query2 = String.format("INSERT INTO UTILIZADOR_PLAYLIST (utilizador_username, playlist_idplaylist) SELECT u.USERNAME, p.IDPLAYLIST FROM UTILIZADOR u, PLAYLIST p WHERE u.USERNAME like '%s' AND p.IDPLAYLIST = (SELECT MAX(IDPLAYLIST) FROM PLAYLIST)", user);

        try {
            stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Não foi possível inserir lista");
            return false;
        }

        try {
            stmt.executeQuery(query2);
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Não foi possível inserir lista");
            return false;
        }

        try {
            for (String temp : lista) {
                String query3 = String.format("INSERT INTO PLAYLIST_MUSICA (PLAYLIST_IDPLAYLIST, MUSICA_IDMUSICA) SELECT p.IDPLAYLIST, m.IDMUSICA FROM PLAYLIST p, MUSICA m WHERE m.IDMUSICA = %d AND p.IDPLAYLIST = (SELECT MAX(IDPLAYLIST) FROM PLAYLIST)", getMusicID(temp));
                stmt.executeQuery(query3);
            }
        } catch (SQLException e) {
            System.out.println("Não foi possível inserir lista");
            return false;
        }

        return true;
    }

    public int getAlbumID(String nome){
        int id = -1;
        String query = String.format("SELECT IDALBUM FROM ALBUM WHERE nome like '%s'", nome);
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                id = res.getInt("IDALBUM");
            }
        } catch (SQLException e) {
            System.out.println("Album não existe");
        }
        return id;
    }

    public int getArtistID(String nome){
        int id = -1;
        String query = String.format("SELECT IDARTISTA FROM ARTISTA WHERE nome like '%s'", nome);
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                id = res.getInt("IDARTISTA");
            }
        } catch (SQLException e) {
            System.out.println("Artista não existe");
        }
        return id;
    }

    public int getMusicID(String nome){
        int id = -1;
        String query = String.format("SELECT IDMUSICA FROM MUSICA WHERE nome like '%s'", nome);
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                id = res.getInt("IDMUSICA");
            }
        } catch (SQLException e) {
            System.out.println("Musica não existe");
        }
        return id;
    }

    public int getPlaylistID(String nome){
        int id = -1;
        String query = String.format("SELECT IDPLAYLIST FROM PLAYLIST WHERE nome like '%s'", nome);
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                id = res.getInt("IDPLAYLIST");
            }
        } catch (SQLException e) {
            System.out.println("Musica não existe");
        }
        return id;
    }

    public void musicByAlbum(String album){

        String query = String.format("SELECT a.nome Album, m.nome Musica FROM ALBUM a, MUSICA m, MUSICA_ALBUM ma WHERE m.IDMUSICA = ma.MUSICA_IDMUSICA AND a.IDALBUM = ma.ALBUM_IDALBUM AND a.IDALBUM = %d", getAlbumID(album));
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                System.out.println("Nome: " + res.getString("Musica"));
            }
        } catch (SQLException e) {
            System.out.println("Album não existe");
        }

    }

    public void playlists(){

        String query = String.format("SELECT p.nome, u.USERNAME FROM PLAYLIST p, UTILIZADOR u, UTILIZADOR_PLAYLIST up WHERE up.UTILIZADOR_USERNAME = u.USERNAME and up.PLAYLIST_IDPLAYLIST = p.IDPLAYLIST ORDER BY u.USERNAME");
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                System.out.println("Nome Playlist: " + res.getString("NOME"));
                System.out.println("Autor: " + res.getString("USERNAME"));
            }
        } catch (SQLException e) {
            System.out.println("Erro");
        }

    }

    public void getPlaylistInfo(String nome){

        String query = String.format("SELECT m.nome FROM MUSICA m, PLAYLIST p, UTILIZADOR u, UTILIZADOR_PLAYLIST up, PLAYLIST_MUSICA pa WHERE p.IDPLAYLIST = up.PLAYLIST_IDPLAYLIST AND u.USERNAME = up.UTILIZADOR_USERNAME AND m.IDMUSICA = pa.MUSICA_IDMUSICA AND p.IDPLAYLIST = pa.PLAYLIST_IDPLAYLIST   AND IDPLAYLIST = %d", getPlaylistID(nome));
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                System.out.println("Musica: " + res.getString("NOME"));
            }
        } catch (SQLException e) {
            System.out.println("Erro");
        }

    }

    public void musicList(){
        String query = String.format("SELECT IDMUSICA, NOME FROM MUSICA");
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                System.out.println("ID: " + res.getString("IDMUSICA"));
                System.out.println("Nome: " + res.getString("NOME"));
            }
        } catch (SQLException e) {
            System.out.println("Erro");
        }
    }

    public boolean upload(String nome, String caminho){

        String query = String.format("UPDATE MUSICA SET FICHEIRO = '%s' WHERE IDMUSICA = %d", caminho, getMusicID(nome));
        try {
            stmt.executeQuery(query);
            return true;
        } catch (SQLException e) {
            System.out.println("Erro");
            return false;
        }
    }

    public boolean download(String nome){

        String query = String.format("SELECT ficheiro FROM MUSICA WHERE IDMUSICA = %d", getMusicID(nome));
        try {
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                System.out.println("Nome: " + res.getString("FICHEIRO"));
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Erro");
            return false;
        }
    }

}
