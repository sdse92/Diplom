import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/*класс принимает JSON строку, разбивает ее на подстроки с информацией об отдельном клиенте
* добавляет эти подстроки в лист listForQueue
* из listForQueue создается блокирующая очередь
*
* дальше потоки берут строки из очереди, обрабатывают их JSON парсером
* из обработанных строк создается объект Client и добавляется в clientsList
*
* метод getClientsList() используется для передачи объектов Client в класс SetRequest*/

public class GetRequests {

    List<Client> clients = new CopyOnWriteArrayList<>();
    BlockingQueue<String> clientsToParce;
    List<String> listForQueue;
    ClientDao dbConnection;

    public String getAPIRequest(String uml) throws IOException {
        HttpClient client = HttpClientBuilder.create().disableContentCompression().build();
        HttpPost request = new HttpPost(uml);
        HttpResponse response = client.execute(request);
        HttpEntity httpEntity = response.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "utf-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString().replaceAll("\\[","").replaceAll("]","");
    }

    public void requestResult(String result){
        listForQueue = new ArrayList<>();
        String[] delimit = null;
        if (!result.equals("")) {
            String element = null;
            delimit = result.split("},");
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

    public void setDbConnection(ClientDao dbConnection) {
        this.dbConnection = dbConnection;
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
                    clients.add(client);
                    System.out.println("cli " + client);
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

    public void pushToDB() throws SQLException {
        for (Client clientToPush : clients){
            dbConnection.createTable();
            dbConnection.add(clientToPush);
        }
    }
}
