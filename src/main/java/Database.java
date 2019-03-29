import java.net.Inet4Address;
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
 * @author Brian van Wessel
 * @version 1.0
 * @since 23-03-2019
 */
public class Database {
    private static String hostURL = "hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com";
    private static String port = "3306";
    private static String databaseName = "owe7_pg5";
    private static String serverTimezone = "UTC";
    private static String password = "blaat1234";


    /**
     * The function checkHeader checks if the header from the chosen FASTA file exist in the database, if so it gets the corresponing Sequence_ID
     *
     * @param inputHeader a string containing the header of the selected FASTA file.
     * @return sequenceID an Integer containing the sequence ID of the given header.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Integer checkHeader(String inputHeader) throws ClassNotFoundException, SQLException {
        Integer sequenceID = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + hostURL + ":" + port + "/" + databaseName + "?serverTimezone=" + serverTimezone + "",
                    "" + databaseName + "@" + hostURL + "",
                    "" + password + "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select Sequence_ID from sequence where Header like '%" + inputHeader + "%';");
            while (rs.next()) {
                sequenceID = rs.getInt(1);
            }
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException();
        } catch (SQLException ex) {
            throw new SQLException("Fault in SQL statment");
        }
        return sequenceID;
    }

    /**
     * The function checkDatabaseInfo makes contact with the owe7_pg5 database.
     *
     * @return ResultSet rs, contains the searched data from the database.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static ArrayList<ArrayList<String>> checkDatabaseORFInfo(String inputHeader) throws ClassNotFoundException, SQLException {
        ArrayList<ArrayList<String>> headerResultORFList = new ArrayList<ArrayList<String>>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + hostURL + ":" + port + "/" + databaseName + "?serverTimezone=" + serverTimezone + "",
                    "" + databaseName + "@" + hostURL + "",
                    "" + password + "");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select ORF_start, ORF_stop, Reading_frame, ORF_ID from orf o\n" +
                    "left outer join sequence s on o.Sequence_ID = s.Sequence_ID\n" +
                    "where Header like '%" + inputHeader + "%';");

            headerResultORFList = getHeaderORFResults(rs);

            con.close();
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException();
        } catch (SQLException ex) {
            throw new SQLException("Fault in SQL statment");
        }
        return headerResultORFList;
    }

    /**
     * The function getHeaderResults saves the database results in a Arraylist.
     *
     * @param rs contains the searched data from the database.
     * @return ArrayList<ArrayList   <   String>> headerResultList, contains the searched data from the database.
     * @throws SQLException
     */
    public static ArrayList<ArrayList<String>> getHeaderORFResults(ResultSet rs) throws SQLException {
        ArrayList<ArrayList<String>> headerORFResultList = new ArrayList<ArrayList<String>>();
        try {

            while (rs.next()) {
                ArrayList<String> headerORFResult = new ArrayList<String>();
                headerORFResult.clear();
                headerORFResult.add(Integer.toString(rs.getInt(1)));
                headerORFResult.add(Integer.toString(rs.getInt(2)));
                headerORFResult.add(rs.getString(3));
                headerORFResult.add(Integer.toString(rs.getInt(4)));
                headerORFResultList.add(headerORFResult);
            }
        } catch (SQLException ex) {
            throw new SQLException("Fault in SQL server");
        }
        return headerORFResultList;
    }

    /**
     * The fucntion getDatabaseBLASTInfo makes contact with the owe7_pg5 database ands get al BLAST hits for a certain ORF.
     *
     * @param ORFID The ID of the selected ORF.
     * @return headerResultBLASTList is an 2D Arraylist containing all ORF info for the chosen file.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static ArrayList<ArrayList<String>> getDatabaseORFBLASTInfo(Integer ORFID) throws ClassNotFoundException, SQLException {
        ORFID = 1;
        ArrayList<ArrayList<String>> headerResultBLASTList = new ArrayList<ArrayList<String>>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + hostURL + ":" + port + "/" + databaseName + "?serverTimezone=" + serverTimezone + "",
                    "" + databaseName + "@" + hostURL + "",
                    "" + password + "");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select BLAST_start, BLAST_stop, Percentage_identity, E_value, Description from blast_results\n" +
                    "left outer join orf o on blast_results.ORF_ID = o.ORF_ID\n" +
                    "where o.ORF_ID = '" + ORFID + "';");

            headerResultBLASTList = getHeaderORFBLASTResults(rs);

            con.close();
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException();
        } catch (SQLException ex) {
            throw new SQLException("Fault in SQL server");
        }
        return headerResultBLASTList;
    }

    /**
     * The function getHeaderBLASTResults extracts the BLAST results from a resultset.
     *
     * @param rs a resultset containing the BLAST information for certaing header.
     * @return
     * @throws SQLException
     */
    public static ArrayList<ArrayList<String>> getHeaderORFBLASTResults(ResultSet rs) throws SQLException {
        ArrayList<ArrayList<String>> headerBLASTResultList = new ArrayList<ArrayList<String>>();
        try {
            while (rs.next()) {
                ArrayList<String> headerBLASTResult = new ArrayList<String>();
                headerBLASTResult.clear();
                headerBLASTResult.add(Integer.toString(rs.getInt(1)));
                headerBLASTResult.add(Integer.toString(rs.getInt(2)));
                headerBLASTResult.add(Integer.toString(rs.getInt(3)));
                headerBLASTResult.add(String.valueOf(Float.parseFloat(rs.getString(4))));
                headerBLASTResultList.add(headerBLASTResult);
            }
        } catch (SQLException ex) {
            throw new SQLException("Fault in SQL server");
        }
        return headerBLASTResultList;
    }

    /**
     * The function setAndGetSequence extracts the highest sequence_id and inserts a new Sequence.
     *
     * @param headerAndSequence is an Arraylist containg an header and sequence.
     * @return sequenceID an Integer containing the highest sequence_id in the database.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Integer setAndGetSequence(ArrayList<String> headerAndSequence) throws ClassNotFoundException, SQLException {
        Integer sequenceID = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + hostURL + ":" + port + "/" + databaseName + "?serverTimezone=" + serverTimezone + "",
                    "" + databaseName + "@" + hostURL + "",
                    "" + password + "");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select max(sequence_id) from sequence");

            while (rs.next()) {
                sequenceID = rs.getInt(1);
            }
            String Sequence = headerAndSequence.get(1);
            String header = headerAndSequence.get(0);
            sequenceID += 1;
            stmt.executeUpdate("insert into sequence(sequence_id, sequence, header) values (" + sequenceID + ", '" + Sequence + "', '" + header + "');");
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException();
        } catch (SQLException ex) {
            throw new SQLException("Fault in SQL server");
        }
        return sequenceID;
    }

    /**
     * The function insertIntoDatabase inserts the found ORF data in the database.
     *
     * @param foundORFS  An Arraylist containing the found ORF objects.
     * @param sequenceID The sequenceID of the chosen file.
     * @return usedORFIDs An Arraylist containing all the used ORF IDs.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static ArrayList<String> insertORFSIntoDatabase(ArrayList<ORF> foundORFS, Integer sequenceID) throws ClassNotFoundException, SQLException {
        Integer orfID = 0;
        Integer orf_start;
        Integer orf_stop;
        String readingFrame;
        ArrayList<String> usedORFIDs = new ArrayList<String>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + hostURL + ":" + port + "/" + databaseName + "?serverTimezone=" + serverTimezone + "",
                    "" + databaseName + "@" + hostURL + "",
                    "" + password + "");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select max(ORF_ID) from ORF");
            while (rs.next()) {
                orfID = rs.getInt(1);
            }

            for (int i = 0; i < foundORFS.size(); i++) {
                orfID += 1;
                usedORFIDs.add(orfID.toString());
                orf_start = foundORFS.get(i).getORF_start();
                orf_stop = foundORFS.get(i).getORF_stop();
                readingFrame = foundORFS.get(i).getReading_Frame();
                stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
                stmt.executeUpdate("insert into orf(orf_id, orf_start, orf_stop, reading_frame, sequence_id) values (" + orfID + ", " + orf_start + ", " + orf_stop + ", '" + readingFrame + "', " + sequenceID + ");\n");
            }
            con.close();
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException();
        } catch (SQLException ex) {
            throw new SQLException("Fault in SQL server");
        }
        return usedORFIDs;
    }

    /**
     * The function insertIntoDatabase inserts the found BLAST data in the database.
     *
     * @param foundBLASTResults An Arraylist containing all found BLAST results.
     * @param orfID             An Arraylist containing the ORFID that has been BLASTED.
     * @return usedBLASTIDS is an Arraylist containing al the used BLAST IDs.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static ArrayList<Integer> insertBLASTHitsIntoDatabase(ArrayList<Hit> foundBLASTResults, Integer orfID) throws ClassNotFoundException, SQLException {
        Integer blast_ID = 0;
        Integer blast_Start;
        Integer blast_Stop;
        Integer percentage_Identity;
        String Description;
        float e_value;
        ArrayList<Integer> usedBLASTIDs = new ArrayList<Integer>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + hostURL + ":" + port + "/" + databaseName + "?serverTimezone=" + serverTimezone + "",
                    "" + databaseName + "@" + hostURL + "",
                    "" + password + "");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select max(BLAST_ID) from blast_results");
            while (rs.next()) {
                blast_ID = rs.getInt(1);
            }

            for (int i = 0; i < foundBLASTResults.size(); i++) {
                blast_ID += 1;
                usedBLASTIDs.add(orfID);
                blast_Start = foundBLASTResults.get(i).getBlast_start();
                blast_Stop = foundBLASTResults.get(i).getBlast_stop();
                percentage_Identity = foundBLASTResults.get(i).getPct_iden();
                Description = foundBLASTResults.get(i).getBlast_def();
                e_value = foundBLASTResults.get(i).getE_val();
                stmt.executeUpdate("insert into blast_results(blast_id, blast_start, blast_stop, percentage_identity, e_value, Description, orf_id) values (" + blast_ID + "," + blast_Start + ", " + blast_Stop + ", " + percentage_Identity + "," + e_value + ", " + orfID + ");");
            }

            con.close();
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException();
        } catch (SQLException ex) {
            throw new SQLException("Fault in SQL server");
        }
        return usedBLASTIDs;
    }

    public static ArrayList<ArrayList<String>> checkDatabaseInfo(String inputHeader) throws ClassNotFoundException, SQLException {
        ArrayList<ArrayList<String>> headerResultList = new ArrayList<ArrayList<String>>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://" + hostURL + ":" + port + "/" + databaseName + "?serverTimezone=" + serverTimezone + "",
                    "" + databaseName + "@" + hostURL + "",
                    "" + password + "");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select ORF_start, ORF_stop, Reading_frame, BLAST_start, BLAST_stop, Percentage_identity, E_value\n" +
                    "from blast_results right outer join orf o on blast_results.ORF_ID = o.ORF_ID\n" +
                    "  RIGHT OUTER JOIN sequence s on o.Sequence_ID = s.Sequence_ID\n" +
                    "where Header like '%" + inputHeader + "%';");

            headerResultList = getHeaderResults(rs);

            con.close();
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException();
        } catch (SQLException ex) {
            throw new SQLException("Fault in SQL server");
        }
        return headerResultList;
    }

    public static ArrayList<ArrayList<String>> getHeaderResults(ResultSet rs) throws SQLException {
        ArrayList<ArrayList<String>> headerResultList = new ArrayList<ArrayList<String>>();
        try {
            while (rs.next()) {
                ArrayList<String> headerResult = new ArrayList<String>();
                headerResult.clear();
                headerResult.add(Integer.toString(rs.getInt(1)));
                headerResult.add(Integer.toString(rs.getInt(2)));
                headerResult.add(rs.getString(3));
                if (rs.getString(4) == null) {
                    headerResult.add("-");
                } else {
                    headerResult.add(Integer.toString(rs.getInt(4)));
                }
                if (rs.getString(5) == null) {
                    headerResult.add("-");
                } else {
                    headerResult.add(Integer.toString(rs.getInt(5)));
                }
                if (rs.getString(6) == null) {
                    headerResult.add("-");
                } else {
                    headerResult.add(Integer.toString(rs.getInt(6)));
                }
                if (rs.getString(7) == null) {
                    headerResult.add("-");
                } else {
                    headerResult.add(String.valueOf(Float.parseFloat(rs.getString(7))));
                }


                headerResultList.add(headerResult);
            }
        } catch (SQLException ex) {
            throw new SQLException("Fault in SQL server");
        }
        return headerResultList;
    }
}
