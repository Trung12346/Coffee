/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
/**
 *
 * @author maith
 */
public class objectToInt {
    public static int[] objectToInteger(ArrayList<Integer> arrayList) {
        int[] intArray = new int[arrayList.size()];
        AtomicInteger index = new AtomicInteger();
        arrayList.forEach(element -> {
            intArray[index.getAndIncrement()] = element;
        });
        return intArray;
    }
}
