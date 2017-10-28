import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Uses Floyd's Algorithm to find the shortest path of all nodes in a directed graph.
 * A correctly formatted file can be passed in as command line arguments, otherwise the user will be
 * prompted to enter the number of nodes and the direct cost of each edge in the directed graph.
 * @author Kyle Hewitt
 * @version 0.0.1
 */
public class Floyd {
    private int[][] dynamicTable;

    public static void main(String[] args){
        //This gives the option to pass in file as command line argument
        if(args.length != 0){
            File file = new File(args[0]);
            try {
                new Floyd(file);
            }catch (IOException ioe){
                System.out.println("Could not find specified file");
                System.exit(-1);
            }
        }else{
            new Floyd(new Scanner(System.in));
        }
    }

    /**
     * Reads directed graph information from a file. The file is expecting a format specifying how many edges are in
     * the graph. Each weight from a node to itself must be zero and -1 represents no connection.
     * @param f The file passed in containing the directed graph specification
     * @throws IOException
     */
    private Floyd(File f)throws IOException{
        Scanner reader = new Scanner(f);
        int tableSize = reader.nextInt()+1; //one extra spot for the row label
        int currentValue;
        dynamicTable = new int[tableSize][tableSize];
        for(int k = 0; k<tableSize; k++){ //fill in column/row labels
            dynamicTable[0][k] = k;
            dynamicTable[k][0] = k;
        }
        // reads the directed graph from a file and fills in table
        for(int i = 1; i<tableSize; i++) {
            for (int j = 1; j < tableSize; j++) {
                currentValue = reader.nextInt();
                if (i == j && currentValue != 0){ // checks that the weights to the same node is zero
                    System.out.println("Cost of a node to itself cannot be non-zero");
                    System.exit(-1);
                }
                else { // fill the table
                    dynamicTable[i][j] = currentValue;
                }
            }
        }
        int [][] linkTable = new int[tableSize][tableSize];
        printTable(0, linkTable);
        FloydsAlgorithm(1, linkTable);
    }

    /**
     * Takes directed graph information from the user in the console. Automatically fills in zero in node weights to
     * themselves. -1 represents no connection
     * @param scn Scanner used for user input
     */
    private Floyd(Scanner scn){
        System.out.print("Enter the number of edges: ");
        int tableSize = scn.nextInt()+1;
        dynamicTable = new int[tableSize][tableSize];
        for(int k = 0; k<tableSize; k++){
            dynamicTable[0][k] = k;
            dynamicTable[k][0] = k;
        }
        for(int i = 1; i<tableSize; i++){
            for(int j=1; j<tableSize; j++){
                if(i == j) dynamicTable[i][j] = 0;
                else {
                    System.out.print("Enter weight from node " + i + " to node " + j+": ");
                    dynamicTable[i][j] = scn.nextInt();

                }
            }
        }
        int [][] linkTable = new int[tableSize][tableSize];
        printTable(0, linkTable);
        FloydsAlgorithm(1, linkTable);
    }

    /**
     * Recursively calculates the shortest path from any node to any other node using Floyd's Algorithm. Each
     * recursive step is printed out to the console in a table.
     * @param D The recursive iteration representing the intermediate node
     * @param linkTable Table that represents the intermediate node to the lowest cost path
     */
    private void FloydsAlgorithm(int D, int[][] linkTable){
        if(D>=dynamicTable.length) return; //basecase
        //TODO       Floyd's Algorithm
        for(int i =1; i<linkTable.length; i++){
            if (i==D) continue;
            for(int j =1; j< linkTable.length; j++){
                if (j==D || i==j) continue;
                if(dynamicTable[i][D] == -1 || dynamicTable[D][j]== -1) continue;
                // if (from i to D + D to j) < i to j
                // then cost i -> j = i to D + D to j
                //and linkTable[i][j] = D
                else if(dynamicTable[i][D]+dynamicTable[D][j] != -1 && dynamicTable[i][j] == -1){
                    dynamicTable[i][j] = dynamicTable[i][D]+dynamicTable[D][j];
                    linkTable[i][j] = D;
                }
                else if(dynamicTable[i][D]+dynamicTable[D][j] < dynamicTable[i][j]){
                    dynamicTable[i][j] = dynamicTable[i][D]+dynamicTable[D][j];
                    linkTable[i][j] = D;
                }
            }
        }
        printTable(D, linkTable);
        FloydsAlgorithm(D+1, linkTable);

    }

    /**
     * Prints out the table of the calculated shortest path in each recursive iteration.
     * @param D The recursive iteration representing the intermediate node
     * @param linkTable Table that represents the intermediate node to the lowest cost path
     */
    private void printTable(int D, int[][] linkTable){
        System.out.println("\n\n\nD"+D+":");
        for(int i =0; i<dynamicTable.length;i++){
            for(int j=0; j<dynamicTable.length; j++){
                if(i==1 && j==0)
                    System.out.println("----------------------------------------------------------------------------" +
                            "--------------------------\n");
                if(i==0 && j==0)
                    System.out.print("   \t\t");
                else if(i==0 || j==0)
                    System.out.print(dynamicTable[i][j]+"\t\t");
                else if(dynamicTable[i][j] == -1)
                    System.out.print(Character.toString('\u221E')+"\t\t");
                else
                    System.out.print(dynamicTable[i][j]+" ("+linkTable[i][j]+")\t\t");
            }
            System.out.println();
        }
    }
}
