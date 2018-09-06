package Entity;

import java.io.Serializable;

public class Season implements Serializable {
    private java.sql.Date enddateC;
    private java.sql.Date startdateC;
    private String sfid;
    private java.sql.Timestamp createddate;
    private String name;
    private java.sql.Timestamp systemmodstamp;
    private String isarchivedC;
    private long id;
    private String hcLastop;
    private String hcErr;
    private String isdeleted;
    private String definedbyC;
    private String issfupdate;


    public java.sql.Date getEnddateC() {
        return enddateC;
    }

    public void setEnddateC(java.sql.Date enddateC) {
        this.enddateC = enddateC;
    }


    public java.sql.Date getStartdateC() {
        return startdateC;
    }

    public void setStartdateC(java.sql.Date startdateC) {
        this.startdateC = startdateC;
    }


    public String getSfid() {
        return sfid;
    }

    public void setSfid(String sfid) {
        this.sfid = sfid;
    }


    public java.sql.Timestamp getCreateddate() {
        return createddate;
    }

    public void setCreateddate(java.sql.Timestamp createddate) {
        this.createddate = createddate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public java.sql.Timestamp getSystemmodstamp() {
        return systemmodstamp;
    }

    public void setSystemmodstamp(java.sql.Timestamp systemmodstamp) {
        this.systemmodstamp = systemmodstamp;
    }


    public String getIsarchivedC() {
        return isarchivedC;
    }

    public void setIsarchivedC(String isarchivedC) {
        this.isarchivedC = isarchivedC;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getHcLastop() {
        return hcLastop;
    }

    public void setHcLastop(String hcLastop) {
        this.hcLastop = hcLastop;
    }


    public String getHcErr() {
        return hcErr;
    }

    public void setHcErr(String hcErr) {
        this.hcErr = hcErr;
    }


    public String getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }


    public String getDefinedbyC() {
        return definedbyC;
    }

    public void setDefinedbyC(String definedbyC) {
        this.definedbyC = definedbyC;
    }


    public String getIssfupdate() {
        return issfupdate;
    }

    public void setIssfupdate(String issfupdate) {
        this.issfupdate = issfupdate;
    }
}
