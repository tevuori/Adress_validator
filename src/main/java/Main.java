import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.dilley.MineStat;
import org.bson.Document;
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
        //Database MYsql

        sql = new MysqlSetterGetter();
        Main main = new Main();
        //main.mysqlSetup();

        //Database MongoDb
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("data");
        MongoCollection<Document> colstart = database.getCollection("servers");
        colstart.deleteMany(new Document());


        List<String> ips = new ArrayList<>();
        //Run the entire shit with multithreading

        ExecutorService executor = Executors.newFixedThreadPool(2000);

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

        final String PORT = "25565";
        for (String ip : ips) {
            executor.execute(() -> {
                MineStat ms = new MineStat(ip, Integer.parseInt(PORT));
                StringBuilder sb = new StringBuilder();
                if (ms.isServerUp()) {
                    sb.append("Server: ").append(ip).append(" is online\n");
                    sb.append("Motd: ").append(ms.getMotd()).append("\n");
                    sb.append("Players: ").append(ms.getCurrentPlayers()).append("/").append(ms.getMaximumPlayers()).append("\n");
                    sb.append("Version: ").append(ms.getVersion()).append("\n");
                    sb.append(" ");

                    String p5 = ""; //This is the list of players
                    if(ms.getCurrentPlayers()>=1){
                        p5 = selfscan.getThem(ip).toString();
                        System.out.println(p5);
                        sb.append(p5).append("\n");
                    }
                    else{
                        p5 = "No players online";
                        sb.append("No players online").append("\n");
                    }
                    //MysqlSetterGetter.createRecord(ip, ms.getMotd(),ms.getVersion(), ms.getCurrentPlayers() + "/" + ms.getMaximumPlayers(), p6);
                    //Mongob
                    MongoCollection<Document> collection = database.getCollection("servers7");
                    Document document = new Document();
                    document.put("ip", ip);
                    document.put("motd", ms.getMotd());
                    document.put("version", ms.getVersion());
                    document.put("player_count", ms.getCurrentPlayers() + "/" + ms.getMaximumPlayers());
                    document.put("players", p5);
                    collection.insertOne(document);

                    System.out.println(sb.toString());
                    //Try to write the data into output.txt file
                    try {
                        saveDataTofile(sb.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String q1 = "Server: " + ip + " is offline";
                    String q2 = " ";
                    System.out.println(q1);
                    System.out.println(q2);
                    //Try to write the data into output.txt file
                    try {
                        saveDataTofile(q1 + "\n" + q2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
        host = "pro.freedb.tech";
        port = 3306;
        database = "database20";
        username = "DummyUsername1";
        password = "DummyUsernamePassword1";
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
