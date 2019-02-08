import java.sql.SQLException;

public class Connect {

    public ClientDao connectDB() throws SQLException {
        ClientDao dbConnection = new ClientDao();
        return dbConnection;
    }
}
