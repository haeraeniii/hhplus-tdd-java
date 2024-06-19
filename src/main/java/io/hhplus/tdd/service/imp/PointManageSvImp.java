package io.hhplus.tdd.service.imp;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.service.PointManageSv;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointManageSvImp implements PointManageSv {

    private final UserPointTable userPointTable;

    private final PointHistoryTable pointHistoryTable;

    @Override
    public UserPoint getUserPoint(long id) {
        return userPointTable.selectById(id);
    }

    @Override
    public List<PointHistory> getHistoryList(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }

    @Override
    @Synchronized
    public UserPoint chargePoint(long id, long amount) {
        pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        return userPointTable.insertOrUpdate(id, amount);
    }

    @Override
    @Synchronized
    public UserPoint usePoint(long id, long amount) {
        UserPoint userPoint = userPointTable.selectById(id);

        if(userPoint.point() < amount) {
            return null;
        } else {
            pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());
        }

        return userPointTable.insertOrUpdate(id, userPoint.point() - amount);
    }
}
