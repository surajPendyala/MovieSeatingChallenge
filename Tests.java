import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class Tests {
    @Test
    public void basicRequest() {
        MovieTheater t = new MovieTheater(3, 3);
        List<String> requests = new ArrayList<>();
        requests.add("R001 1");
        List<String> outputs = t.processRequests(requests);
        assertEquals("R001 B1", outputs.get(0));
    }

    @Test
    public void requestZeroSeats() {
        MovieTheater t = new MovieTheater(3, 3);
        List<String> requests = new ArrayList<>();
        requests.add("R001 0");
        List<String> outputs = t.processRequests(requests);
        assertEquals("For request: R001. Please reserve at least 1 seat", outputs.get(0));
    }

    @Test
    public void theaterFull() {
        MovieTheater t1 = new MovieTheater(1, 1);
        List<String> requests = new ArrayList<>();
        requests.add("R001 1");
        requests.add("R002 1");
        List<String> outputs = t1.processRequests(requests);
        assertEquals("R001 A1", outputs.get(0));
        assertEquals("Sorry, not enough seats is available for this request: R002", outputs.get(1));

        MovieTheater t2 = new MovieTheater(3, 3);
        requests = new ArrayList<>();
        requests.add("R001 4");
        requests.add("R002 6");
        outputs = t2.processRequests(requests);
        assertEquals("Sorry, not enough seats is available for this request: R002", outputs.get(1));
    }

    @Test
    public void consecutiveSeats() {
        MovieTheater t = new MovieTheater(4, 4);
        List<String> requests = new ArrayList<>();
        requests.add("R001 4");
        List<String> outputs = t.processRequests(requests);
        assertEquals("R001 C1 C2 C3 C4", outputs.get(0));
    }

    @Test
    public void splitParty() {
        MovieTheater t = new MovieTheater(2, 2);
        List<String> requests = new ArrayList<>();
        requests.add("R001 4");
        List<String> outputs = t.processRequests(requests);
        assertEquals("R001 B1 B2 A1 A2", outputs.get(0));
    }

    @Test
    public void recursiveSplit() {
        MovieTheater t = new MovieTheater(5, 1);
        List<String> requests = new ArrayList<>();
        requests.add("R001 5");
        List<String> outputs = t.processRequests(requests);
        assertEquals("R001 C1 B1 D1 A1 E1", outputs.get(0));
    }


    @Test
    public void updateAndSplit() {
        MovieTheater t = new MovieTheater(3, 3);
        List<String> requests = new ArrayList<>();
        requests.add("R001 1");
        requests.add("R002 2");
        List<String> outputs = t.processRequests(requests);
        assertEquals("R001 B1", outputs.get(0));
        assertEquals("R002 A3 C3", outputs.get(1));
    }

    @Test
    public void closerToCenter() {
        MovieTheater t = new MovieTheater(5, 5);
        List<String> requests = new ArrayList<>();
        requests.add("R001 1");
        requests.add("R002 2");
        requests.add("R003 2");
        List<String> outputs = t.processRequests(requests);
        assertEquals("R001 C1", outputs.get(0));
        assertEquals("R002 B3 B4", outputs.get(1));
        assertEquals("R003 D3 D4", outputs.get(2));
    }
}
