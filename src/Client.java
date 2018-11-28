import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Classe da Interface de Utilizador
 */

public class Client {
    private static Main Main;
    private static Client client;
    private String useratual;
    Scanner sc = new Scanner(System.in);
    Scanner string = new Scanner(System.in);

    /**
     * Menu inicial do programa, opção de login ou registo
     */
    public void login() {

        System.out.println("|-----------------|\n");
        System.out.println("|----DropMusic----|\n");
        System.out.println("|-----------------|\n");
        System.out.println();
        System.out.println("1 - Login");
        System.out.println("2 - Registo");
        System.out.println("3 - Sair");
        System.out.print("-> ");

        int input;
        input = sc.nextInt();

        try {
            switch (input) {
                case 1:
                    System.out.println("-----Login-----\n");
                    System.out.println("Nome: ");
                    String nome = sc.next();
                    useratual = nome;
                    System.out.println("Password:");
                    String pass = sc.next();
                    if (nome.isEmpty() || pass.isEmpty())
                        System.out.println("Preencher ambos os campos");

                    if (Main.login(nome, pass)) {
                        System.out.println("Login bem sucedido");
                        menu();
                    } else {
                        System.out.println("Login Incorrecto");
                        login();
                    }
                    break;
                case 2:
                    System.out.println("-----Registo-----\n");
                    System.out.println("Nome: ");
                    String nome2 = sc.next();
                    System.out.println("Password:");
                    String pass2 = sc.next();
                    System.out.println("Editor? ");
                    int editor = sc.nextInt();
                    if (Main.registo(nome2, pass2, editor)) {
                        System.out.println("Registo bem sucedido");
                        menu();
                    } else {
                        System.out.println("Erro no registo");
                        login();
                    }
                    break;
                case 3:
                    break;
            }

        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
    }

    /**
     * Função que lê as opções introduzidas pelo utilizador e chama as funções do servidor rmi
     */

    private void menu() {

        Menu menu = Menu.Inicial;
        int input;
        String album = null;
        Scanner scanner = new Scanner(System.in);

        while (true) {

            if (false) break;

            printMenu(menu);
            input = sc.nextInt();
            try {
                switch (menu) {

                    case Inicial:
                        switch (input) {
                            case 1:
                                menu = Menu.Albuns;
                                Main.getAlbuns();
                                break;
                            case 2:
                                menu = Menu.Pesquisa;
                                break;
                            case 3:
                                menu = Menu.Adicao;
                                break;
                            case 4:
                                menu = Menu.Gestao;
                                break;
                            case 5:
                                menu = Menu.Playlists;
                                break;
                            case 6:
                                menu = Menu.Upload;
                            case 7:
                                return;
                            default:
                                break;
                        }
                        break;
                    case Albuns:
                        switch (input) {
                            case 1:
                                System.out.println("Introduza o nome do album:");
                                String opcao = scanner.nextLine();
                                System.out.println("Escolha: " + opcao);
                                Main.getAlbumData(opcao);
                                menu = Menu.AlbunsExt;
                                break;
                            case 0:
                                menu = Menu.Inicial;
                                break;
                        }
                        break;
                    case AlbunsExt:
                        switch (input) {
                            case 1:
                                // Add review
                                System.out.println("Critica: ");
                                scanner.nextLine();
                                String review = scanner.nextLine();
                                System.out.println("Pontuacao: ");
                                String tmp = scanner.nextLine();
                                int score = Integer.parseInt(tmp);

                                if (Main.addReview(useratual, album, review, score)) {
                                    System.out.println("Sucesso");
                                } else {
                                    System.out.println("Falha");
                                }
                                break;
                            case 2:
                                // detalhes
                                System.out.println("Editar Genero:");
                                String details = scanner.nextLine();
                                if (Main.editAlbumDetails(useratual, album, details)) {
                                    System.out.println("Sucesso");
                                } else {
                                    System.out.println("Falha");
                                }
                                break;
                            case 0:
                                menu = Menu.Inicial;
                                break;
                        }
                        break;
                    case Pesquisa:
                        switch (input) {
                            case 1:
                                System.out.println("Nome do artista:");
                                String s = scanner.nextLine();
                                Main.getArtistData(s);
                                System.out.println("1 - Editar Bio");
                                System.out.println("0 - Voltar");
                                System.out.println("-> ");
                                int op = sc.nextInt();
                                if(op == 1){
                                    System.out.println("Nova Biografia:");
                                    String bio = scanner.nextLine();
                                    if(Main.editArtistDetails(useratual, s, bio)){
                                        System.out.println("Sucesso");
                                    }
                                    else
                                        System.out.println("Erro");
                                        break;
                                }
                                break;
                            case 2:
                                System.out.println("Nome do album:");
                                String x = scanner.nextLine();
                                Main.getAlbumData(x);
                                break;
                            case 0:
                                menu = Menu.Inicial;
                                break;
                        }
                        break;
                    case Adicao:
                        switch (input) {
                            case 1:
                                // Add music
                                System.out.println("Nome da musica para adicionar: ");
                                String nomeMusica = scanner.nextLine();
                                System.out.println("Data: (dd/mm/yyyy)");
                                String dataMusica = scanner.nextLine();
                                if(Main.addMusic(useratual, nomeMusica, dataMusica)) {
                                    System.out.println("Feito");
                                } else {
                                    System.out.println("Falha");
                                }
                                break;
                            case 2:
                                // Add Album
                                System.out.println("Nome do album para adicionar: ");
                                String nomeAlbum = scanner.nextLine();
                                System.out.println("Genero Musical:");
                                String genero = scanner.nextLine();
                                System.out.println("Data: (dd/mm/yyyy)");
                                String dataAlbum = scanner.nextLine();
                                if(Main.addAlbum(useratual, nomeAlbum, genero, dataAlbum)) {
                                    System.out.println("Feito");
                                } else {
                                    System.out.println("Falha");
                                }
                                break;
                            case 3:
                                // Add Artist
                                System.out.println("Nome do artista para adicionar: ");
                                String nome = scanner.nextLine();
                                System.out.println("Biografia: ");
                                String bio = scanner.nextLine();
                                System.out.println("Data: (dd/mm/yyyy)");
                                String data = scanner.nextLine();
                                if(Main.addArtist(useratual, nome, bio, data)) {
                                    System.out.println("Feito");
                                } else {
                                    System.out.println("Falha");
                                }
                                break;
                            case 0:
                                menu = Menu.Inicial;
                                break;
                        }
                        break;
                    case Playlists:
                        switch(input) {
                            case 1:

                                break;
                            case 2:
                                int escolha = sc.nextInt();
                                break;
                            case 3:
                                System.out.println("Nome da Playlist:");
                                String nomePlaylist = sc.nextLine();
                                System.out.println("Introduza musicas para playlist: (fim para)");
                                String musica;
                                ArrayList<String> musicas=new ArrayList<String>();
                                do{
                                    musica = sc.nextLine();
                                    musicas.add(musica);
                                }while (!musica.equals("fim"));
                                int size = musicas.size();
                                musicas.remove(size-1);
                                Main.makePlaylist(useratual, nomePlaylist, musicas);
                                break;
                            case 0:
                                menu = Menu.Inicial;
                                break;
                        }
                        break;
                    case Gestao:
                        switch (input) {
                            case 1:
                                System.out.println("Utilizador");
                                String user2 = string.nextLine();
                                if (Main.makeEditor(useratual, user2)) {
                                    System.out.println("Permissão dada");
                                } else
                                    System.out.println("Permissão recusada");
                                break;
                            case 0:
                                menu = Menu.Inicial;
                                break;
                        }
                        break;
                    case Upload:
                        switch (input) {
                            case 1:
                                //rmi.upload(file);
                                break;
                            case 2:
                                //rmi.download(file);
                                break;
                            case 0:
                                menu = Menu.Inicial;
                                break;
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.println("Exception in main: " + e);
            }
        }

    }

    /**
     * Enum do menu
     */
    public enum Menu {
        Inicial, Albuns, AlbunsExt, Adicao, Pesquisa, Playlists, Gestao, Upload,
    }

    /**
     * Recebe o menu que se pretende e imprime no terminal as opções de cada parte
     *
     * @param menu (enum Menu)
     */

    public static void printMenu(Menu menu) {
        switch (menu) {
            case Inicial:
                System.out.println("-----Menu-----\n");
                System.out.println("1 - Listar Albuns");
                System.out.println("2 - Pesquisa");
                System.out.println("3 - Adicionar");
                System.out.println("4 - Gestão");
                System.out.println("5 - Playlists");
                System.out.println("6 - Upload/Download");
                System.out.println("7 - Sair");
                System.out.println("-> ");
                break;
            case Albuns:
                System.out.println("-----Albuns-----");
                System.out.println("1 - Escolher album");
                System.out.println("0 - Voltar");
                System.out.println("-> ");
                break;
            case AlbunsExt:
                System.out.println("-----Informação-----");
                System.out.println("1 - Escrever Critica");
                System.out.println("2 - Editar Genero");
                System.out.println("0 - Voltar");
                System.out.println("-> ");
                break;
            case Pesquisa:
                System.out.println("-----Pesquisa-----");
                System.out.println("1 - Por artista:");
                System.out.println("2 - Por album:");
                System.out.println("0 - Voltar");
                System.out.println("-> ");
                break;
            case Adicao:
                System.out.println("-----Adicionar-----");
                System.out.println("1 - Adicionar nova musica:");
                System.out.println("2 - Adicionar novo album:");
                System.out.println("3 - Adicionar novo artista:");
                System.out.println("0 - Voltar");
                System.out.println("-> ");
                break;
            case Playlists:
                System.out.println("-----Playlists-----");
                System.out.println("1 - Listar Playlists");
                System.out.println("2 - Ver Playlist");
                System.out.println("3 - Criar Playlist");
                System.out.println("0 - Voltar");
                System.out.println("-> ");
                break;
            case Gestao:
                System.out.println("-----Gestao-----");
                System.out.println("1 - Dar Previlegios:");
                System.out.println("0 - Voltar");
                System.out.println("-> ");
                break;
            case Upload:
                System.out.println("-----Upload/Download-----");
                System.out.println("1 - Upload:");
                System.out.println("2 - Download:");
                System.out.println("0 - Voltar");
                System.out.println("-> ");
                break;

        }
    }

    /**
     * Cria novo cliente
     */
    public static void main(String args[]) {

            client = new Client();
            Main = new Main();
            Main.connect();
            client.login();


    }
}


