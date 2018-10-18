import DataBaseConnection.SqliteDbConnection;
import EntriesObject.AEntry;
import EntriesObject.User;

import java.sql.Date;


public class main {
    public static void main (String[] args){
        SqliteDbConnection test=new SqliteDbConnection(true);


        AEntry testEntry= new User("JhonTest","testPass", Date.valueOf("2010-01-01"),"testFN","testLN","testCity");

        test.createNewTable( testEntry);

        testEntry.insertToDb(test);
//        testEntry.deleteFromDb(test);
        for (String s :
                test.getEntryById("JhonTest",testEntry)) {
            System.out.println(s+",");
        }
        String[] testValues = {"JhonTest","password123","2010-01-02","yishaia","zabary","haifa"};
        test.updateEntry(testEntry,testValues);
        String[] fieldsTochangeTeat={"user_password","user_firstname"};
        test.getSpecificData(testEntry,"JhonTest",fieldsTochangeTeat);
        test.deleteAllFromTable("Users");
//        this is a test for the branch of tamir
//        for (String s :
//                test.getEntryById("Users","1",testEntry)) {
//            System.out.println(s+",");
//        }
//        for (String[] s:test.getAllFromTable(testEntry,"Users")
//             ) {
//            System.out.println("line:\n");
//            for (String sInner :
//                   s ) {
//                System.out.println(sInner+",");
//
//            }
//            System.out.println("\n");
//
//        }

    }

}
