import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
            get.requestResult("http://new.welcome-tracker.ru/api.php?api=71e5367021e4c6cf091f34434e5e9458&from="
                    + getTimeFrom() +  "&to=" + getTimeTo());

            int processCount = Runtime.getRuntime().availableProcessors();
            List<Thread> threads = new ArrayList<>();
            int threadCount;
            if (get.numberOfClients() > processCount){ threadCount = processCount; }
            else { threadCount = get.numberOfClients(); }

            for (int i = 0; i < threadCount; i++){
                threads.add(new Thread(new ThreadParser(get)));
            }

            for (int i = 0; i < threadCount; i++){
                threads.get(i).start();
            }

            for (int i = 0; i < threadCount; i++) {
                try {
                    threads.get(i).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

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
