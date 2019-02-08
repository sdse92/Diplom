import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class Timer {

//    private long timeFrom = 1544511600;
//    private long timeTo = 1544518800;

    private long timeFrom = 1544515200;
    private long timeTo = 1544518800;
    private String getRequestResult = null;

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
            ExceptionLogger exception = new ExceptionLogger();
            exception.create();
            Connect connector = new Connect();

            GetRequests get = new GetRequests();
            try {
                getRequestResult = get.getAPIRequest("http://new.welcome-tracker.ru/api.php?api=71e5367021e4c6cf091f34434e5e9458&from="
                        + getTimeFrom() +  "&to=" + getTimeTo());

//                getRequestResult = get.getAPIRequest("http://new.welcome-tracker.ru/api.php?api=71e5367021e4c6cf091f34434e5e9458&from=1544508000"
//                                +  "&to=1544518800");
            } catch (IOException e) {
                exception.write(e.toString());
                try {
                    waitToUptime();
                } catch (InterruptedException e1) {
                    exception.write(e1.toString());
                }
                increaseTimeForRequest();
                start();
            }

            if (getRequestResult.equals("")){
                try {
                    waitToUptime();
                } catch (InterruptedException e) {
                    exception.write(e.toString());
                }
                increaseTimeForRequest();
                start();
            }

            get.requestResult(getRequestResult);
            try {
                get.setDbConnection(connector.connectDB());
            } catch (SQLException e) {
                exception.write(e.toString());
            }

            int processCount = Runtime.getRuntime().availableProcessors();
            List<Thread> threads = new ArrayList<>();
            int threadCount = get.numberOfClients();
//            if (get.numberOfClients() > processCount){ threadCount = processCount; }
//            else { threadCount = get.numberOfClients(); }
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
                    exception.write(e.toString());
                }
            }

            try {
                get.pushToDB();
            } catch (SQLException e) {
                exception.write(e.toString());
            }

            SetRequests set = null;
            try {
                set = new SetRequests(connector.connectDB());
                set.createJSON();
//                set.printJson();
                set.sendClient();
            } catch (SQLException | IOException e) {
                exception.write(e.toString());
            }

            setTimeTo(getTimeTo() - 3600);
            setTimeFrom(getTimeFrom() - 3600);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                exception.write(e.toString());
            }
        }
    }

    private void waitToUptime() throws InterruptedException {
            Thread.sleep(2000);
    }

    private void increaseTime(){
        setTimeTo(getTimeTo() + 3600);
        setTimeFrom(getTimeFrom() + 3600);
    }

    private void decreaseTime(){
        //уменшение на час, тест
        setTimeTo(getTimeTo() - 3600);
        setTimeFrom(getTimeFrom() - 3600);
        //уменьшение на день, тест
    }

    private void increaseTimeForRequest(){
//        setTimeTo(getTimeTo() + 3600);
        //уменшение на час, тест
        setTimeFrom(getTimeFrom() - 3600);
        //уменшение на день, тест
//        setTimeTo(getTimeFrom() - 86400);
    }
}
