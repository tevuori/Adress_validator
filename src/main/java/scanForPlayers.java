import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class scanForPlayers {
    public static void main (String[] args) throws IOException {
        System.out.println(scan("localhost"));
    }

    public static List<String> scan(String ip) {
        //Get player names using getPlayers.js
        List<String> playerstemp = new ArrayList<String>();

        try {
            ProcessBuilder builder = new ProcessBuilder("node ", "getPlayers.js", ip);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                playerstemp.add(line);
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error executing getPlayers.js " + exitCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Filter unnecessary data
        List<String> playerstemp2 = new ArrayList<String>();
        for (int i = 0; i < playerstemp.size(); i++) {
            if (playerstemp.get(i).contains("name")) {
                playerstemp2.add(playerstemp.get(i).substring(8, playerstemp.get(i).length() - 2));
            }
        }
        List<String> players = new ArrayList<String>();
        for (String s :playerstemp2) {
            int startIndex = s.indexOf("name:") + 6;
            int endIndex = s.indexOf(",", startIndex);
            while (startIndex > 5 && endIndex > startIndex) {
                String playerName = s.substring(startIndex, endIndex);
                players.add(playerName);
                startIndex = s.indexOf("name:", endIndex) + 6;
                endIndex = s.indexOf(",", startIndex);
            }
        }
        return players;
    }
}
