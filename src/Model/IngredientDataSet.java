/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author maith
 */
public class IngredientDataSet {
    public int ingredientId;
    public String ingredientLable;
    public String unit;
    public int storedQuantity;

    public IngredientDataSet(int ingredientId, String ingredientLable, String unit) {
        this.ingredientId = ingredientId;
        this.ingredientLable = ingredientLable;
        this.unit = unit;
    }

    public IngredientDataSet(int ingredientId, int storedQuantity) {
        this.ingredientId = ingredientId;
        this.storedQuantity = storedQuantity;
    }
    
}
