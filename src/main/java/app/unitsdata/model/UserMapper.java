/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author igogo
 */
public class UserMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setSchoolID(rs.getString("schoolid"));
        user.setContact(rs.getString("contact"));
        user.setEmail(rs.getString("email"));
        user.setPasswd(rs.getString("passwd"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setUnit(rs.getString("unit"));
        user.setEnabled(rs.getBoolean("enabled"));
        return user;
    }

}
