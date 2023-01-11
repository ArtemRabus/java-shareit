package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String description;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requester;
    @NotNull
    private LocalDateTime created;

    public ItemRequest(String description, User requester, LocalDateTime created) {
        this.description = description;
        this.requester = requester;
        this.created = created;
    }
}
