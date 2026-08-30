package com.sojin.birthdayfunding.repository;

import com.sojin.birthdayfunding.domain.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContributionRepository extends JpaRepository<Contribution, Long> {

    // 특정 펀딩방(fundingRoomId)에 들어온 모든 참여 내역을 조회하는 커스텀 메서드
    List<Contribution> findByFundingRoomId(Long fundingRoomId);
}