package com.master.side;

import com.master.side.application.dto.CombinedTaskBoardCommentResponse;
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


}