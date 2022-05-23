package com.example.lab11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@SpringBootApplication
@RestController
public class Lab11Application {
    private final Database db = Database.getInstance();

    public static void main(String[] args) {
        SpringApplication.run(Lab11Application.class, args);
    }

    public Comparator<Map<String, String>> mapComparator = new Comparator<Map<String, String>>() {
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            return m1.get("friends").compareTo(m2.get("friends"));
        }
    };

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/list")
    public Map<String, String> list() throws SQLException {
        Map<String, String> result = new HashMap<>();
        String sql = "SELECT * FROM persons";
        ResultSet rs = this.db.query(sql);
        while (rs.next()) {
            result.put(rs.getString("id"), rs.getString("name"));
        }

        return result;
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/listrel")
    public Map<String, String> listRel() throws SQLException {
        Map<String, String> result = new HashMap<>();
        String sql = "SELECT * FROM relationships";
        ResultSet rs = this.db.query(sql);
        while (rs.next()) {
            result.put(rs.getString("person_id"), rs.getString("friends"));
        }

        return result;
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/listall")
    public List<Map<String, String>> listall() throws SQLException {
        List<Map<String, String>> temp = new ArrayList<>();
        Map<String, Map<String, String>> result = new HashMap<>();
        String sql = "SELECT p.id, p.name, r.person_id, r.friends FROM persons p JOIN relationships r ON r.person_id = p.id";
        ResultSet rs = this.db.query(sql);

        while (rs.next()) {
            Map<String, String> sqlresult = new HashMap<>();

            sqlresult.put("id", rs.getString("p.id"));
            sqlresult.put("name", rs.getString("p.name"));
            sqlresult.put("person_id", rs.getString("r.person_id"));
            sqlresult.put("friend", rs.getString("r.friends"));


            temp.add(sqlresult);
            System.out.println(rs.getString("p.id") + " " + rs.getString("p.name") + " " + rs.getString("r.person_id") + " " + rs.getString("r.friends"));
//            result.put(rs.getString("person_id"), rs.getString("friends"));

        }

        return temp;
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/popular")
    public List<Map<String, String>> popular(@RequestParam String number) throws SQLException {
        System.out.println(number);

        int counter = 0;
        List<Map<String, String>> res = new ArrayList<>();
        List<Map<String, String>> result = new ArrayList<>();
        String sql = "SELECT p.id, p.name, count(r.friends) AS number FROM persons p JOIN relationships r ON p.id = r.person_id GROUP BY p.id ORDER BY count(r.friends) DESC";
        ResultSet rs = this.db.query(sql);
        while (rs.next()) {
            Map<String, String> temp = new HashMap<>();
            temp.put("name", rs.getString("p.name"));
            temp.put("friends", rs.getString("number"));


            res.add(temp);
        }

        for(var i = 0; i<Integer.parseInt(number); ++i) {
            result.add(res.get(i));
        }

        System.out.println(result);
        return result;
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping(value = "/add")
    public Map<String, String> add(@RequestBody String body) throws SQLException {
        System.out.println(body);
        String[] keyVal = body.split("&");
        Map<String, String> request = new HashMap<>();
        for (var param : keyVal) {
            String[] p = param.split("=");
            if (p[0] != null && p[1] != null) {
                request.put(p[0], p[1]);
            }
        }

        System.out.println(request);

        var name = request.get("name");
        var friends = request.get("friends");

        System.out.println(name);
        System.out.println(friends);

        var sql = String.format("INSERT INTO persons (name) VALUES ('%s')", name);
        this.db.update(sql);

        sql = "SELECT count(*) AS lastId FROM persons";
        ResultSet rs = this.db.query(sql);
        int lastId = 0;
        while (rs.next()) {
            lastId = rs.getInt("lastId");
        }
        var aux = friends.split("\\*");

        for (var id : aux) {
            int newId = Integer.parseInt(id);
            sql = String.format("INSERT INTO relationships (person_id, friends) VALUES (%d, %d)", lastId, newId);
            this.db.update(sql);
            sql = String.format("INSERT INTO relationships (person_id, friends) VALUES (%d, %d)", newId, lastId);
            this.db.update(sql);
        }


        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("body", "Insert done!");
        return result;
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PutMapping("/modify")
    public Map<String, String> modify(@RequestBody Map<String, String> body) throws SQLException {
        for (Map.Entry<String, String> entry : body.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            var sql = String.format("UPDATE persons SET name = %s WHERE name = %s", value, key);
            this.db.update(sql);
        }

        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("body", "update done");
        return result;
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @DeleteMapping("/delete")
    public Map<String, String> delete(@RequestBody Map<String, String> body) throws SQLException {
        for (Map.Entry<String, String> entry : body.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            var sql = String.format("DELETE FROM persons WHERE name = %s", value);
            this.db.update(sql);
        }

        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("body", "delete done");
        return result;
    }

}
