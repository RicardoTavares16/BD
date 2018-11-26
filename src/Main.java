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
        System.out.println("JDBC Driver funciona .. tentar a ligacao");
        System.out.println("JDBC Driver works .. attempting connection");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe",
                    "bd",
                    "bd");
        } catch (SQLException e) {
            System.out.println("Ligacao falhou.. erro:");
            System.out.println("Connection failed error:");
            e.printStackTrace();
            return;

        }

        if (connection != null) {
            System.out.println("Ligação feita com sucessso");
            System.out.println("Connected with success");
        } else {
            System.out.println("Nao conseguimos estabelecer a ligacao");
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

            String query = "SELECT * FROM EMP";
            System.out.println("querying: "+ query);
            ResultSet res = stmt.executeQuery(query);

            // para podermos saber quantas colunas o resultado tem
            // To check how many columns does the result holds
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            //SABER AS VARIAVEIS
            /*for(int i = 1 ; i <= columnsNumber ; i++){
                System.out.print(rsmd.getColumnName(i) +", ");
            }*/


            while (res.next()) {
                // Listar o resultado da query
                // List the result from the query
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = res.getString(i);
                    System.out.print(columnValue);
                }
                System.out.println("");
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

    public Boolean addMusic(String name) {
        return true;
    }

    public Boolean addAlbum(String name){
        return true;
    }

    public Boolean addArtist(String name, String bio, String data){
        String query = String.format("INSERT INTO artista (idartista, username, password, editor) VALUES (idartista_seq.nextval, '%s', '%s', %s)", name, bio, data);
        return true;
    }

    public String getAlbuns(){
        String x = "";
        return x;
    }

    public String getAlbumData(String x){
        return x;
    }

    public String getArtistData(String x){
        return x;
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

    public void pesquisaArtista(String s){

    }

    public void pesquisaAlbum(String s){

    }

    public boolean makeEditor( String user1, String user2){
        return true;
    }



}
