import java.io.File;
import java.sql.*;
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
            /*
            String query = "SELECT * FROM EMP";
            System.out.println("querying: "+ query);
            ResultSet res = stmt.executeQuery(query);

            // para podermos saber quantas colunas o resultado tem
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            //SABER AS VARIAVEIS
            /*for(int i = 1 ; i <= columnsNumber ; i++){
                System.out.print(rsmd.getColumnName(i) +", ");
            }*/
/*
            while (res.next()) {
                // Listar o resultado da query
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = res.getString(i);
                    System.out.print(columnValue);
                }
                System.out.println("");
            }*/
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
            e.printStackTrace();
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
        ResultSet res;
        String tmp;
        String query = String.format("SELECT * FROM artista art, album a, album_artista aa WHERE aa.album_idalbum = a.idalbum AND aa.artista_idartista = art.idartista AND art.name like '%s'", nameArtist);
        try {
            res = stmt.executeQuery(query);

            while (res.next()) {
                tmp = res.getString("nome");
                System.out.println(tmp);
            }
        } catch (SQLException e) {
            System.out.println("Não foi possível obter albuns");
            return;
        }
    }

    public void getAlbumData(String x){
        String query = String.format("SELECT nome, genero, data FROM album WHERE nome like '%s'", x);

        try {
            ResultSet res = stmt.executeQuery(query);
            while(res.next()) {
                System.out.println("Nome: " + res.getString("nome"));
                System.out.println("Género: " + res.getString("genero"));
                System.out.println("Data de lançamento: " + res.getString("data"));
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

        System.out.println("Alguns lançados:");
        getAlbunsByArtist(x);

    }

    public Boolean editAlbumData(String user, String nomeAlbum){
        return true;
    }

    public Boolean editAlbumName(String user, String nomeAtual, String name){
        return true;
    }

    public Boolean editAlbumDetails (String user, String nomeAlbum, String details){
        return true;
    }

    public Boolean addReview(String user, String album, String review){
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



}
