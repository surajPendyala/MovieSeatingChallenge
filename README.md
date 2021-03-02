# Movie Theater Seating Challenge
Programming Language: Java

Challenge Overview:
This program is an algorithm for assigning seats within a movie theater to fulfill reservation requests. The movie theater has the seating arrangement of 10 rows x 20 seats. This program maximizes both customer satisfaction and customer safety. For the purpose of public safety, assume that a buffer of three seats and/or one row is required.

Input Description:
The input should be a file that contains one line of input for each reservation request. The order of the lines in the file reflects the order in which the reservation requests were received. Each line in the file will be comprised of a reservation identifier, followed by a space, and then the number of seats requested. The reservation identifier will have the format: R####. For example:

R001 2
R002 5
R003 7
R004 3

Output Description:
The program outputs a file containing the seating assignments for each request. Each row in the file includes the reservation number followed by a space, and then a comma-delimited list of the assigned seats. For example:

R001 I1,I2
R002 F16,F17,F18,F19
R003 A1,A2,A3,A4
R004 J4,J5,J6
Assumptions:

When there's not enough seats for a reservation, the program outputs the assigned seats and rejects the reservation and its following reservation(s).
The reservations are processed in order, and seat assignments cannot be changed once they are assigned.
The program does not maximize seat utilization.
The input follows the requirements described about.
The algorithm is based on assumptions of customers' psychology: closer to the center is better; sitting in consecutive seats is better than splitting.
The members of a party do not need to follow the COVID protocols, even when they are split.
Ouput assignments do not need to be in  order.



