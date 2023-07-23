import me.dilley.MineStat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main (String[] args) throws IOException {
        List<String> ips = new ArrayList<>();
        //Run the entire shit with multithreading

        ExecutorService executor = Executors.newFixedThreadPool(500);

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
                if(ms.getCurrentPlayers() > 0){
                    String p6 = scanForPlayers.scan(ip).toString();
                    System.out.println(p6);

                    try {
                        saveDataTofile(p6);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
}