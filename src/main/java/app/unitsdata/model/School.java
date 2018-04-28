/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata.model;

/**
 *
 * @author igogo
 */
public class School {
    String eduid;
    String schoolname;
    String county;
    String oid;
    String tel;
    String url;
    String address;
    String coordinates;

    public School() {
    }

    public School(String eduid, String schoolname, String county, String oid, String tel, String url, String address, String coordinates) {
        this.eduid = eduid;
        this.schoolname = schoolname;
        this.county = county;
        this.oid = oid;
        this.tel = tel;
        this.url = url;
        this.address = address;
        this.coordinates = coordinates;
    }

    public String getEduid() {
        return eduid;
    }

    public void setEduid(String eduid) {
        this.eduid = eduid;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }




}
