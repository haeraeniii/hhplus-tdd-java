package io.hhplus.tdd.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public interface PointManageSv {
    UserPoint getUserPoint(long id);

    List<PointHistory> getHistoryList(long id);

    UserPoint chargePoint(long id, long amount);

    UserPoint usePoint(long id, long amount);
}
