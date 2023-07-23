import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.util.internal.StringUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class selfscan {

    public static void main(String[] args) throws JSONException {
        System.out.println(getThem("5.181.13.32"));
    }

    public static List<String> getThem(String ip) throws JSONException {
        String urlString = "https://api.mcstatus.io/v2/status/java/" + ip;
        String jsonString = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString += line;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(jsonString);
        List<String> players = new ArrayList<String>();

        //Filter the shit from names

        String[] parts = jsonString.split("\"name_raw\":");
        for (int i = 1; i < parts.length; i++) {
            String name = parts[i].split(",")[0];
            players.add(name);
        }
        System.out.println(players);

        //Remove version from desired List

        players.removeIf(s -> s.contains("."));
        return players;
    }
}

