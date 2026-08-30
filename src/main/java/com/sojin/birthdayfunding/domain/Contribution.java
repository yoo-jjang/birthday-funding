package com.sojin.birthdayfunding.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contributorName; // 돈 보낸 친구 이름 (예: 민석)
    private Long amount;            // 보낸 금액 (예: 20000)
    private String message;         // 축하 메시지

    @Enumerated(EnumType.STRING)
    private ContributionStatus status = ContributionStatus.PENDING; // 입금 상태 (기본값: PENDING)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_room_id")
    private FundingRoom fundingRoom; // 어떤 펀딩방에 참여했는지 (N:1 관계)

    public Contribution(String contributorName, Long amount, String message, FundingRoom fundingRoom) {
        this.contributorName = contributorName;
        this.amount = amount;
        this.message = message;
        this.fundingRoom = fundingRoom;
        this.status = ContributionStatus.PENDING; // 처음 등록시 입금 대기 상태
    }

    // 비즈니스 로직: 방장이 입금 확인 눌렀을 때 상태 변경
    public void completeDeposit() {
        this.status = ContributionStatus.COMPLETED;
    }
}