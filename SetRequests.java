import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SetRequests {

    private ArrayList<String> jsonObjects = new ArrayList<>();
    ClientDao dbConnect;

    public SetRequests(ClientDao dbConnect) {
        this.dbConnect = dbConnect;
    }

    public void createJSON() throws SQLException {
        List<Client> clients = dbConnect.getAll();
        System.out.println(clients);
        for (Client c : clients){
            JsonParser json = new JsonParser();
            json.put("name","ВК-ТРЕКЕР тест");
            json.put("created", c.getTime());
            json.put("created_by", "ВК-ТРЕКЕР");
//            json.put("firstPhone", c.getPhoneFirst());
//            json.put("secondPhone", c.getPhoneSecond());
            json.put("info_source", "заявка с " + c.getSite());
            json.put("url", c.getRef());

            jsonObjects.add(json.toString());
            dbConnect.delete();
        }
    }

    public void send(){
        String s = jsonObjects.toString();
        byte [] encodeURL = Base64.getEncoder().encode("1843:h29J460ED3uOmC-IlgFcgUj7mnd0s_Rw".getBytes());
        String siteKey = new String(encodeURL);
//        s = siteKey + s;
        URL url = null;
//        try {
//            url = new URL("https://api.myfreshcloud.com/companies/ " + siteKey);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST");
//            con.setDoOutput(true);
//            con.getOutputStream().write(s.getBytes("UTF-8"));
//            con.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void printJson(){
        System.out.println(jsonObjects);
    }
}
