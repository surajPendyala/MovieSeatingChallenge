import java.io.*;
import java.nio.file.*;
import java.util.*;
import static java.nio.file.StandardOpenOption.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Please enter the file path");
        Scanner input = new Scanner(System.in);
        String path = input.next();
        Path file = Paths.get(path);

        List<String> requests = new ArrayList<>();
        try (InputStream in = Files.newInputStream(file); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requests.add(line);
            }
        } catch (IOException e) {
            System.err.println(e);
        }

        MovieTheater mt = new MovieTheater(10, 20);
        List<String> outputs = mt.processRequests(requests);
        String concatString = "";
        for(String output: outputs) {
            concatString += output + "\n";
        }

        byte[] data = concatString.getBytes();
        path = path.replaceAll("\\.[a-z]+", ".output");
        System.out.println(path);
        Path p = Paths.get(path);

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
