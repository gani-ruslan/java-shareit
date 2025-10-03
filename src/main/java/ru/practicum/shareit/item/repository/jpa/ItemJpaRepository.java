package ru.practicum.shareit.item.repository.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemJpaRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long ownerId);

    List<Item> findByOwnerIdOrderByIdAsc(Long ownerId);

    @Query("""
    select i from Item i
    where i.available = true
      and (lower(i.name) like lower(concat('%', :q, '%'))
           or lower(i.description) like lower(concat('%', :q, '%')))
    """)
    List<Item> search(@Param("q") String q);
}