package com.sojin.birthdayfunding.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FundingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;          // 펀딩 제목 (예: 소진이의 닌텐도 펀딩)
    private Long targetAmount;     // 목표 금액
    private Long currentAmount = 0L; // 현재 모인 금액 (기본값 0원)
    private String accountInfo;    // 입금받을 계좌 정보

    // 생성자 (펀딩방을 처음 만들 때 필요한 값들)
    public FundingRoom(String title, Long targetAmount, String accountInfo) {
        this.title = title;
        this.targetAmount = targetAmount;
        this.accountInfo = accountInfo;
        this.currentAmount = 0L;
    }

    // 비즈니스 로직: 입금이 승인되면 현재 금액을 올려주는 기능
    public void addAmount(Long amount) {
        this.currentAmount += amount;
    }
}