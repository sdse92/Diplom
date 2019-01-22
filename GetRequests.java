import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//добавить поле roistat

public class GetRequests {

    public ArrayList<Client> clientsList = new ArrayList<>();
    BlockingQueue<String> clientsToParce;
    List<String> listForQueue;
    Timer timer = new Timer();

    private String getHTMLrequest(String urlToRead) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.replaceAll("\\[","").replaceAll("]","");
    }

    public void requestResult(String url){
        String requestResult = getHTMLrequest(url);
        listForQueue = new ArrayList<>();
        String[] delimit = null;
        if (!requestResult.equals("")) {
            String element = null;
            delimit = requestResult.split("},");
            for (int i = 0; i < delimit.length; i++) {
                StringBuilder sb = new StringBuilder();
                element = delimit[i].replaceAll("}", "");
                sb = sb.append(element).append("}");
                element = sb.toString();
                listForQueue.add(element);
                listForQueue.add("point");
            }
        }
        clientsToParce = new ArrayBlockingQueue<>(listForQueue.size(), true, listForQueue);
    }

    public int numberOfClients(){
        return clientsToParce.size()/2;
    }

    public void createClients(){
        String c;
        try {
            while (!(c = clientsToParce.take()).equals("point")) {
                if (c != null){
                    JsonParser json = new JsonParser(c);
                    String phone = phone(json.get("phone").toString());
                    String ref = ref(json.get("ref").toString());
                    String site = site(json.get("site").toString());
                    Client client = new Client(phone, ref, site);
                    clientsList.add(client);
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private String phone(String s){
        String rez = s.replaceAll("pP","").replaceAll("\\D","");
        if (rez.length() < 11){
            rez = "";
        }
        return rez;
    }

    private String ref(String s){
        String rez = s;
        return rez;
    }

    private String site(String s){
        String rez = "";
        if (s.contains("petrobani.ru")){
            rez = "http://petrobani.ru/";
        }
        if (s.contains("petrobitovki.ru")){
            rez = "http://petrobitovki.ru/";
        }
        if (s.contains("petro-blok.ru")){
            rez = "http://petro-blok.ru/";
        }
        return rez;
    }

    public ArrayList<Client> getClientsList() {
        return clientsList;
    }
}
