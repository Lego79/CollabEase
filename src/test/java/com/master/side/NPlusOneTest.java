package com.master.side;

import com.master.side.application.dto.CombinedTaskBoardCommentDto;
import com.master.side.application.service.TaskService;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NPlusOneTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void testGetAllCombinedData_NoNPlusOne() {
        // Hibernate Statistics 활성화
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        // 서비스 메서드 실행
        List<CombinedTaskBoardCommentDto> dtos = taskService.getAllCombinedData();

        // 쿼리 실행 횟수 확인 (예: 1 ~ 2회 정도가 예상됨)
        long queryCount = statistics.getQueryExecutionCount();
        System.out.println("Executed Query Count: " + queryCount);

        // N+1 문제 없이 쿼리가 최소화되었는지 기대하는 임계치 이하인지 확인
        // (예: EntityGraph를 통해 fetch join 되었으므로 2회 이내가 되어야 함)
        assertThat(queryCount).isLessThanOrEqualTo(2);
    }
}