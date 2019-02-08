import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExceptionLogger {

    File file;
    File filePath;
    FileWriter fWriter;

    public void create(){
        file = new File("exceptions.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String exception){
        try {
            System.out.println(exception);
            Date date = new Date();
            SimpleDateFormat time = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
            fWriter = new FileWriter(file, true);
            fWriter.write(time.format(date) + "\n" + exception + "\n");
            fWriter.flush();
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
