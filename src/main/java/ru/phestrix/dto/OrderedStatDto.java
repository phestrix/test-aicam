package ru.phestrix.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class OrderedStatDto {
    private String fullName;
    private ArrayList<String> productNames = new ArrayList<>();;
    private ArrayList<Integer> expenses = new ArrayList<>();
    private Integer totalExpenses = 0;

    public OrderedStatDto(String name, String product, Integer expenses) {
        fullName = name;
        productNames.add(product);
        this.expenses.add(expenses);
        totalExpenses += expenses;
    }

    public void addProductAndExpenses(String product, Integer expenses){
        productNames.add(product);
        this.expenses.add(expenses);
        totalExpenses+=expenses;
    }
}

