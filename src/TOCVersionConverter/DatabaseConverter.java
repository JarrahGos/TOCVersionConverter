package TOCVersionConverter;

/**
 * Created by jarrah on 19/03/15.
 */

import java.io.*;
import java.util.Scanner;


public class DatabaseConverter {
    private Scanner readOutFile;

    public final int writeOutDatabasePerson(Person persOut) { //TODO make the barcode work with this.
        String databaseLocation = "./personDatabase/";
        try {
            File check = new File(databaseLocation + persOut.getName());
            if(check.exists()) check.delete();
            check = new File(databaseLocation + persOut.getBarCode());
            if(check.exists()) check.delete();
            check = null;
            if(persOut.getBarCode() != 7000000) {
                FileOutputStream personOut = new FileOutputStream(databaseLocation + persOut.getName());
                ObjectOutputStream out = new ObjectOutputStream(personOut);
                out.writeObject(persOut);
                out.close();
                personOut.close();
            }
            // it may be quicker to do this with the java.properties setup that I have made. The code for that will sit unused in settings.java.
            FileOutputStream personOut1 = new FileOutputStream(databaseLocation + persOut.getBarCode());
            ObjectOutputStream out1 = new ObjectOutputStream(personOut1);
            out1.writeObject(persOut);
            out1.close();
            personOut1.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }
    public final int readDatabasePeople(String path) {
        String tempName, tempInput;
        long tempTotalCostRunning, tempTotalCostWeek;
        double doubleCosts;
        int tempBarCode;
        int count = 0;
        boolean tempCanBuy;
        int z;
        try {
            File file = new File(path); // if this fails, chances are the user hit 2 and imput a file that doesn't exist.
            readOutFile = new Scanner(file); // create the scanner
            readOutFile.nextLine(); // exclude the header of the file
            readOutFile.nextLine(); // exclude dashes
            tempInput = readOutFile.nextLine();
            tempBarCode = Integer.parseInt(tempInput);
            tempName = readOutFile.nextLine();
           // admin = new Person(tempName, tempBarCode, 0, 0, true);
            for (z = 0; readOutFile.hasNext(); z++) { // until all of the lines have been read, I want to read the lines.
                readOutFile.nextLine(); // someone decided to put a redundant line in each person of the file, this throws it away.
                tempInput = readOutFile.nextLine();
                tempBarCode = Integer.parseInt(tempInput);
                tempName = readOutFile.nextLine();
                doubleCosts = Double.parseDouble(readOutFile.nextLine());
                tempTotalCostRunning = (long)(doubleCosts*100);
                doubleCosts = Double.parseDouble(readOutFile.nextLine());
                tempTotalCostWeek = (long)(doubleCosts*100);
                tempInput = readOutFile.nextLine();
                tempCanBuy = Boolean.parseBoolean(tempInput);
                writeOutDatabasePerson(new Person(tempName, tempTotalCostRunning, tempTotalCostWeek, tempBarCode, tempCanBuy)); // send the big pile of lines that we just read to the person constructor.
            }
            readOutFile.close(); // clean up by closing the file
            return z - count; // tell the program how many persons we just got. If it's more than a thousand, I hope the sort doesn't take too long.
        } catch (FileNotFoundException e) {
            readOutFile.close(); // Well, if something goes wrong, someone should find out.
            return -1; // this is what we use to tell them that something we didn't expect happened. Like the user assuring me that the file exists.
        }
    }
    public final int writeOutDatabaseProduct(Product productOut) {
        String databaseLocation = "./productDatabase/";
        try {
            File check = new File(databaseLocation + productOut.getName());
            if(check.exists()) check.delete();
            check = new File(databaseLocation + productOut.getBarCode());
            if(check.exists()) check.delete();
            check = null;
            FileOutputStream personOut = new FileOutputStream(databaseLocation + productOut.getName());
            ObjectOutputStream out = new ObjectOutputStream(personOut);
            out.writeObject(productOut);
            out.close();
            personOut.close();
            FileOutputStream personOut1 = new FileOutputStream(databaseLocation + productOut.getBarCode());
            ObjectOutputStream out1 = new ObjectOutputStream(personOut1);
            out1.writeObject(productOut);
            out1.close();
            personOut.close();
        }
        catch (Exception e) {
            System.out.println(e);
            return 1;
        }
        return 0;
    }
    public final int readDatabaseProducts(String path)
    {
        String tempName, tempInput;
        Product tempProduct;
        long tempProductPrice;
        double doubleProductPrice;
        long tempBarCode;
        int tempNumberOfProduct;
        boolean negative = false;
        int count = 0;
        int z = 0;
        Scanner readOutFile = null;
        try {
            File file = new File(path); // if this fails, chances are the user hit 2 and imput a file that doesn't exist.
            readOutFile = new Scanner(file); // create the scanner
            readOutFile.nextLine(); // header
            for(z = 0; readOutFile.hasNext(); z++) { // until all of the lines have been read, I want to read the lines.
                readOutFile.nextLine(); // someone decided to put a redundant line in each product of the file, this throws it away.
                tempName = readOutFile.nextLine();
                doubleProductPrice = Double.parseDouble(readOutFile.nextLine());
                tempProductPrice = (long)(doubleProductPrice*100);
                tempBarCode = Long.parseLong(readOutFile.nextLine());
                tempInput = readOutFile.nextLine();
                if('-' == tempInput.charAt(0)) {
                    tempInput = tempInput.substring(1);
                    negative = true;
                }
                tempNumberOfProduct = Integer.parseInt(tempInput);
                if(negative) {
                    tempNumberOfProduct *= -1;
                    negative = false;
                }
                tempProduct = new Product(tempName, tempProductPrice, tempBarCode); // send the big pile of lines that we just read to the product constructor.
                tempProduct.setNumber(tempNumberOfProduct);
                writeOutDatabaseProduct(tempProduct);
            }
            readOutFile.close(); // clean up by closing the file
            return z - count; // tell the program how many products we just got. If it's more than a thousand, I hope the sort doesn't take too long.
        }
        catch(FileNotFoundException e) {
            if (readOutFile != null) readOutFile.close(); // Well, if something goes wrong, someone should find out.
            return -1; // this is what we use to tell them that something we didn't expect happened. Like the user assuring me that the file exists.
        }
    }
    public static void main(String[] args)
    {
        DatabaseConverter toc = new DatabaseConverter();
        toc.readDatabasePeople("personDatabase.txt");
        toc.readDatabaseProducts("productDatabase.txt");
        System.out.println("databases rewritten");
    }
}
