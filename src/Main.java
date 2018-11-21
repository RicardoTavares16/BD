import java.sql.*;

public class Main {

    public static void main(String[] args) {
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

            Statement stmt;

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

            for(int i = 1 ; i <= columnsNumber ; i++){
                System.out.print(rsmd.getColumnName(i) +", ");
            }


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
}
