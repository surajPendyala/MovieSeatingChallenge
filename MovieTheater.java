package MovieTheaterSeatingChallenge;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class MovieTheater {
    private int rows;
    private int columns;
    private int seats;
    private int[] center;
    private int[][] theater;
    private int[] seatsPerRow;

    public MovieTheater(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.seats = rows * columns;
        this.center = new int[]{rows / 2, columns / 2};
        this.theater = new int[rows][columns];
        for (int[] row: theater) {
            Arrays.fill(row, 1);
        }
        this.seatsPerRow = new int[rows];
        Arrays.fill(seatsPerRow, columns);
    }

    /**
     * Process the reservation requests. Prints the rejected requests(if any).
     * @return the processed requests
     * @param requests the list of requests
     */
    public List<String> takeRequests(List<String> requests) {
        ArrayList<String> listOfRequests = new ArrayList<>();
        for(String request: requests) {
            listOfRequests.add(reserveSeats(request));
        }
        return listOfRequests;
    }

    /**
     * Processes a reservation request by user
     */
    private String reserveSeats(String reservation) {
        String str = null;
        String[] split = reservation.split(" ");
        int requestedSeats = Integer.parseInt(split[1]);

        if (requestedSeats > seats) {
            return "Sorry, there are no seats left for this request " + split[0];
        } else if (requestedSeats < 1) {
            return "For request: " + split[0] + ". Please reserve at least one seat. Thank you!";
        } else {
            str = split[0];
        }

        List<Integer[]> assigned = searchSeats(requestedSeats);
        for (Integer[] assignment: assigned) {
            updateTheater(assignment[0], assignment[1], assignment[2], false);
            for (int i = 0; i < assignment[2]; i++) {
                int j = assignment[0] + 65;
                char c = (char)j;
                str = str + " "  + c + (assignment[1] + 1 + i);
            }
        }

        return str;
    }

    /**
     * Search for the number of seats. The search starts from the center row,
     * and recursively expands to the other rows. 
     * The priorities of searching are the following:
     *  1. distance from the center
     *  2. Linearity: sitting on the same row
     */
    public List<Integer[]> searchSeats(int num) {
        List<Integer[]> seats = new ArrayList<>();
        int row = center[0];
        int distance = 1;
        boolean upper = false;
        boolean lower = false;
        while (row > -1 && row < rows) {
            int col = searchRow(row, num);
            if (col > -1) {
                seats.add(new Integer[]{row, col, num});
                break;
            } else if (!lower){
                lower = true;
                row = center[0] - distance;
            } else if (!upper) {
                upper = true;
                row = center[0] + distance;
            } else {
                distance++;
                upper = false; lower = false;
            }
        }

        if (seats.size() == 0) {
            seats.addAll(searchSeats(num / 2));
            for(Integer[] a: seats) {
                updateTheater(a[0], a[1], a[2], true);
            }
            seats.addAll(searchSeats(num - (num / 2)));
        }

        return seats;
    }

    /**
    Searches the row for number of seats
     */
    public int searchRow(int row, int num) {
        if (seatsPerRow[row] < num) {
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
     * Update the available seats in the theater (follows COVID-19 protocols)
     */
    public void updateTheater(int row, int col, int num, boolean safe) {
        if (safe) {
            for (int i = 0; i < num; i++) {
                theater[row][col + i] = 0;
                seats--;
                seatsPerRow[row]--;
            }
        } else {
            for (int c = col - 3; c < col + num + 3; c++) {
                if (c > -1 && c < columns && theater[row][c] == 1) {
                    theater[row][c] = 0;
                    seats--;
                    seatsPerRow[row]--;
                }
            }

            for (int c = col - 1; c < col + num + 1; c++) {
                if (c > -1 && c < columns) {
                    if (row - 1 > -1 && theater[row - 1][c] == 1) {
                        theater[row - 1][c] = 0;
                        seats--;
                        seatsPerRow[row - 1]--;
                    }
                    if (row + 1 < rows && theater[row + 1][c] == 1) {
                        theater[row + 1][c] = 0;
                        seats--;
                        seatsPerRow[row + 1]--;
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
     * Returns the distance between point two points
     */
    private double twoPointDistance(int i, int j, int k, int l) {
        return Math.sqrt(Math.pow(i - k, 2) + Math.pow(j - l, 2));
    }
}
