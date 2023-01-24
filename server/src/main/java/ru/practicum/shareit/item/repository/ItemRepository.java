package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("select i from Item i " +
            "where i.available = true " +
            "and (upper(i.name) like upper(concat('%', :text, '%')) or " +
            "upper(i.description) like upper(concat('%', :text, '%')))")
    Page<Item> search(String text, Pageable page);

    Page<Item> findAllByOwnerId(int ownerId, Pageable page);

    @Query(" select i from Item i " +
            "where i.itemRequest.id = :itemRequestId " +
            "order by i.id desc")
    List<Item> findItemByItemRequestId(int itemRequestId);
}
