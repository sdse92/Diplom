import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    private String phoneFirst;
    private String phoneSecond;
    private String ref;
    private String site;
    private String time;

    public Client(String phone, String ref, String site) {
        if (phone.length() > 11){
            this.phoneFirst = phone.substring(0,11);
            this.phoneSecond = phone.substring(11);
        }else {
            this.phoneFirst = phone;
            this.phoneSecond = "";
        }
        this.ref = ref;
        this.site = site;
        Date date = new Date();
        this.time = time(date);
    }

    public Client(String phoneFirst, String phoneSecond, String ref, String site, String time) {

    }

    public Client(String phoneFirst, String phoneSecond) {
    }

    public String getPhoneFirst() {
        return phoneFirst;
    }

    @Override
    public String toString() {
        return "Client{" +
                "phoneFirst='" + phoneFirst + '\'' +
                ", phoneSecond='" + phoneSecond + '\'' +
                ", ref='" + ref + '\'' +
                ", site='" + site + '\'' +
                ", time=" + time +
                '}';
    }

    private String time(Date date){
        SimpleDateFormat time = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        return time.format(date);
    }

    public String getPhoneSecond() {
        return phoneSecond;
    }

    public String getRef() {
        return ref;
    }

    public String getSite() {
        return site;
    }

    public String getTime() {
        return time;
    }


}
