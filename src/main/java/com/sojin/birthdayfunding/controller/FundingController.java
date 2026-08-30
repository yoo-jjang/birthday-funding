package com.sojin.birthdayfunding.controller;

import com.sojin.birthdayfunding.domain.Contribution;
import com.sojin.birthdayfunding.domain.FundingRoom;
import com.sojin.birthdayfunding.service.FundingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funding")
@RequiredArgsConstructor
public class FundingController {

    private final FundingService fundingService;

    // 1. 새로운 펀딩방 생성 API
    // POST http://localhost:8080/api/funding
    @PostMapping
    public ResponseEntity<FundingRoom> createRoom(@RequestBody CreateRoomRequest request) {
        FundingRoom room = fundingService.createFundingRoom(
                request.getTitle(),
                request.getTargetAmount(),
                request.getAccountInfo()
        );
        return ResponseEntity.ok(room);
    }

    // 2. [친구용] 특정 펀딩방 정보 조회 API (참여 메시지 목록은 전달 안 함!)
    // GET http://localhost:8080/api/funding/{roomId}
    @GetMapping("/{roomId}")
    public ResponseEntity<FundingRoom> getRoomInfo(@PathVariable Long roomId) {
        FundingRoom room = fundingService.getFundingRoom(roomId);
        return ResponseEntity.ok(room);
    }

    // 3. [친구용] 펀딩 참여 신청 API
    // POST http://localhost:8080/api/funding/{roomId}/apply
    @PostMapping("/{roomId}/apply")
    public ResponseEntity<String> applyContribution(
            @PathVariable Long roomId,
            @RequestBody ApplyContributionRequest request) {

        fundingService.applyContribution(
                roomId,
                request.getContributorName(),
                request.getAmount(),
                request.getMessage()
        );
        return ResponseEntity.ok("펀딩 참여 신청이 완료되었습니다. 입금 확인 후 반영됩니다!");
    }

    // 4. 👑 [방장 전용] 참여 내역 & 축하 메시지 목록 조회 API
    // GET http://localhost:8080/api/funding/{roomId}/admin/contributions
    @GetMapping("/{roomId}/admin/contributions")
    public ResponseEntity<List<Contribution>> getAdminContributions(@PathVariable Long roomId) {
        List<Contribution> contributions = fundingService.getContributions(roomId);
        return ResponseEntity.ok(contributions);
    }

    // 5. 👑 [방장 전용] 입금 승인 처리 API
    // POST http://localhost:8080/api/funding/contributions/{contributionId}/approve
    @PostMapping("/contributions/{contributionId}/approve")
    public ResponseEntity<String> approveDeposit(@PathVariable Long contributionId) {
        fundingService.approveDeposit(contributionId);
        return ResponseEntity.ok("입금이 성공적으로 승인되어 펀딩 금액에 반영되었습니다.");
    }

    // --- 요청 데이터 담기용 DTO (Data Transfer Object) 클래스들 ---

    public static class CreateRoomRequest {
        private String title;
        private Long targetAmount;
        private String accountInfo;

        public String getTitle() { return title; }
        public Long getTargetAmount() { return targetAmount; }
        public String getAccountInfo() { return accountInfo; }
    }

    public static class ApplyContributionRequest {
        private String contributorName;
        private Long amount;
        private String message;

        public String getContributorName() { return contributorName; }
        public Long getAmount() { return amount; }
        public String getMessage() { return message; }
    }
}