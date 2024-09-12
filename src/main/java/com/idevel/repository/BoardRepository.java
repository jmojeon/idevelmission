package com.idevel.repository;

import com.idevel.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // update board_tabel set board_hits = board_hits+1 where id =?
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id =:id")
    void updateHits(@Param("id") Long id);

    @Modifying
    @Query(value = "SELECT (@rownum := @rownum + 1) AS rowNumber, b.* " +
            "FROM BoardEntity b, (SELECT @rownum := 0) r " +
            "ORDER BY b.id DESC LIMIT :offset, :size",
            nativeQuery = true)
    List<Object[]> findAllWithRowNumberAndPaging(@Param("offset") int offset, @Param("size") int size);


}
