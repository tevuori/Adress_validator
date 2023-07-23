import me.dilley.MineStat;
import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static MysqlSetterGetter sql;

    public static void main (String[] args) throws IOException {
        //Database

        sql = new MysqlSetterGetter();
        Main main = new Main();
        main.mysqlSetup();

        List<String> ips = new ArrayList<>();
        //Run the entire shit with multithreading

        ExecutorService executor = Executors.newFixedThreadPool(1000);

        //Read file from ips.txt and convert it into usable ArrayList


        File file = new File("C:\\Users\\kubah\\IdeaProjects\\Massping\\src\\main\\java\\ips.txt");
        String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));


        String s2 = content.replaceAll(String.valueOf(','),"");
        String s3 = s2.replaceAll(String.valueOf('\''),"");
        String s4 = s3.replaceAll(String.valueOf(','),"");

        //Split the string contend by space and put it into list ips
        for (String retval: s4.split(" ")) {
            ips.add(retval);
        }

        //Try to use every valid address in the ArrayList and get basic information (status, motd, players...). If the server is not online it will be noted

        for (String ip : ips) {
            executor.execute(() -> {
            MineStat ms = new MineStat(ip, Integer.parseInt("25565"));
            if (ms.isServerUp()) {
                String p1 ="Server: " + ip + " is online";
                String p2 = "Motd: " + ms.getMotd();
                String p3 = "Players: " + ms.getCurrentPlayers() + "/" + ms.getMaximumPlayers();
                String p4 = "Version: " + ms.getVersion();
                String p5 = " ";

                if(ms.isServerUp()){
                    if(ms.getCurrentPlayers()>=1){
                        String p6 = "Players: "; //This is the list of players
                        try {
                            p6 = selfscan.getThem(ip).toString();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(p6);

                        try {
                            saveDataTofile(p6);
                            MysqlSetterGetter.createRecord(ip, ms.getMotd(),ms.getVersion(), ms.getCurrentPlayers() + "/" + ms.getMaximumPlayers(), p6);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                System.out.println(p1);
                System.out.println(p2);
                System.out.println(p3);
                System.out.println(p4);
                System.out.println(p5);

                //Try to write the data into output.txt file

                try {
                    saveDataTofile(p1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    saveDataTofile(p2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    saveDataTofile(p3);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    saveDataTofile(p4);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    saveDataTofile(p5);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            } else {
                String q1 = "Server: " + ip + " is offline";
                String q2 = " ";
                /*System.out.println(q1);
                System.out.println(q2);

                //Try to write the data into output.txt file

                try {
                    saveDataTofile(q1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    saveDataTofile(q2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
            }
        });
        }

    }
    public static void saveDataTofile(String str) throws IOException {
        Path path = Paths.get("C:\\Users\\kubah\\IdeaProjects\\Adress_validator\\src\\main\\java\\output.txt");

        // Try block to check for exceptions
        FileWriter fw = new FileWriter(path.toFile(), true);
        fw.write(str + "\n");
        fw.close();
    }


    //database setup

    public void mysqlSetup(){
        host = "34.116.185.67";
        port = 3306;
        database = "dadta";
        username = "address_validator";
        password = "elotyblbe";
        table = "servers";

        try{

            synchronized (this){

                if(getConnection() != null && !getConnection().isClosed()){
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));

                System.out.println("Connected to database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }
    private static Connection connection;
    public static String host;
    public static String database;
    public static String username;
    public static String password;
    public static String table;
    public static int port;

    public static Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;

    }
    public void disconnect(Connection connection) throws SQLException {
        this.connection.close();

    }
}
