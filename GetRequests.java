import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*класс принимает JSON строку, разбивает ее на подстроки с информацией об отдельном клиенте
* добавляет эти подстроки в лист listForQueue
* из listForQueue создается блокирующая очередь
*
* дальше потоки берут строки из очереди, обрабатывают их JSON парсером
* из обработанных строк создается объект Client и добавляется в clientsList
*
* метод getClientsList() используется для передачи объектов Client в класс SetRequest*/

public class GetRequests {

    public ArrayList<Client> clientsList = new ArrayList<>();
    BlockingQueue<String> clientsToParce;
    List<String> listForQueue;
    Timer timer = new Timer();

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
