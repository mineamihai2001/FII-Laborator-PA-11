//package com.example.lab11;
//
//import org.springframework.web.bind.annotation.*;
//
//import javax.xml.crypto.Data;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//public class Controller {
//    private final Database db;
//
//    public Controller() {
//        this.db = Database.getInstance();
//    }
//
//    @GetMapping("/hello")
//    public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
//        return String.format("Hello %s", name);
//    }
//    @GetMapping("/list")
//    public ResultSet list() throws SQLException {
//
//        List<String> result = new ArrayList<>();
//        String sql = "SELECT * FROM test";
//        ResultSet rs = this.db.query(sql);
//        return rs;
//
//    }
//    @PostMapping("/add")
//    public String add() {
//
//        return "added";
//    }
//
//    @PutMapping("/modify")
//    public String modify() {
//
//        return "modified";
//    }
//
//    @DeleteMapping("/delete")
//    public String delete() {
//
//        return "deleted";
//    }
//}
