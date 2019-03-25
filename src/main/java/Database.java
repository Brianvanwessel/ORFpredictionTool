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

    public static void main(String[] args){
        try {
            ResultSet rs = checkDatabaseInfo();
            insertIntoDatabase();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The function checkDatabaseInfo makes contact with the owe7_pg5 database. 
     * 
     * @return ResultSet rs, contains the searched data from the database.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static ResultSet checkDatabaseInfo() throws ClassNotFoundException, SQLException {
        String inputheader = ">A25";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com:3306/owe7_pg5?serverTimezone=UTC",
                "owe7_pg5@hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
                "blaat1234");

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from blast_results right outer join orf o on blast_results.ORF_ID = o.ORF_ID\n"
            + "right outer join sequence s on o.Sequence_ID = s.Sequence_ID\n"
            + "left outer join gene g on blast_results.Gene_ID = g.Gene_ID\n"
            + "left outer join gene_function gf on g.Function_ID = gf.Function_ID\n"
            + "where Header like '%" + inputheader + "%';");
        
        ArrayList<ArrayList<String>> headerResultList = getHeaderResults(rs);

        con.close();
        return rs;
    }

    /**
     * The function getHeaderResults saves the database results in a Arraylist.
     *
     * @param rs contains the searched data from the database.
     * @return ArrayList<ArrayList<String>> headerResultList, contains the searched data from the database.
     * @throws SQLException
     */
    public static ArrayList<ArrayList<String>> getHeaderResults(ResultSet rs) throws SQLException {
        ArrayList<ArrayList<String>> headerResultList = new ArrayList<ArrayList<String>>();
        
        while (rs.next()) {
            ArrayList<String> headerResult = new ArrayList<String>();

            headerResult.clear();
            headerResult.add(Integer.toString(rs.getInt(2)));
            headerResult.add(Integer.toString(rs.getInt(3)));
            headerResult.add(Integer.toString(rs.getInt(4)));
            headerResult.add(Integer.toString(rs.getInt(5)));
            headerResult.add(Integer.toString(rs.getInt(9)));
            headerResult.add(Integer.toString(rs.getInt(10)));
            headerResult.add(rs.getString(11));
            headerResult.add(rs.getString(14));
            headerResult.add(rs.getString(15));
            headerResult.add(rs.getString(17));
            headerResult.add(rs.getString(20));
            headerResultList.add(headerResult);
        }
        System.out.println(headerResultList);
        return headerResultList;
    }
    
    /**
     * The function insertIntoDatabase inserts the found data in the database.
     * 
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void insertIntoDatabase() throws ClassNotFoundException, SQLException{
        
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
