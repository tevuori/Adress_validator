import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlSetterGetter {
    public static void createRecord(String ip, String motd,String version,String Player_count, String players){
        try{
                PreparedStatement insert = Main.getConnection().prepareStatement("INSERT INTO " + "servers" + " (ip,motd,version,player_count,players) VALUE (?,?,?,?,?)");
                insert.setString(1, ip.toString());
                insert.setString(2,motd);
                insert.setString(3, version);
                insert.setString(4, Player_count);
                insert.setString(5, players);
                insert.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
