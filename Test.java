package utilities;

import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Test {
    public static void main(String[] args) throws FileNotFoundException{
    //File f = new File("/Users/paul/Downloads/saveMeTb.csv")
    //Scanner s = new Scanner(new BufferedReader(new FileReader(f)));
        String s = "1, \"hello\", \"second\", \"third\", 2";
        String [] q = s.split(",");
        for (int i = 0; i < q.length; i++) {
            System.out.println(q[i]);
        }
        String[] d = s.replaceAll("\"", "").split(",");
        System.out.println("=========Break=========");
        for (int i = 0; i < d.length; i++) {
            System.out.println(d[i]);
        }
    
    }
}
