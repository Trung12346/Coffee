/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author maith
 */
public class ProductIngredientsDataSet {
    public int productId;
    public int ingredientId;
    public float quantity;

    public ProductIngredientsDataSet(int productId, int ingredientId, float quantity) {
        this.productId = productId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }
    
}
