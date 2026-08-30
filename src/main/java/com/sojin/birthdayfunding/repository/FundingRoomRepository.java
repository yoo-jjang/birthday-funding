package com.sojin.birthdayfunding.repository;

import com.sojin.birthdayfunding.domain.FundingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingRoomRepository extends JpaRepository<FundingRoom, Long> {
    // JpaRepository<Entity 클래스명, PK 타입>을 상속받는 것만으로
    // save(), findById(), findAll(), delete() 같은 기본 CRUD 메서드가 자동으로 생성돼!
}