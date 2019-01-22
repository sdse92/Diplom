import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class ThreadParser implements Runnable{

    GetRequests get;

    public ThreadParser(GetRequests get) {
        this.get = get;
    }

    @Override
    public void run() {
       get.createClients();
    }
}
