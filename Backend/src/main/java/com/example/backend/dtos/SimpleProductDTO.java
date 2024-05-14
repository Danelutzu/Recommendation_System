package com.example.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SimpleProductDTO extends RepresentationModel<SimpleProductDTO> {
    private int id;
    private String name;
    private float price;
    private String image;
}
