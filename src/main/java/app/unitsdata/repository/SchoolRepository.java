/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata.repository;

import app.unitsdata.model.School;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SchoolRepository {

    private final Logger logger = LoggerFactory.getLogger(SchoolRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<School> getAllUnits() {
        List<School> schools = new ArrayList<>();
        String sql = "SELECT * FROM schoolsInfo";

        jdbcTemplate.query(sql, new Object[]{}, (rs, row)
                -> new School(rs.getString("eduid"), rs.getString("schoolname"), rs.getString("county"), rs.getString("oid"), rs.getString("tel"), rs.getString("url"), rs.getString("address"), rs.getString("coordinates"))).forEach(school -> schools.add(school));
        return schools;
    }

    public List<School> findByKeywords(String keywords) {
        String sql = "SELECT * FROM schoolsInfo where schoolname like ?";
//        if (stage.equals("none") && county.equals("none")) {
        sql = "SELECT * FROM schoolsInfo where schoolname like ?";
//        }

//        if (stage.equals("none") && !county.equals("none")) {
//            logger.info("no stage but county");
//        }
//        if (county.equals("none") && !stage.equals("none")) {
//            logger.info("no county but stage");
//        }
        List<School> schools = new ArrayList<>();
        jdbcTemplate.query(sql, new Object[]{"%" + keywords + "%"}, (rs, row)
                -> new School(rs.getString("eduid"), rs.getString("schoolname"), rs.getString("county"), rs.getString("oid"), rs.getString("tel"), rs.getString("url"), rs.getString("address"), rs.getString("coordinates"))).forEach(school -> schools.add(school));

        schools.forEach(school -> school.setEduid(""));  //清空eduid

        return schools;
    }

    public List<School> findByOid(String oid) {
        List<School> schools = new ArrayList<>();
        String sql = "SELECT * FROM schoolsInfo where oid = ?";
        jdbcTemplate.query(sql, new Object[]{oid}, (rs, row)
                -> new School(rs.getString("eduid"), rs.getString("schoolname"), rs.getString("county"), rs.getString("oid"), rs.getString("tel"), rs.getString("url"), rs.getString("address"), rs.getString("coordinates"))).forEach(school -> schools.add(school));

        return schools;
    }

}

//    public void updateScoolsUnits() throws IOException {
//
//        String url = "https://oidc.tanet.edu.tw/moeresource/api/v1/eduoid/all";
//        InputStream in = new URL(url).openStream();
//        GZIPInputStream gzin = new GZIPInputStream(in);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(gzin, "UTF-8"));
//        String readLine;
//        StringBuilder sb = new StringBuilder();
//        String updateSQL = "INSERT INTO schoolsInfo (eduid, schoolname,county, oid, tel, url, address, coordinates) "
//                + "VALUES (?,?,?,?,?,?,?,?) "
//                + "ON DUPLICATE KEY UPDATE schoolname = VALUES(schoolname), county = VALUES(county), oid = VALUES(oid), tel = VALUES(tel), url = VALUES(url), address = VALUES(address), coordinates = VALUES(coordinates) ";
//        while ((readLine = reader.readLine()) != null) {
//            sb.append(readLine);
//        }
//
//        if (sb.length() > 0) {
//            ObjectMapper mapper = new ObjectMapper();
//            final List<School> schoolsObj
//                    = Arrays.asList(mapper.readValue(sb.toString(), School[].class));
//
//            logger.info(("檢查長度"));
//            logger.info(String.format("%s,%s", schoolsObj.get(0).getEduid(), schoolsObj.get(0).getSchoolname()));
//
//            for (School school : schoolsObj) {
//                if (school.getEduid().length() != 6) {
//                    Pattern pattern = Pattern.compile("[a-zA-Z0-9]{6}");
//                    Matcher m = pattern.matcher(school.getEduid());
//
//                    if (m.find()) {
//                        school.setEduid(m.group(0));
//                    }
//                    logger.info(school.getEduid());
//                }
//            }
//
//            int[] updateCounts = jdbcTemplate.batchUpdate(updateSQL, new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    School school = schoolsObj.get(i);
//                    ps.setString(1, school.getEduid());
//                    ps.setString(2, school.getSchoolname());
//                    ps.setString(3, school.getCounty());
//                    ps.setString(4, school.getOid());
//                    ps.setString(5, school.getTel());
//                    ps.setString(6, school.getUrl());
//                    ps.setString(7, school.getAddress());
//                    ps.setString(8, school.getCoordinates());
//                }
//
//                @Override
//                public int getBatchSize() {
//                    return schoolsObj.size();
//                }
//            });
//            //修正出現？的中文字, 抓open data gov 資料更新
//            //國小 http://stats.moe.gov.tw/files/school/105/e1_new.csv
//            //國中 http://stats.moe.gov.tw/files/school/105/j1_new.csv
//            //高中 http://stats.moe.gov.tw/files/school/105/high.csv
//            //0代碼, 1學校名稱, 4地址
//            //        url = "http://stats.moe.gov.tw/files/school/105/e1_new.csv";
//            //        in = new URL(url).openStream();
//            //        reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//            //        reviseSchoolsInfo("http://stats.moe.gov.tw/files/school/105/e1_new.csv");
//            final String[] urlList = {
//                " http://stats.moe.gov.tw/files/school/105/e1_new.csv",
//                "http://stats.moe.gov.tw/files/school/105/j1_new.csv",
//                "http://stats.moe.gov.tw/files/school/105/high.csv"
//            };
//
//            for (String dataUrl : urlList) {
//                reviseSchoolsInfo(dataUrl);
//            }
//        }
//    }
//    private void reviseSchoolsInfo(String url) throws MalformedURLException, IOException {
//        logger.info("更新教育單位資料庫");
//        logger.info(url);
//
//        InputStream in = new URL(url).openStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//        String readLine;
//        List<String[]> splitSchoolsInfo = new ArrayList();
//        int i = 0;
//        while ((readLine = reader.readLine()) != null) {
//            if (i != 0) {
//                String[] aschool = readLine.split(",");
//                splitSchoolsInfo.add(aschool);
//                if (aschool[0].equals("014692")) {
//                    logger.info(aschool[0]);
//                    logger.info(aschool[1]);
//                    logger.info(aschool[4]);
//                }
//            }
//            i++;
//        }
//
////
//        String updateSQL = "UPDATE schoolsInfo SET schoolname=?, address=? where eduid=?";
//        jdbcTemplate.batchUpdate(updateSQL, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                ps.setString(1, splitSchoolsInfo.get(i)[1]);
//                ps.setString(2, splitSchoolsInfo.get(i)[4]);
//                ps.setString(3, splitSchoolsInfo.get(i)[0]);
//            }
//
//            @Override
//            public int getBatchSize() {
//                return splitSchoolsInfo.size();
//            }
//        });
//
//    }

