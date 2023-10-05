package ru.phestrix.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {

    private Integer id;
    private Integer customerId;
    private Integer goodId;
    private Date date;


}

