package com.example.lab11;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class View {

    public static String table(ResultSet rs, String[] columns) throws SQLException {
        StringBuilder table = new StringBuilder("<table>");
        StringBuilder head = new StringBuilder();
        StringBuilder row = new StringBuilder();
        StringBuilder cell = new StringBuilder();
        table.append("<thead>");
        for(String col : columns) {
            head.append("<th>").append(col).append("</th>");
        }
        table.append("<tr>").append(head).append("</tr>");
        table.append("</thead><tbody>");

        while(rs.next()) {

        }

        table.append("</tbody></table>");
        return String.valueOf(table);
    }
}
