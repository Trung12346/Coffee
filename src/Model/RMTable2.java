/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author maith
 */
public class RMTable2 {
    public int id;
    public String label;
    public float quantity;
    public String unit;

    public RMTable2(int id, String label, float quantity, String unit) {
        this.id = id;
        this.label = label;
        this.quantity = quantity;
        this.unit = unit;
    }

    public RMTable2(String label, float quantity, String unit) {
        this.label = label;
        this.quantity = quantity;
        this.unit = unit;
    }
    
}
