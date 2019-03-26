import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jade van Dinter
 * @version 1.0
 * @since 23-03-2019
 */
public class Database {

    /**
     * The function checkDatabaseInfo makes contact with the owe7_pg5 database. 
     * 
     * @return ResultSet rs, contains the searched data from the database.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static ArrayList<ArrayList<String>> checkDatabaseORFInfo(String inputHeader) throws ClassNotFoundException, SQLException {
        ArrayList<ArrayList<String>> headerResultList = new ArrayList<ArrayList<String>>();
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com:3306/owe7_pg5?serverTimezone=UTC",
                "owe7_pg5@hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
                "blaat1234");

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select ORF_start, ORF_stop, Reading_frame from orf o\n" +
                "left outer join sequence s on o.Sequence_ID = s.Sequence_ID\n" +
                "where Header like '%"+inputHeader+"%';");
        
        headerResultList = getHeaderORFResults(rs);

        con.close();
        return headerResultList;
    }

    /**
     * The function getHeaderResults saves the database results in a Arraylist.
     *
     * @param rs contains the searched data from the database.
     * @return ArrayList<ArrayList<String>> headerResultList, contains the searched data from the database.
     * @throws SQLException
     */
    public static ArrayList<ArrayList<String>> getHeaderORFResults(ResultSet rs) throws SQLException {
        ArrayList<ArrayList<String>> headerORFResultList = new ArrayList<ArrayList<String>>();
        while (rs.next()) {
            ArrayList<String> headerORFResult = new ArrayList<String>();
            headerORFResult.clear();
            headerORFResult.add(Integer.toString(rs.getInt(1)));
            headerORFResult.add(Integer.toString(rs.getInt(2)));
            headerORFResult.add(rs.getString(3));
            headerORFResultList.add(headerORFResult);
        }
        return headerORFResultList;
    }

    /**
     * The fucntion getDatabaseBLASTInfo makes contact with the owe7_pg5 database ands get al BLAST hits for a certain ORF.
     * @param ORFID The ID of the selected ORF.
     * @return headerResultBLASTList is an 2D Arraylist containing all ORF info for the chosen file.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static ArrayList<ArrayList<String>> getDatabaseBLASTInfo(Integer ORFID) throws ClassNotFoundException, SQLException {
        ORFID = 1;
        ArrayList<ArrayList<String>> headerResultBLASTList = new ArrayList<ArrayList<String>>();
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com:3306/owe7_pg5?serverTimezone=UTC",
                "owe7_pg5@hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
                "blaat1234");

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select BLAST_start, BLAST_stop, Percentage_identity, E_value from blast_results\n" +
                "left outer join orf o on blast_results.ORF_ID = o.ORF_ID\n" +
                "where o.ORF_ID = '"+ORFID+"';");

        headerResultBLASTList = getHeaderBLASTResults(rs);

        con.close();
        return headerResultBLASTList;
    }

    public static ArrayList<ArrayList<String>> getHeaderBLASTResults(ResultSet rs) throws SQLException {
        ArrayList<ArrayList<String>> headerBLASTResultList = new ArrayList<ArrayList<String>>();
        while (rs.next()) {
            ArrayList<String> headerBLASTResult = new ArrayList<String>();
            headerBLASTResult.clear();
            headerBLASTResult.add(Integer.toString(rs.getInt(1)));
            headerBLASTResult.add(Integer.toString(rs.getInt(2)));
            headerBLASTResult.add(Integer.toString(rs.getInt(3)));
            headerBLASTResult.add(String.valueOf(Float.parseFloat(rs.getString(4))));
            headerBLASTResultList.add(headerBLASTResult);
        }
        return headerBLASTResultList;
    }


    /**
     * The function arraylistToArray converts an arraylist to an 2D Array.
     * @param arraylist an arrylist that needs to be converted to an Array.
     * @return headerResultsArray is an 2D Array that contains the information of the given Arraylist./
     */
    public static String[][] arraylistToArray(ArrayList<ArrayList<String>> arraylist){
        String[][] headerResultsArray = new String[arraylist.size()][];
        for (int i = 0; i < arraylist.size(); i++) {
            ArrayList<String> row = arraylist.get(i);
            headerResultsArray[i] = row.toArray(new String[row.size()]);
        }
        return headerResultsArray;
    }



    /**
     * The function insertIntoDatabase inserts the found data in the database.
     * 
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void insertIntoDatabase(ArrayList<ORF> foundORFS) throws ClassNotFoundException, SQLException{
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com:3306/owe7_pg5?serverTimezone=UTC",
                "owe7_pg5@hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
                "blaat1234");

        Statement stmt = con.createStatement();
        stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
        stmt.executeUpdate("insert into sequence(sequence_id, sequence, header) values (10, 'ALJKD', '>B283 chromosoom I');");
        stmt.executeUpdate("insert into orf(orf_id, orf_start, orf_stop, reading_frame, sequence_id) values (8, 72389, 23982, '+2', 10);\n");
        stmt.executeUpdate("insert into blast_results(blast_id, blast_stop, blast_start, percentage_identity, e_value, orf_id, gene_id) values (6, 2345, 123, 56, 0.98, 8, 2);");
        stmt.executeUpdate("insert into gene(gene_id, gene_name, function_id) VALUES (2, 'Hemoglobine', 2);");
        stmt.executeUpdate("insert into gene_function(function_id, function) VALUES (2, 'zuurstofbindend');");

        con.close();
       
    }
}
