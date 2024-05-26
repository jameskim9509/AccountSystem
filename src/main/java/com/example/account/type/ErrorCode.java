package com.example.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ACCOUNT_NOT_FOUND("해당하는 계좌가 없습니다."),
    FOUND_BALANCE("해당 계좌에 잔액이 존재합니다."),
    CANCELED_TRANSACTION("이미 취소된 결제입니다."),
    UNREGISTERED_ACCOUNT("이미 해지된 계좌입니다."),
    FULL_ACCOUNT("보유 가능 계좌의 수를 초과했습니다."),
    NOT_ENOUGH_BALANCE("잔액이 부족합니다."),
    NOT_MATCH_ACCOUNT_AND_TRANSACTION("계좌와 거래가 일치하지 않습니다."),
    NOT_MATCH_AMOUNT("거래 취소 금액이 일치하지 않습니다."),
    OLD_TRANSACTION("1년이 지난 거래입니다."),
    TRANSACTION_NOT_FOUND("해당하는 거래가 없습니다."),
    USER_NOT_FOUND("사용자가 없습니다."),
    ARGUMENT_NOT_VALID("잘못된 입력값 입니다."),
    IN_PROCESSING("처리 중인 계좌 입니다."),
    JSON_PARSE_ERROR("잘못된 JSON 형식입니다.");

    private final String message;
}
