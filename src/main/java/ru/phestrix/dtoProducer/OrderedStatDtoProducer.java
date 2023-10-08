package ru.phestrix.dtoProducer;

import ru.phestrix.dto.OrderedStatDto;
import ru.phestrix.dto.StatDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class OrderedStatDtoProducer {
    public static ArrayList<OrderedStatDto> makeOrderedStatDtos(ArrayList<StatDto> statDtos) {
        if (statDtos.isEmpty()) return null;
        ArrayList<OrderedStatDto> list = new ArrayList<>();
        for (StatDto statDto : statDtos) {
            OrderedStatDto tmp = new OrderedStatDto(
                    statDto.getFullName(),
                    statDto.getProductName(),
                    statDto.getExpenses());
            Optional<OrderedStatDto> optionalTmp = list
                    .stream()
                    .filter(
                            it -> Objects.equals(it.getFullName(), tmp.getFullName()))
                    .findFirst();
            if (optionalTmp.isPresent()) {
                optionalTmp.get().addProductAndExpenses(tmp.getProductNames().get(0), tmp.getTotalExpenses());
            } else {
                list.add(tmp);
            }
        }
        list.sort(Comparator.comparing(OrderedStatDto::getTotalExpenses).reversed());
        return list;
    }
}
