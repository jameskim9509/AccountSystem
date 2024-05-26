package com.example.account.controller;

import com.example.account.domain.*;
import com.example.account.dto.*;
import com.example.account.exception.AccountException;
import com.example.account.exception.ErrorCode;
import com.example.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("계좌 생성 성공")
    @Test
    void createAccountTest() throws Exception {
        given(accountService.createAccount(anyLong(), anyLong()))
                .willReturn(Account.builder()
                        .accountUser(
                                AccountUser.builder()
                                        .id(1L)
                                        .build()
                        )
                        .accountNumber("1234")
                        .build());

        String jsonRequest = objectMapper.writeValueAsString(
                        CreateAccountForm.RequestForm.builder()
                                .userId(1L)
                                .initBalance(1000L)
                                .build()
                );

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.accountNumber")
                        .value("1234"))
                .andExpect(jsonPath("$.userId")
                        .value(1L))
                .andExpect(status().isOk());
    }

    @DisplayName("계좌 해지 성공")
    @Test
    void accountUnregisterTest() throws Exception {
        given(accountService.unregisterAccount(anyLong(), any()))
                .willReturn(Account.builder()
                        .accountUser(
                                AccountUser.builder()
                                        .id(1L)
                                        .build()
                        )
                        .accountNumber("1234")
                        .build());

        String jsonRequest = objectMapper.writeValueAsString(
                DeleteAccountForm.RequestForm.builder()
                        .userId(1L)
                        .accountNumber("1234")
                        .build()
        );

        mockMvc.perform(delete("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.userId")
                        .value(1L))
                .andExpect(jsonPath("$.accountNumber")
                        .value("1234"))
                .andExpect(status().isOk());
    }

    @DisplayName("계좌 확인 성공")
    @Test
    void getAccountsTest() throws Exception {
        given(accountService.getAccountByUserId(anyLong()))
                .willReturn(
                        List.of(
                            Account.builder()
                                    .accountNumber("1234")
                                    .balance(1000L)
                                    .build(),
                            Account.builder()
                                    .accountNumber("2345")
                                    .balance(2000L)
                                    .build()
                        )
        );

        mockMvc.perform(get("/account?userId="+1L))
                .andDo(print())
                .andExpect(jsonPath("$[0].accountNumber")
                        .value("1234"))
                .andExpect(jsonPath("$[0].balance")
                        .value(1000L))
                .andExpect(jsonPath("$[1].accountNumber")
                        .value("2345"))
                .andExpect(jsonPath("$[1].balance")
                        .value(2000L))
                .andExpect(status().isOk());
    }

    @DisplayName("잔액 사용 성공")
    @Test
    void createTransactionTest() throws Exception {
        given(accountService.createTransaction(anyLong(), any(), anyLong()))
                .willReturn(
                        Transaction.builder()
                                .account(
                                        Account.builder()
                                                .accountNumber("1234")
                                                .build()
                                )
                                .transactionResultType(TransactionResultType.S)
                                .transactionId("abcd")
                                .amount(1000L)
                                .build()
        );

        String jsonRequest = objectMapper.writeValueAsString(
                CreateTransactionForm.RequestForm.builder()
                        .userId(1L)
                        .accountNumber("1234")
                        .amount(1000L)
                        .build()
        );

        mockMvc.perform(post("/transaction/use")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.accountNumber")
                        .value("1234"))
                .andExpect(jsonPath("$.transactionResult")
                        .value("S"))
                .andExpect(jsonPath("$.transactionId")
                        .value("abcd"))
                .andExpect(jsonPath("$.amount")
                        .value(1000L))
                .andExpect(status().isOk());
    }

    @DisplayName("잔액 사용 취소 성공")
    @Test
    void cancelTransactionTest() throws Exception {
        given(accountService.cancelTransaction(any(), any(), anyLong()))
                .willReturn(
                        Transaction.builder()
                                .account(
                                        Account.builder()
                                                .accountNumber("1234")
                                                .build()
                                )
                                .transactionResultType(TransactionResultType.S)
                                .transactionId("abcd")
                                .amount(1000L)
                                .build()
                );

        String jsonRequest = objectMapper.writeValueAsString(
                CancelTransactionForm.RequestForm.builder()
                        .transactionId("abcd")
                        .accountNumber("1234")
                        .amount(1000L)
                        .build()
        );

        mockMvc.perform(post("/transaction/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.accountNumber")
                        .value("1234"))
                .andExpect(jsonPath("$.transactionResult")
                        .value("S"))
                .andExpect(jsonPath("$.transactionId")
                        .value("abcd"))
                .andExpect(jsonPath("$.amount")
                        .value(1000L))
                .andExpect(status().isOk());
    }

    @DisplayName("잔액 사용 확인 성공")
    @Test
    void getTransactionTest() throws Exception {
        given(accountService.getTransaction(any()))
                .willReturn(
                        Transaction.builder()
                                .account(
                                        Account.builder()
                                                .accountNumber("1234")
                                                .build()
                                )
                                .transactionType(TransactionType.USE)
                                .transactionResultType(TransactionResultType.S)
                                .transactionId("abcd")
                                .amount(1000L)
                                .build()
                );

        mockMvc.perform(get("/transaction/abcd"))
                .andDo(print())
                .andExpect(jsonPath("$.accountNumber")
                        .value("1234"))
                .andExpect(jsonPath("$.transactionType")
                        .value("USE"))
                .andExpect(jsonPath("$.transactionResult")
                        .value("S"))
                .andExpect(jsonPath("$.transactionId")
                        .value("abcd"))
                .andExpect(jsonPath("$.amount")
                        .value(1000L))
                .andExpect(status().isOk());
    }

    @DisplayName("USER_NOT_FOUND 확인")
    @Test
    void UserNotFoundTest() throws Exception {
        given(accountService.createAccount(anyLong(), anyLong()))
                .willThrow(new AccountException(ErrorCode.USER_NOT_FOUND));

        String jsonRequest = objectMapper.writeValueAsString(
                CreateAccountForm.RequestForm.builder()
                        .userId(1L)
                        .initBalance(1000L)
                        .build()
        );

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.code")
                        .value("USER_NOT_FOUND"))
                .andExpect(status().isNotAcceptable());
    }

    @DisplayName("FULL_ACCOUNT 확인")
    @Test
    void FullAccountTest() throws Exception {
        given(accountService.createAccount(anyLong(), anyLong()))
                .willThrow(new AccountException(ErrorCode.FULL_ACCOUNT));

        String jsonRequest = objectMapper.writeValueAsString(
                CreateAccountForm.RequestForm.builder()
                        .userId(1L)
                        .initBalance(1000L)
                        .build()
        );

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.code")
                        .value("FULL_ACCOUNT"))
                .andExpect(status().isNotAcceptable());
    }

    @DisplayName("ACCOUNT_NOT_FOUND 확인")
    @Test
    void AccountNotFoundTest() throws Exception {
        given(accountService.unregisterAccount(anyLong(), any()))
                .willThrow(new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        String jsonRequest = objectMapper.writeValueAsString(
                DeleteAccountForm.RequestForm.builder()
                        .userId(1L)
                        .accountNumber("1234")
                        .build()
        );

        mockMvc.perform(delete("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.code")
                        .value("ACCOUNT_NOT_FOUND"))
                .andExpect(status().isNotAcceptable());
    }

    @DisplayName("UNREGISTERED_ACCOUNT 확인")
    @Test
    void UnregisteredAccountTest() throws Exception {
        given(accountService.unregisterAccount(anyLong(), any()))
                .willThrow(
                        new AccountException(ErrorCode.UNREGISTERED_ACCOUNT)
                );

        String jsonRequest = objectMapper.writeValueAsString(
                DeleteAccountForm.RequestForm.builder()
                        .userId(1L)
                        .accountNumber("1234")
                        .build()
        );

        mockMvc.perform(delete("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.code")
                        .value("UNREGISTERED_ACCOUNT"))
                .andExpect(status().isNotAcceptable());
    }

    @DisplayName("FOUND_BALANCE 확인")
    @Test
    void FoundBalanceTest() throws Exception {
        given(accountService.unregisterAccount(anyLong(), any()))
                .willThrow(new AccountException(ErrorCode.FOUND_BALANCE));

        String jsonRequest = objectMapper.writeValueAsString(
                DeleteAccountForm.RequestForm.builder()
                        .userId(1L)
                        .accountNumber("1234")
                        .build()
        );

        mockMvc.perform(delete("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.code")
                        .value("FOUND_BALANCE"))
                .andExpect(status().isNotAcceptable());
    }

    @DisplayName("NOT_ENOUGH_BALANCE 확인")
    @Test
    void NotEnoughBalanceTest() throws Exception {
        given(accountService.createTransaction(anyLong(), any(), anyLong()))
                .willThrow(new AccountException(ErrorCode.NOT_ENOUGH_BALANCE));

        String jsonRequest = objectMapper.writeValueAsString(
                CreateTransactionForm.RequestForm.builder()
                        .userId(1L)
                        .accountNumber("1234")
                        .amount(1000L)
                        .build()
        );

        mockMvc.perform(post("/transaction/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.code")
                        .value("NOT_ENOUGH_BALANCE"))
                .andExpect(status().isNotAcceptable());
    }

    @DisplayName("NOT_MATCH_ACCOUNT_AND_TRANSACTION 확인")
    @Test
    void NotMatchAccountAndTransactionTest() throws Exception {
        given(accountService.cancelTransaction(any(), any(), anyLong()))
                .willThrow(
                        new AccountException(
                                ErrorCode.NOT_MATCH_ACCOUNT_AND_TRANSACTION
                        )
                );

        String jsonRequest = objectMapper.writeValueAsString(
                CancelTransactionForm.RequestForm.builder()
                        .transactionId("abcd")
                        .accountNumber("1234")
                        .amount(1000L)
                        .build()
        );

        mockMvc.perform(post("/transaction/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.code")
                        .value("NOT_MATCH_ACCOUNT_AND_TRANSACTION"))
                .andExpect(status().isNotAcceptable());
    }

    @DisplayName("NOT_MATCH_AMOUNT 확인")
    @Test
    void NotMatchAmountTest() throws Exception {
        given(accountService.cancelTransaction(any(), any(), anyLong()))
                .willThrow(new AccountException(ErrorCode.NOT_MATCH_AMOUNT));

        String jsonRequest = objectMapper.writeValueAsString(
                CancelTransactionForm.RequestForm.builder()
                        .transactionId("abcd")
                        .accountNumber("1234")
                        .amount(1000L)
                        .build()
        );

        mockMvc.perform(post("/transaction/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.code")
                        .value("NOT_MATCH_AMOUNT"))
                .andExpect(status().isNotAcceptable());
    }

    @DisplayName("OLD_TRANSACTION 확인")
    @Test
    void OldTransactionTest() throws Exception {
        given(accountService.cancelTransaction(any(), any(), anyLong()))
                .willThrow(new AccountException(ErrorCode.OLD_TRANSACTION));

        String jsonRequest = objectMapper.writeValueAsString(
                CancelTransactionForm.RequestForm.builder()
                        .transactionId("abcd")
                        .accountNumber("1234")
                        .amount(1000L)
                        .build()
        );

        mockMvc.perform(post("/transaction/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.code")
                        .value("OLD_TRANSACTION"))
                .andExpect(status().isNotAcceptable());
    }

    @DisplayName("CANCELED_TRANSACTION 확인")
    @Test
    void CanceledTransactionTest() throws Exception {
        given(accountService.cancelTransaction(any(), any(), anyLong()))
                .willThrow(new AccountException(
                        ErrorCode.CANCELED_TRANSACTION
                ));

        String jsonRequest = objectMapper.writeValueAsString(
                CancelTransactionForm.RequestForm.builder()
                        .transactionId("abcd")
                        .accountNumber("1234")
                        .amount(1000L)
                        .build()
        );

        mockMvc.perform(post("/transaction/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$.code")
                        .value("CANCELED_TRANSACTION"))
                .andExpect(status().isNotAcceptable());
    }


    @DisplayName("ARGUMENT_NOT_VALID 확인")
    @Test
    void ArgumentNotValidTest() throws Exception {
        given(accountService.createTransaction(anyLong(), any(), anyLong()))
                .willReturn(
                        Transaction.builder()
                                .build()
                );

        String jsonRequest = objectMapper.writeValueAsString(
                CreateTransactionForm.RequestForm.builder()
                        .userId(1L)
                        .accountNumber("1234")
                        .amount(10000000L) // 거래 금액이 매우 큰 경우
                        .build()
        );

        mockMvc.perform(post("/transaction/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(jsonPath("$[0].code")
                        .value("ARGUMENT_NOT_VALID"))
                .andExpect(status().isBadRequest());
    }
}