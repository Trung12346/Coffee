/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;

/**
 *
 * @author maith
 */
public class MembershipDataSet {
    public int id;
    public String name;
    public String phone;
    private String securityCode;
    public String rank;
    public Date expirDate;
    public float discount;
    public int point;
//DEPRECATED METHOD{-----------------------------------------------------------------------------------------------------------------------
    public MembershipDataSet(int id, String name, String phone, String securityCode, String rank, Date expirDate, float discount) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.securityCode = securityCode;
        this.rank = rank;
        this.expirDate = expirDate;
        this.discount = discount;
    }
//DEPRECATED METHOD}-----------------------------------------------------------------------------------------------------------------------
    
    public MembershipDataSet(int id, String name, String phone, String securityCode, int point) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.securityCode = securityCode;
        this.point = point;
    }
    public MembershipDataSet() {
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
    
}
