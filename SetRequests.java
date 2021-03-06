
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SetRequests {

    private ArrayList<String> jsonObjects = new ArrayList<>();
    private ArrayList<String> jsonObjectsDeal = new ArrayList<>();
    private ArrayList<String> clientId_Site = new ArrayList<>();
    private ArrayList<String> clientIdToJson = new ArrayList<>();
    ClientDao dbConnect;

    public SetRequests(ClientDao dbConnect) {
        this.dbConnect = dbConnect;
    }

    public void createJSON() throws SQLException {
        List<Client> clients = dbConnect.getAll();
        System.out.println(clients);
        for (Client c : clients){
            JsonParser json = new JsonParser();
            if (c.getSite().equals("http://petrobani.ru/")){
                json.put("name","Tracker-MB " + c.getPhoneFirst());
                json.put("note", "Client from Tracker-MB" + "\n" + c.getPhoneFirst() + "\n" + c.getPhoneSecond());
                clientId_Site.add("Tracker-MB " + c.getPhoneFirst());
            }
            if (c.getSite().equals("http://petrobitovki.ru/")){
                json.put("name","Tracker-BD " + c.getPhoneFirst());
                json.put("note", "Client from Tracker-BD" + "\n" + c.getPhoneFirst() + "\n" + c.getPhoneSecond());
                clientId_Site.add("Tracker-BD " + c.getPhoneFirst());
            }
            if (c.getSite().equals("http://petro-blok.ru/")){
                json.put("name","Tracker-BK " + c.getPhoneFirst());
                json.put("note", "Client from Tracker-BK" + "\n" + c.getPhoneFirst() + "\n" + c.getPhoneSecond());
                clientId_Site.add("Tracker-BK " + c.getPhoneFirst());
            }
//            json.put("created_by", "VK-TRACKER");

            json.put("created_by", "API test");
            json.put("created", "2019-02-10T13:43:27.153");

            json.put("info_source", "TRACKER");
            json.put("type_id", "19");
            if (c.getPhoneSecond().equals("")){
                json.put("phones", "[{\\\"phone\\\":\\\"" + c.getPhoneFirst() + "\\\"}]");
            }else {
                json.put("phones", "[{\\\"phone\\\":\\\"" + c.getPhoneFirst() + "\\\"},{\\\"phone\\\":\\\"" + c.getPhoneSecond() + "\\\"}]");
            }

            jsonObjects.add(json.toString());
            dbConnect.delete();
        }
    }

    public void sendClient() throws IOException {
        for (String jsonToPost : jsonObjects){
            System.out.println(jsonToPost);
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost("https://api.myfreshcloud.com/companies");
            StringEntity params = new StringEntity(jsonToPost);
//            StringEntity params = new StringEntity("{\"name\":\"API test\",\"note\":\"Client from Tracker-MB\n" +
//                    "79213371972\n" +
//                    "\",\"created_by\":\"API test\",\"created\":\"2019-02-10T13:43:27.153\",\"info_source\":\"TRACKER\",\"type_id\":19,\"phones\":[{\"phone\":\"79213371972\"}]}");
            request.addHeader("authorization", "Basic MTg5Nzo3MFpxYThXaGhmZHM5RTF5RkJVX0pFb3NKWmZESlVXMQ==");
            request.addHeader("content-type", "application/json;odata=verbose;charset=utf-8");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println("status: " + response.getStatusLine());
        }
    }

    public void getClientId() throws IOException {
        InputStream is = null;
        List<String> id = new ArrayList<>();
        JsonParser parser = null;
        for (String cId : clientId_Site){
            HttpClient httpClient = HttpClientBuilder.create().disableContentCompression().build();
            HttpPost request = new HttpPost("https://api.myfreshcloud.com/companies?$filter=name eq '" + cId + "'");
            request.addHeader("authorization", "Basic MTg5Nzo3MFpxYThXaGhmZHM5RTF5RkJVX0pFb3NKWmZESlVXMQ==");
            request.addHeader("content-type", "application/json;odata=verbose;charset=utf-8");
            HttpResponse response = httpClient.execute(request);
            HttpEntity httpEntity = response.getEntity();
            is = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                System.out.println(line);
            }
            is.close();
            parser = new JsonParser(sb.toString());
            id.add(parser.get("id") + " " + cId);
            clientIdToJson.add(id.toString());
        }
    }

    public void createJsonToDeal(){
        for (String jsonToPost : clientIdToJson) {
            String [] delimit = jsonToPost.split(" ");
            String id = delimit[0];
            String site = delimit[1];
            JsonParser json = new JsonParser();
            json.put("id_status_deal", "1");
            json.put("id_type_deal", "10");
            json.put("id_company", id);
            if (site.contains("MB")) json.put("note", "roistat: tracker_mb");
            if (site.contains("BD")) json.put("note", "roistat: tracker_bd");
            if (site.contains("BK")) json.put("note", "roistat: tracker_bk");
            jsonObjectsDeal.add(json.toString());
        }
    }

    public void createDeal() throws IOException {
        for (String json : jsonObjectsDeal){
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost("https://api.myfreshcloud.com/deal");
            StringEntity params = new StringEntity(json);
            request.addHeader("authorization", "Basic MTg5Nzo3MFpxYThXaGhmZHM5RTF5RkJVX0pFb3NKWmZESlVXMQ==");
            request.addHeader("content-type", "application/json;odata=verbose;charset=utf-8");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
        }
    }

    public void printJson(){
        System.out.println(jsonObjects);
    }
}
