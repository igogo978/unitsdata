/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata.repository;

import app.unitsdata.model.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Service;

/**
 *
 * @author igogo
 */
@Service
public class ItemsRepository {

    private final Logger logger = LoggerFactory.getLogger(ItemsRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    //    public List<Person> findByPid(String pid) {
//        List<Person> entries = new ArrayList<>();
//        jdbcTemplate.query("SELECT * FROM persons where pid=?", new Object[]{pid}, (rs, row) -> new Person(rs.getString("cardid"), rs.getString("pname"), rs.getString("pid"), rs.getString("dob"), rs.getString("sex"), rs.getString("issue"), rs.getBoolean("enable"))).forEach(user -> entries.add(user));
//        return entries;
//    }
//    public Item findByPersonGuid(String pid, String creator) {
//        Item item = new Item();
//        return item;
//    }
    public List<Item> findByLimit(String creator, int start, int rows) {
        List<Item> items = new ArrayList<Item>();
        //0,2 第0筆開始, 抓兩筆, 抓到0,1 ;   2,2 第二筆開始抓兩筆 抓 到2,3
        String sql = "SELECT * FROM items where Creator = ? limit ?,?";
        jdbcTemplate.query(
                sql, new Object[]{creator, start, rows},
                (rs, rowNum) -> new Item(rs.getString("FullName"), rs.getString("PersonGuid"), rs.getString("SchoolID"), rs.getString("Creator"), rs.getString("Classtitle"), rs.getString("Grade"), rs.getString("Classno"), rs.getString("Title1"), rs.getString("Title2"), rs.getString("Title3"), rs.getString("Semester"))
        ).forEach(one -> items.add(one));

        return items;
    }

    public List<Item> findAll(String creator) {
        List<Item> items = new ArrayList<Item>();
        String sql = "SELECT * FROM items where Creator = ?";
        //Item(String FullName, String PersonGuid, String SchoolID, String Creator, String Classtitle, String Grade, String Classno, String Title1, String Title2, String Title3, String Semester) {

        jdbcTemplate.query(
                sql, new Object[]{creator},
                (rs, rowNum) -> new Item(rs.getString("FullName"), rs.getString("PersonGuid"), rs.getString("SchoolID"), rs.getString("Creator"), rs.getString("Classtitle"), rs.getString("Grade"), rs.getString("Classno"), rs.getString("Title1"), rs.getString("Title2"), rs.getString("Title3"), rs.getString("Semester"))
        ).forEach(one -> items.add(one));
        return items;
    }

    public int updateItems(List<Item> items) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
//        logger.info("update db:" + mapper.writeValueAsString(items));
        String sql = "INSERT INTO items(PersonGuid, SchoolID, Creator, FullName, Semester, Grade, Classno, Title1, Title2, Title3) "
                + " VALUES(?,?,?,?,?,?,?,?,?,?)"
                + " ON DUPLICATE KEY UPDATE Title1 = VALUES(Title1), Title2 = VALUES(Title2), Title3 = VALUES(Title3), Semester = VALUES(Semester), Grade = VALUES(Grade), Classno = VALUES(Classno) ";

        int[][] updateCounts = jdbcTemplate.batchUpdate(sql, items, items.size(), new ParameterizedPreparedStatementSetter<Item>() {
            @Override
            public void setValues(PreparedStatement ps, Item item) throws SQLException {
                ps.setString(1, item.getPersonGuid());
                ps.setString(2, item.getSchoolID());
                ps.setString(3, item.getCreator());
                ps.setString(4, item.getFullName());
                ps.setString(5, item.getSemester());
                ps.setString(6, item.getGrade());
                ps.setString(7, item.getClassno());
                ps.setString(8, item.getTitle1());
                ps.setString(9, item.getTitle2());
                ps.setString(10, item.getTitle3());
            }
        });

        int results = 0;
        for (int i = 0; i < updateCounts.length; i++) {
            for (int j = 0; j < updateCounts[i].length; j++) {
//                logger.info(String.format("update counts:%d-%d", i, j));
                results++;
            }
        }
        return results;
    }

    public int countAll() {
//        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM personsInfo where pid=?", new Object[]{pid}, Integer.class);
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM items", new Object[]{}, Integer.class);
    }

    public int deleteItems(List<Item> items) throws JsonProcessingException {
        String sql = "DELETE FROM items WHERE PersonGuid = ? and Creator = ? ";
        int[][] updateCounts = jdbcTemplate.batchUpdate(sql, items, items.size(), new ParameterizedPreparedStatementSetter<Item>() {
            @Override
            public void setValues(PreparedStatement ps, Item item) throws SQLException {
                ps.setString(1, item.getPersonGuid());
                ps.setString(2, item.getCreator());
            }
        });

        int results = 0;
        for (int i = 0; i < updateCounts.length; i++) {
            for (int j = 0; j < updateCounts[i].length; j++) {
//                logger.info(String.format("update counts:%d-%d", i, j));
                results++;
            }
        }
        return results;
    }
}
