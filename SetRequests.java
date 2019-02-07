
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SetRequests {

    private ArrayList<String> jsonObjects = new ArrayList<>();
    ClientDao dbConnect;

    public SetRequests(ClientDao dbConnect) {
        this.dbConnect = dbConnect;
    }

    public void createJSON() throws SQLException {
        List<Client> clients = dbConnect.getAll();
//        System.out.println(clients);
        for (Client c : clients){
            JsonParser json = new JsonParser();
            if (c.getSite().equals("http://petrobani.ru/")) json.put("name","Tracker-MB");
            if (c.getSite().equals("http://petrobitovki.ru/")) json.put("name","Tracker-BD");
            if (c.getSite().equals("http://petro-blok.ru/")) json.put("name","Tracker-BK");
            json.put("created_by", "VK-TRACKER");
            json.put("info_source", "TRACKER");
            json.put("note", "Client from " + c.getRef() + "\n" + c.getPhoneFirst() + "\n" + c.getPhoneSecond());

            jsonObjects.add(json.toString());
//            dbConnect.delete();
        }
    }

    public void send() throws IOException {
        for (String jsonToPost : jsonObjects){
//            System.out.println(jsonToPost);

//            HttpClient httpClient = HttpClientBuilder.create().build();
//            HttpPost request = new HttpPost("https://api.myfreshcloud.com/companies");
//            StringEntity params = new StringEntity(jsonToPost);
//            request.addHeader("authorization", "Basic MTg5Nzo3MFpxYThXaGhmZHM5RTF5RkJVX0pFb3NKWmZESlVXMQ==");
//            request.addHeader("content-type", "application/json;odata=verbose;charset=utf-8");
//            request.setEntity(params);
//            HttpResponse response = httpClient.execute(request);
        }
    }

    public void printJson(){
        System.out.println(jsonObjects);
    }
}
