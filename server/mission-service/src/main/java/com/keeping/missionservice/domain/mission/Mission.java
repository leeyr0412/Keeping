package com.keeping.missionservice.domain.mission;

import com.keeping.missionservice.global.common.TimeBaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@RequiredArgsConstructor
public class Mission extends TimeBaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long id;
    
    @Column(name = "child_key")
    private String childKey;

    @Column
    @Enumerated(EnumType.STRING)
    private MissionType type;
    
    @Column
    private String todo;
    
    @Column
    private int money;
    
    @Column
    private String cheeringMessage;
    
    @Column
    private String childRequestComment; // 자식이 미션을 요청할 때 하는 코멘트
    
    @Column
    private String finishedComment; // 미션 완료 후 자식의 코멘트
    
    @Column
    private LocalDate startDate;
    
    @Column
    private LocalDate endDate;
    
    @Column
    @Enumerated(EnumType.STRING)
    private Completed completed;

    @Builder
    public Mission(Long id, String childKey, MissionType type, String todo, int money, String cheeringMessage, String childRequestComment, String finishedComment, LocalDate startDate, LocalDate endDate, Completed completed) {
        this.id = id;
        this.childKey = childKey;
        this.type = type;
        this.todo = todo;
        this.money = money;
        this.cheeringMessage = cheeringMessage;
        this.childRequestComment = childRequestComment;
        this.finishedComment = finishedComment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
    }

    public static Mission toMission(String childKey, MissionType type, String todo, int money, String cheeringMessage,String childRequestComment, LocalDate startDate, LocalDate endDate, Completed completed) {
        return Mission.builder()
                .childKey(childKey)
                .type(type)
                .todo(todo)
                .money(money)
                .cheeringMessage(cheeringMessage)
                .childRequestComment(childRequestComment)
                .startDate(startDate)
                .endDate(endDate)
                .completed(completed)
                .build();
    }

    public void updateCheeringMessage(String cheeringMessage) {
        this.cheeringMessage = cheeringMessage;
    }

    public void updateCompleted(Completed completed) {
        this.completed = completed;
    }

    public void updateChildRequestComment(String childRequestComment) {
        this.childRequestComment = childRequestComment;
    }
    
    public void updateFinishedComment(String finishedComment) {
        this.finishedComment = finishedComment;
    }

    public void updateMission(String todo, int money, String cheeringMessage, LocalDate startDate, LocalDate endDate) {
        this.todo = todo;
        this.money = money;
        this.cheeringMessage = cheeringMessage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void deleteMission() {
        this.completed = Completed.DISABLED;
    }
}
