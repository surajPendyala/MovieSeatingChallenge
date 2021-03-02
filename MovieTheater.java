import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Movie Theater Class
 * @author Benjamin Cheung
 */
public class MovieTheater {
    private int rows;
    private int columns;
    private int availableSeats;
    private int[] center;
    private int[][] theater;
    private int[] numSeatsRow; // Number of available seats per row.

    public MovieTheater(int numRows, int numColumns) {
        rows = numRows;
        columns = numColumns;
        availableSeats = rows * columns;
        center = new int[]{rows / 2, columns / 2};
        theater = new int[rows][columns];
        for (int[] row: theater) {
            Arrays.fill(row, 1);
        }
        numSeatsRow = new int[rows];
        Arrays.fill(numSeatsRow, columns);
    }

    /**
     * Process the reservation requests. Prints the rejected requests(if any).
     * @return the processed requests
     * @param requests the list of requests
     */
    public List<String> processRequests(List<String> requests) {
        List<String> outputs = new ArrayList<>();
        for(String request: requests) {
            outputs.add(reserveSeats(request));
        }
        return outputs;
    }

    /**
     * Process a single reservation request.
     * @return string following the output format
     * @param reservation A single reservation request
     */
    private String reserveSeats(String reservation) {
        String output = null;
        String[] split = reservation.split(" ");
        int requestSeats = Integer.parseInt(split[1]);

        if (requestSeats > availableSeats) {
            return "Sorry, not enough seats is available for this request: " + split[0];
        } else if (requestSeats < 1) {
            return "For request: " + split[0] + ". Please reserve at least 1 seat";
        } else {
            output = split[0];
        }

        List<Integer[]> assignments = searchSeats(requestSeats);
        for (Integer[] assignment: assignments) {
            updateTheater(assignment[0], assignment[1], assignment[2], false);
            for (int i = 0; i < assignment[2]; i++) {
                int j = assignment[0] + 65;
                char c = (char)j;
                output = output + " "  + c + (assignment[1] + 1 + i);
            }
        }

        return output;
    }

    /**
     * Search for NUM number of seats. The search starts from the center row,
     * and recursively expanding to the upper and lower rows. For example, say the
     * theater has row A, B, C, D, and E, if we cannot find enough seats on row A, we will
     * search B and C next, if no seats are found we go search D and E.
     * If no consecutive seats are found, recursively split the party and search.
     * The priorities of searching are the following:
     *  1. distance from the center
     *  2. consecutiveness: sitting on the same row
     *
     * Output example:
     * [0, 0, 3] means reserved 3 seats starting from A1 (i.e. reserved A1, A2, and A3).
     * @return List of assigned seats
     * @param num number of requested seats
     */
    public List<Integer[]> searchSeats(int num) {
        List<Integer[]> assignedSeats = new ArrayList<>();
        int row = center[0], dist = 1;
        boolean upper = false, lower = false;
        while (row > -1 && row < rows) {
            int col = searchRow(row, num);
            if (col > -1) {
                assignedSeats.add(new Integer[]{row, col, num});
                break;
            } else if (!lower){
                lower = true;
                row = center[0] - dist;
            } else if (!upper) {
                upper = true;
                row = center[0] + dist;
            } else {
                dist++;
                upper = false; lower = false;
            }
        }

        if (assignedSeats.size() == 0) {
            assignedSeats.addAll(searchSeats(num / 2));
            for(Integer[] a: assignedSeats) {
                updateTheater(a[0], a[1], a[2], true);
            }
            assignedSeats.addAll(searchSeats(num - (num / 2)));
        }

        return assignedSeats;
    }

    /**
     * Search a ROW for NUM of seats
     * @return the starting column
     */
    public int searchRow(int row, int num) {
        if (numSeatsRow[row] < num) {
            return -1;
        }

        int left = 0, right = 0;
        while (left < columns && right < columns) {
            if (right - left + 1 == num) {
                return left;
            }
            if (theater[row][left] == 0) {
                left++;
                right = left;
            } else if (theater[row][right] == 1) {
                right++;
            } else {
                right++;
                left = right;
            }
        }

        return -1;
    }

    /**
     * Update the available seats in the theater according to the public
     * safety guidelines, which is a buffer of 3 seats and/or one row.
     */
    private void updateTheater(int row, int column, int num, boolean safe) {
        if (safe) {
            for (int i = 0; i < num; i++) {
                theater[row][column + i] = 0;
                availableSeats--;
                numSeatsRow[row]--;
            }
        } else {
            for (int c = column - 3; c < column + num + 3; c++) {
                if (c > -1 && c < columns && theater[row][c] == 1) {
                    theater[row][c] = 0;
                    availableSeats--;
                    numSeatsRow[row]--;
                }
            }

            for (int c = column - 1; c < column + num + 1; c++) {
                if (c > -1 && c < columns) {
                    if (row - 1 > -1 && theater[row - 1][c] == 1) {
                        theater[row - 1][c] = 0;
                        availableSeats--;
                        numSeatsRow[row - 1]--;
                    }
                    if (row + 1 < rows && theater[row + 1][c] == 1) {
                        theater[row + 1][c] = 0;
                        availableSeats--;
                        numSeatsRow[row + 1]--;
                    }
                }
            }
        }

    }

    /**
     * Returns the distance between point (i, j) and the center.
     */
    private double distanceFromCenter(int i, int j) {
        return twoPointDistance(i, j, center[0], center[1]);
    }

    /**
     * Returns the distance between point (i, j) and point (u, v).
     */
    private double twoPointDistance(int i, int j, int u, int v) {
        return Math.sqrt(Math.pow(i - u, 2) + Math.pow(j - v, 2));
    }

}

