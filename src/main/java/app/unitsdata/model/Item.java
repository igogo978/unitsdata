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
public class Item {

    private String FullName; //姓名
    private String PersonGuid; //使用者識別碼

    private String SchoolID; //單位代碼
    private String Creator;  //單位管理者
    private String Classtitle;  //班級名稱
    private String Grade; //年級
    private String Classno; //班級
    private String Title1; //稱職
    private String Title2;  //稱職
    private String Title3;  //稱職
    private String Semester; //學期




    public Item(String FullName, String PersonGuid, String SchoolID, String Creator, String Classtitle, String Grade, String Classno, String Title1, String Title2, String Title3, String Semester) {
        this.FullName = FullName;
        this.PersonGuid = PersonGuid;
        this.SchoolID = SchoolID;
        this.Creator = Creator;
        this.Classtitle = Classtitle;
        this.Grade = Grade;
        this.Classno = Classno;
        this.Title1 = Title1;
        this.Title2 = Title2;
        this.Title3 = Title3;
        this.Semester = Semester;
    }



    public Item() {
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String Semester) {
        this.Semester = Semester;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public String getPersonGuid() {
        return PersonGuid;
    }

    public void setPersonGuid(String PersonGuid) {
        this.PersonGuid = PersonGuid;
    }

    public String getSchoolID() {
        return SchoolID;
    }

    public void setSchoolID(String SchoolID) {
        this.SchoolID = SchoolID;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String Creator) {
        this.Creator = Creator;
    }

    public String getClasstitle() {
        return Classtitle;
    }

    public void setClasstitle(String Classtitle) {
        this.Classtitle = Classtitle;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String Grade) {
        this.Grade = Grade;
    }

    public String getClassno() {
        return Classno;
    }

    public void setClassno(String Classno) {
        this.Classno = Classno;
    }

    public String getTitle1() {
        return Title1;
    }

    public void setTitle1(String Title1) {
        this.Title1 = Title1;
    }

    public String getTitle2() {
        return Title2;
    }

    public void setTitle2(String Title2) {
        this.Title2 = Title2;
    }

    public String getTitle3() {
        return Title3;
    }

    public void setTitle3(String Title3) {
        this.Title3 = Title3;
    }

}
