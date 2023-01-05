package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query(" select i from Item i " +
            " where i.available = true " +
            "   and (upper(i.name) like upper(concat('%', ?1, '%')) or " +
            "        upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> search(String text);

    @Query("select i from Item i " +
            "where i.ownerId = ?1 " +
            "order by i.id ")
    List<Item> findAllByOwner(int owner);
}


