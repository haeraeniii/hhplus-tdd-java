package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PointControllerTest {

    @Autowired
    UserPointTable userPointTable;

    @Autowired
    PointHistoryTable pointHistoryTable;

    @Test
    public void charge() {
        //given
        UserPoint userPoint = userPointTable.insertOrUpdate(0, 10000);

        //then
        Assertions.assertThat(userPoint.point()).isEqualTo(10000);

    }

    @Test
    public void useTest() {
        //given
        int amount = 30000;
        UserPoint userPoint = userPointTable.insertOrUpdate(0, 10000);

        //when
        if(userPoint.point() < amount) {
            Assertions.fail("포인트가 부족합니다.");
        } else {
            userPoint = userPointTable.insertOrUpdate(0, userPoint.point() - 3000);
        }

        //then
        Assertions.assertThat(userPoint.point()).isEqualTo(7000);
    }

    @Test
    public void pointTest() {
        //given
        UserPoint userPoint = userPointTable.insertOrUpdate(1, 30000);

        //when
        UserPoint userPoint1 = userPointTable.selectById(1L);

        //then
        Assertions.assertThat(userPoint.point()).isEqualTo(userPoint1.point());
    }

   @Test
   public void historyTest() {
       //given
       pointHistoryTable.insert(3, 30000, TransactionType.CHARGE, System.currentTimeMillis());
       pointHistoryTable.insert(3, 3000, TransactionType.USE, System.currentTimeMillis());
       pointHistoryTable.insert(3, 500, TransactionType.USE, System.currentTimeMillis());

       //when
       List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(3);

       //then
       Assertions.assertThat(pointHistories.size()).isEqualTo(4);
   }
}