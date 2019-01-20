import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class GetRequests {

    private ArrayList<Client> clientsList = new ArrayList<>();
    Timer timer = new Timer();

    public String getHTMLrequest(String urlToRead) {
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
        result = result.replaceAll("\\[","").replaceAll("]","");
        return result;
    }

    public void Result(long timeFrom, long timeTo){
//        String URL = "http://new.welcome-tracker.ru/api.php?api=71e5367021e4c6cf091f34434e5e9458&from="
//                + timer.getTimeFrom() +  "&to=" + timer.getTimeTo();
        String URL = "http://new.welcome-tracker.ru/api.php?api=71e5367021e4c6cf091f34434e5e9458&from="
                + timeFrom +  "&to=" + timeTo;
        String result = getHTMLrequest(URL);
        if (!result.equals("")){
            String element = null;
            String[] delimit = result.split("},");
            for (int i = 0; i < delimit.length; i++) {
                StringBuilder sb = new StringBuilder();
                element = delimit[i].replaceAll("}", "");
                sb = sb.append(element).append("}");
                element = sb.toString();
                createClients(element);
            }
        }
    }

    public void createClients(String s){
        if (s != null){
            JsonParser json = new JsonParser(s);
//                    System.out.println(json);
            String phone = phone(json.get("phone").toString());
            String ref = ref(json.get("ref").toString());
            String site = site(json.get("site").toString());
            Client client = new Client(phone, ref, site);
//                    System.out.println(client);
            clientsList.add(client);
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
        System.out.println(s);
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
