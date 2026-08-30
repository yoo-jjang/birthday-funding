package com.sojin.birthdayfunding.service;

import com.sojin.birthdayfunding.domain.Contribution;
import com.sojin.birthdayfunding.domain.ContributionStatus;
import com.sojin.birthdayfunding.domain.FundingRoom;
import com.sojin.birthdayfunding.repository.ContributionRepository;
import com.sojin.birthdayfunding.repository.FundingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FundingService {

    private final FundingRoomRepository fundingRoomRepository;
    private final ContributionRepository contributionRepository;

    // 1. 펀딩방 생성하기
    @Transactional
    public FundingRoom createFundingRoom(String title, Long targetAmount, String accountInfo) {
        FundingRoom fundingRoom = new FundingRoom(title, targetAmount, accountInfo);
        return fundingRoomRepository.save(fundingRoom);
    }

    // 2. 특정 펀딩방 조회하기
    public FundingRoom getFundingRoom(Long roomId) {
        return fundingRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 펀딩방입니다. id=" + roomId));
    }

    // 3. 친구가 펀딩 참여 등록하기 (초기 상태: PENDING)
    @Transactional
    public Contribution applyContribution(Long roomId, String contributorName, Long amount, String message) {
        FundingRoom fundingRoom = getFundingRoom(roomId);
        Contribution contribution = new Contribution(contributorName, amount, message, fundingRoom);
        return contributionRepository.save(contribution);
    }

    // 4. 펀딩방에 들어온 모든 참여 내역 조회하기
    public List<Contribution> getContributions(Long roomId) {
        return contributionRepository.findByFundingRoomId(roomId);
    }

    // 🌟 5. [핵심] 방장의 입금 승인 로직 (수동 확인 ➔ 펀딩 달성률 계산)
    @Transactional
    public void approveDeposit(Long contributionId) {
        Contribution contribution = contributionRepository.findById(contributionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 참여 내역입니다. id=" + contributionId));

        // 이미 완료된 건이면 중복 처리 방지
        if (contribution.getStatus() == ContributionStatus.COMPLETED) {
            throw new IllegalStateException("이미 입금 완료 승인된 내역입니다.");
        }

        // 1) 참여 이력 상태를 COMPLETED로 변경
        contribution.completeDeposit();

        // 2) 펀딩방의 현재 모인 금액(currentAmount)에 입금된 금액을 더함
        FundingRoom fundingRoom = contribution.getFundingRoom();
        fundingRoom.addAmount(contribution.getAmount());
    }
}