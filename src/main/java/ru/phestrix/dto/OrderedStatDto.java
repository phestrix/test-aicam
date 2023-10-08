package ru.phestrix.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class OrderedStatDto {
    private String fullName;
    private ArrayList<String> productNames = new ArrayList<>();
    ;
    private ArrayList<Integer> expenses = new ArrayList<>();
    private Integer totalExpenses = 0;

    public OrderedStatDto(String name, String product, Integer expenses) {
        fullName = name;
        productNames.add(product);
        this.expenses.add(expenses);
        totalExpenses += expenses;
    }

    public void addProductAndExpenses(String product, Integer expenses) {
        productNames.add(product);
        this.expenses.add(expenses);
        totalExpenses += expenses;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("name: ").append('\"').append(fullName).append('\"').append(",\n");
        sb.append("purchases: [\n");
        for (int i = 0; i < productNames.size(); i++) {
            sb.append("{\n");
            sb.append("name: ").append('\"').append(productNames.get(i)).append('\"').append(",\n");
            sb.append("expenses: ").append(expenses.get(i)).append("\n");
            sb.append("},\n");
        }
        sb.append("],\n");
        sb.append("totalExpenses: ").append(totalExpenses).append("\n");
        sb.append("}");
        return sb.toString();

    }
}

