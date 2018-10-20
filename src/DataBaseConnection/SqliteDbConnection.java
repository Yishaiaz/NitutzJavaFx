package DataBaseConnection;


import EntriesObject.IEntry;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;


public class SqliteDbConnection implements IdbConnection {

    private Properties props;
    private Connection conn;

    /**
     * //table name from entry
     * //no id - use username
     * creates a db using name from config.properties file
     */
    public SqliteDbConnection(boolean create) throws Exception {
        props = new Properties();
        String propFileName = "config.properties";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                props.load(inputStream);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
        }finally {
            if(create&& props!=null){

                String url = props.getProperty("dbUrl");

                try (Connection conn = DriverManager.getConnection(url)) {
                    if (conn != null) {
                        DatabaseMetaData meta = conn.getMetaData();
                        System.out.println("The driver name is " + meta.getDriverName());
                        System.out.println("A new database has been created.");
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public void connectToDb() throws Exception{
        conn = null;
        try {
            // db parameters
            String url = this.props.getProperty("dbUrl");
            // create a connection to the database
            conn = DriverManager.getConnection(url, "yishaiazabary", "Yi82Za65");

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            e.printStackTrace();
//            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void createNewTable(IEntry entry) throws Exception{
        this.connectToDb();

        // SQLite connection string

        String tableColumnsSql ="" ;

        //creating the sql string part with all the the column titles
        for (String str :
                entry.getColumnsTitles()) {
            tableColumnsSql += str + " text" + " NOT NULL,\n";
        }

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS "+entry.getTableName()+" ("
                +tableColumnsSql
                + "PRIMARY KEY ("+entry.getIdentifiers()+"));";

        try (Connection tempConn=this.conn;
             Statement stmt = tempConn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("//////////FOR DEBUGGING////////dont forget to connect to db! ");
//            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }


    }

    @Override
    public void insert(IEntry entry) throws Exception{
        this.connectToDb();

        String generatedId="";

        String fieldNamesForSql=createSqlStringColumns(entry);

        String fieldValuesForSql=createSqlStringValues(entry) ;

        String sql = "INSERT INTO "+entry.getTableName()+"("+fieldNamesForSql+") VALUES("+fieldValuesForSql+")";

        try (Connection conn = this.conn;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            generatedId += Integer.toString(pstmt.getGeneratedKeys().getInt(1));

        } catch (SQLException e) {
//            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }


    }

    @Override
    public String[] getEntryById(String entryId, IEntry entry)throws Exception {
        this.connectToDb();
        entryId="'"+entryId+"'";

        String[] ans=null;

        if (conn == null) {
            System.out.println("you have to connect to the DB first, use [dbInstance].connectToDb() function");
        }
        else{
            ans=new String[entry.getColumnsTitles().length];
            String sql = "SELECT * FROM "+entry.getTableName()+" WHERE "+entry.getIdentifiers()+"="+entryId+";";
            String[] columnsNames= entry.getColumnsTitles();
            try (Connection tempConn = this.conn;
                 Statement stmt  = tempConn.createStatement();
                 ResultSet rs    = stmt.executeQuery(sql)){
//                ans[0]=rs.getString(entry.getIdentifiers());
                for (int i = 0; i < columnsNames.length; i++) {
                    ans[i]=rs.getString(columnsNames[i]);
                }
            } catch (SQLException e) {
//                System.out.println(e.getMessage());
                throw new Exception(e.getMessage());
            }
            return ans;
        }
        return null;
    }


    @Override
    public void deleteAllFromTable(String tableName)throws Exception {
        this.connectToDb();

        String sql = "DELETE FROM "+tableName;

        try (Connection conn = this.conn;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
//            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void deleteDb(String dbName) {

    }

    @Override
    public void updateEntry(IEntry entry, String[] newValues)throws Exception {
        this.connectToDb();

        String fieldNamesForSql=createSqlStringForEditing(entry,newValues);
        //validate function and
        int i=0;

        String sql = "UPDATE "+entry.getTableName()+" SET "+fieldNamesForSql
                + " WHERE "+entry.getIdentifiers()+"='"+entry.getIdentifierValue()+"';";

        try (Connection tempConn = this.conn;
             PreparedStatement pstmt = tempConn.prepareStatement(sql)) {
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void deleteById(IEntry entry) throws Exception{
        this.connectToDb();

        String sql = "DELETE FROM "+entry.getTableName()+" WHERE "+entry.getIdentifiers()+" = " +"'"+entry.getIdentifierValue()+"';";

        try (Connection conn = this.conn;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
//            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


    @Override
    public LinkedList<String[]> getAllFromTable(IEntry entry) throws Exception{
        this.connectToDb();

        LinkedList<String[]> ans=new LinkedList<String[]>();

        String[] tempStringArray;

        String sql = "SELECT * FROM "+entry.getTableName();

        try (Connection tempConn = this.conn;
             Statement stmt  = tempConn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                String[] columns=entry.getColumnsTitles();
                tempStringArray = new String[columns.length+1];
                tempStringArray[0]=Integer.toString(rs.getInt("id"));
                for (int i = 1; i < columns.length+1; i++) {
                    tempStringArray[i]=rs.getString(columns[i-1]);
                }
                ans.add(tempStringArray);
            }
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }

        return ans;
    }

    @Override
    public ArrayList<String> getSpecificData(IEntry entry, String entryId, String[] namesOfSpecificField)throws Exception {
        ArrayList<String> ans=new ArrayList<>();
        this.connectToDb();
        entryId="'"+entryId+"'";

//        String[] allEntryData=getEntryById(entryId,entry);
        if (conn == null) {
            System.out.println("you have to connect to the DB first, use [dbInstance].connectToDb() function");
        }
        else{
            String sql = "SELECT * FROM "+entry.getTableName()+" WHERE "+entry.getIdentifiers()+"="+entryId+";";
            String[] columnsNames= entry.getColumnsTitles();
            try (Connection tempConn = this.conn;
                 Statement stmt  = tempConn.createStatement();
                 ResultSet rs    = stmt.executeQuery(sql)){
                for (int i = 0; i < namesOfSpecificField.length; i++) {
                    ans.add(rs.getString(namesOfSpecificField[i]));
                }
            } catch (SQLException e) {
//                System.out.println(e.getMessage());
                throw new Exception(e.getMessage());
            }
            return ans;
        }


        return null;
    }

    @Override
    public void closeConnection() throws Exception{
        try {
            if (this.conn != null) {
                this.conn.close();
            }

        } catch (SQLException e) {
//            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }




    private String createSqlStringColumns(IEntry entry){
        String ans="" ;
        for (String s:entry.getColumnsTitles()
        ) {
            ans+=s+",";
        }
        return ans.substring(0,ans.length()-1);
    }
    private String createSqlStringValues(IEntry entry) throws Exception{
        String ans="" ;
        for (String s:entry.getAllData()
        ) {
            ans+="'"+s+"',";
        }
        return ans.substring(0,ans.length()-1);
    }
    private String createSqlStringForEditing(IEntry entry,String[] newValues){
        String ans="";
        int i=0;
        String[] columns = entry.getColumnsTitles();
        for (String value :
                newValues) {
            ans+=columns[i]+"='"+value+"',";
            i++;
        }
        return ans.substring(0,ans.length()-1);
    }

}