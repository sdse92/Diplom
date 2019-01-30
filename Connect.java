import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class Connect {

    public HttpURLConnection connectApiFrom(String urlToRead) throws IOException {
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        return conn;
    }

    public ClientDao connectDB() throws SQLException {
        ClientDao dbConnection = new ClientDao();
        return dbConnection;
    }
}
