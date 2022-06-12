import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    public static String readPropertyFiles(String path, String key) throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream(path);
        prop.load(input);
    //    System.out.println(prop.getProperty(key));
        return  prop.getProperty(key);
    }

    public static void main(String[] args) throws IOException {
        readPropertyFiles(System.getProperty("user.dir")+"/src/main/resources/TestData.properties", "userID");
        System.out.println(System.getProperty("user.dir"));
    }
}