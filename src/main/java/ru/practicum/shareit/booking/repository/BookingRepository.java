package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "order by b.id ")
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
    Optional<Booking> findLastBooking(int itemId, int ownerId);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 " +
            "and b.item.ownerId = ?2 " +
            "and b.status = 'APPROVED'" +
            "and b.start > current_timestamp " +
            "order by b.start ")
    Optional<Booking> findNextBooking(int itemId, int ownerId);
}
