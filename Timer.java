import java.util.Date;
import java.util.TimerTask;

public class Timer {

//    private long timeFrom = 1544511600;
//    private long timeTo = 1544518800;

    private long timeFrom = 1544515200;
    private long timeTo = 1544518800;

    public long getTimeFrom() {
        return timeFrom;
    }

    public long getTimeTo() {
        return timeTo;
    }

    public void setTimeFrom(long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public void setTimeTo(long timeTo) {
        this.timeTo = timeTo;
    }

    public void start(){
        while (true){

            System.out.println(getTimeFrom() + " " + getTimeTo());
            GetRequests get = new GetRequests();
            get.Result(getTimeFrom(), getTimeTo());
            SetRequests set = new SetRequests(get);
            set.createJSON();
            set.printJson();
            setTimeTo(getTimeTo() - 3600);
            setTimeFrom(getTimeFrom() - 3600);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
