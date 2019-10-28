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
@Document(indexName = "user-clicks", type = "click", shards = 1)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Click {

    @Id
    private String id;

    @NotNull(message = "user id cannot be null")
    @NotBlank(message = "user id cannot be empty")
    private int userId;

    @NotNull(message = "hotel id cannot be null")
    @NotBlank(message = "hotel id cannot be empty")
    private int hotelId;

    @NotNull(message = "hotel region cannot be null")
    @NotBlank(message = "hotel region cannot be empty")
    private String hotelRegion;

    @NotNull(message = "timestamp cannot be null")
    @NotBlank(message = "timestamp cannot be empty")
    private int timestamp;

}
