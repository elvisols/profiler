package com.lvs.profiler.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "user-selections", type = "selection")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Selection {

    @Id
    private String id;

    @NotNull(message = "user id cannot be null")
    @NotBlank(message = "user id cannot be empty")
    private int userId;

    @NotNull(message = "amenity id cannot be null")
    @NotBlank(message = "amenity id cannot be empty")
    private int amenityId;

    @NotNull(message = "timestamp cannot be null")
    @NotBlank(message = "timestamp cannot be empty")
    private int timestamp;

}
