/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.util.ArrayList;
/**
 *
 * @author maith
 */
public class Recipt {
    public int columnWidth_1;
    public int columnWidth_2;
    public String recipt = "";
    
    public Recipt(int columnWidth_1, int columnWidth_2) {
        this.columnWidth_1 = columnWidth_1;
        this.columnWidth_2 = columnWidth_2;
    }
    public void addReciptRow(String rr) {
        recipt += (rr + "\n");
    }
    public String getRecipt() {
        return recipt;
    }
}
