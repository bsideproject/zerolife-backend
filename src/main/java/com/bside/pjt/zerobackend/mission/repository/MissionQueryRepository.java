package com.bside.pjt.zerobackend.mission.repository;

import com.bside.pjt.zerobackend.mission.domain.Mission;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MissionQueryRepository {

    private final EntityManager em;

    private final int LAST_MISSION_ORDER = 60;

    public List<Mission> findByIdStartsWith(final int start, final int limit) {
        StringBuilder query = new StringBuilder("SELECT m FROM Mission m WHERE m.id in :ids ORDER BY CASE m.id ");

        final List<Long> ids = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            final int rest = (i + start) % LAST_MISSION_ORDER;
            final long id = (rest == 0) ? LAST_MISSION_ORDER : rest;
            query.append(String.format("WHEN %d THEN %d ", id, i));
            ids.add(id);
        }

        query.append("END");

        return em.createQuery(query.toString(), Mission.class)
            .setParameter("ids", ids)
            .getResultList();
    }
}