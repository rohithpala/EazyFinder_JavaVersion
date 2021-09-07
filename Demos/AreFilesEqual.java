package EazyFinderGUI.Demos;

import java.io.*;
import java.util.Scanner;

public class AreFilesEqual {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Path of file 1: ");
        String path1 = input.next();
        System.out.println("Path of file 2: ");
        String path2 = input.next();

        File file1 = new File(path1);
        File file2 = new File(path2);
        if (file1.length() != file2.length()) {
            System.out.println("not same");
        } else {
            try {
                BufferedReader reader1 = new BufferedReader(new FileReader(file1));
                BufferedReader reader2 = new BufferedReader(new FileReader(file2));
                String str;
                StringBuilder buf1 = new StringBuilder();
                StringBuilder buf2 = new StringBuilder();
                while ((str = reader1.readLine()) != null) buf1.append(str);
                while ((str = reader2.readLine()) != null) buf2.append(str);
                if (String.valueOf(buf1).equals(String.valueOf(buf2))) System.out.println("same");
                else System.out.println("not same");
                reader1.close();
                reader2.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
