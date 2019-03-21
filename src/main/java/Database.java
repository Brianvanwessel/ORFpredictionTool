/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author jadev
 */
public class Database {
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        String inputheader = ">A25";
        Class.forName("com.mysql.cj.jdbc.Driver"); 
        System.out.println("connect");
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com:3306/owe7_pg5?serverTimezone=UTC",
                "owe7_pg5@hannl-hlo-bioinformatica-mysqlsrv.mysql.database.azure.com",
                "blaat1234");  
        
        Statement stmt=con.createStatement();  
        ResultSet rs=stmt.executeQuery("select * from blast_results left outer join orf o on blast_results.ORF_ID = o.ORF_ID\n" +
                                        "left outer join sequence s on o.Sequence_ID = s.Sequence_ID\n" +
                                        "left outer join gene g on blast_results.Gene_ID = g.Gene_ID\n" +
                                        "left outer join gene_function gf on g.Function_ID = gf.Function_ID\n" +
                                        "where Header like '%" + inputheader+ "%';");  
        while(rs.next())  
        System.out.println(rs.getInt(1) + "     " + rs.getInt(2) + "    " + rs.getInt(3) + "    " + rs.getInt(4) + "    " + rs.getInt(5) + "   " + rs.getInt(6) + "    " + rs.getInt(7));  
        con.close();  
    }
}
