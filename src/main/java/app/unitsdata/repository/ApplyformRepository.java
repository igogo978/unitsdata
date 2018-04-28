package app.unitsdata.repository;

import app.unitsdata.model.Applyform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ApplyformRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void update(Applyform applyform) {
        String sql = "INSERT INTO applyform(contact, oid, unit, phone, email, address, schoolid, allowedip, timestamp) "
                + " VALUES(?,?,?,?,?,?,?,?,?)"
                + " ON DUPLICATE KEY UPDATE contact = VALUES(contact), "
                + "oid = VALUES(oid), unit = VALUES(unit), phone = VALUES(phone), "
                + "email = VALUES(email), address = VALUES(address), "
                + "allowedip = VALUES(allowedip), timestamp = VALUES(timestamp)";

        jdbcTemplate.update(sql, applyform.getContact(), applyform.getOid(), applyform.getUnit(), applyform.getPhone(), applyform.getEmail(), applyform.getAddress(), applyform.getEduid(), applyform.getAllowedip(), applyform.getTimestamp());

    }
}
