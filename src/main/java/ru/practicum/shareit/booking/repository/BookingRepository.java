package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerId(int bookerId);

    @Query(" select b from Item i, Booking b " +
            " where b.item.ownerId = ?1 " +
            " order by b.start ")
    List<Booking> findAllByOwnerId(int ownerId);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 " +
            "and b.item.ownerId = ?2 " +
            "and b.status = 'APPROVED'" +
            "and b.end < current_timestamp " +
            "order by b.end ")
    Booking findLastBooking(int itemId, int ownerId);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 " +
            "and b.item.ownerId = ?2 " +
            "and b.status = 'APPROVED'" +
            "and b.start > current_timestamp " +
            "order by b.start ")
    Booking findNextBooking(int itemId, int ownerId);
}
