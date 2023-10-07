package ru.phestrix.dtoProducer;

import ru.phestrix.dto.CustomerDto;
import ru.phestrix.storage.entity.Customer;

public class CustomerDtoProducer {
    public static CustomerDto makeCustomerDto(Customer customer){
        return new CustomerDto(customer.getName(), customer.getSurname());
    }
}
