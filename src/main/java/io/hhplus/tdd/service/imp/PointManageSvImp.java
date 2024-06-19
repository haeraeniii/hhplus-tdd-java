package io.hhplus.tdd.service.imp;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.repository.PointHistoryRepository;
import io.hhplus.tdd.repository.UserPointRepository;
import io.hhplus.tdd.service.PointManageSv;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointManageSvImp implements PointManageSv {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    // 유저 포인트 조회
    @Override
    public UserPoint getUserPoint(long id) {
        return userPointRepository.selectById(id);
    }

    // 유저 포인트 내역 조회
    @Override
    public List<PointHistory> getHistoryList(long id) {
        return pointHistoryRepository.selectAllByUserId(id);
    }

    // 유저 포인트 충전
    @Override
    @Synchronized
    public UserPoint chargePoint(long id, long amount) {
        pointHistoryRepository.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        return userPointRepository.insertOrUpdate(id, amount);
    }

    // 유저 포인트 사용
    @Override
    @Synchronized
    public UserPoint usePoint(long id, long amount) {
        UserPoint userPoint = userPointRepository.selectById(id);

        // 충전 포인트가 사용할 포인트보다 적을 경우
        if(userPoint.point() < amount) {
            return null;
        } else {
            //히스토리 내역 추가
            pointHistoryRepository.insert(id, amount, TransactionType.USE, System.currentTimeMillis());
        }

        return userPointRepository.insertOrUpdate(id, userPoint.point() - amount);
    }
}
