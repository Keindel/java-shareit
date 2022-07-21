package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {
    @EqualsAndHashCode.Include
    private long itemId;
    @NotBlank
    @Size(max = 50)
    private String itemName;
    @NonNull
    @Size(max = 500)
    private String itemDescription;
    private final long ownerId;
    private Boolean available;

    private List<Long> bookingsIds;
}
