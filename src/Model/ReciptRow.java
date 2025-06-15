/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author maith
 */
public class ReciptRow extends Recipt {
    public String row = "";
    public ReciptRow(int columnWidth_1, int columnWidth_2) {
        super(columnWidth_1, columnWidth_2);
    }
    public void setParams(String param_1, String param_2) {
        row = "";
        row += param_1;
        for(int i = 0; i < super.columnWidth_1 - param_1.length(); i++) {
            row += " ";
        }
        for(int i = 0; i < super.columnWidth_2 - param_2.length(); i++) {
            row += " ";
        }
        row += param_2;
    }
}
