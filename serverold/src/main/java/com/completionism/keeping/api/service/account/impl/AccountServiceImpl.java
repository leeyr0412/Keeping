package com.completionism.keeping.api.service.account.impl;

import com.completionism.keeping.api.service.account.AccountService;
import com.completionism.keeping.api.service.account.dto.AddAccountDto;
import com.completionism.keeping.api.service.account.dto.AuthPhoneDto;
import com.completionism.keeping.api.service.account.dto.CheckPhoneDto;
import com.completionism.keeping.api.service.sms.dto.MessageDto;
import com.completionism.keeping.api.service.sms.dto.SmsResponseDto;
import com.completionism.keeping.api.service.sms.impl.SmsServiceImpl;
import com.completionism.keeping.domain.account.Account;
import com.completionism.keeping.domain.account.repository.AccountRepository;
import com.completionism.keeping.global.exception.NoAuthorizationException;
import com.completionism.keeping.global.utils.RedisUtils;
import com.completionism.keeping.global.utils.ValidationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtils redisUtils;
    private final ValidationUtils validationUtils;
    private final SmsServiceImpl smsServiceImpl;

    @Override
    public Long addAccount(String memberKey, AddAccountDto dto) throws JsonProcessingException {
        String key = "AccountAuth_" + memberKey;

        if(redisUtils.getRedisValue(key, String.class) == null) {
            throw new NoAuthorizationException("401", HttpStatus.UNAUTHORIZED, "핸드폰 번호가 인증되지 않았습니다.");
        }

        String accountNumber = createNewAccountNumber();

        Account account = Account.toAccount(memberKey, accountNumber, passwordEncoder.encode(dto.getAuthPassword()));
        Account saveAccount = accountRepository.save(account);

        return saveAccount.getId();
    }

    @Override
    public void checkPhone(String memberKey, CheckPhoneDto dto) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        String authNumber = validationUtils.createRandomNumCode();

        redisUtils.setRedisValue("AccountPhoneCheck_" + memberKey, authNumber, 210l);

        MessageDto messageDto = MessageDto.builder()
                .to(dto.getPhone())
                .content("[KeePing] 인증번호 [" + authNumber + "]를 입력해 주세요.")
                .build();

        SmsResponseDto response = smsServiceImpl.sendSmsMessage(messageDto);
    }

    @Override
    public void authPhone(String memberKey, AuthPhoneDto dto) throws JsonProcessingException {
        String key = "AccountPhoneCheck_" + memberKey;
        String value = redisUtils.getRedisValue(key, String.class);

        if(value == null) {
            throw new NoAuthorizationException("401", HttpStatus.UNAUTHORIZED, "인증번호가 전송되지 않았습니다.");
        }
        else if(!value.equals(dto.getCode())) {
            throw new NoAuthorizationException("401", HttpStatus.UNAUTHORIZED, "인증번호가 일치하지 않습니다.");
        }

        redisUtils.setRedisValue("AccountAuth_" + memberKey, "true", 210l);
    }

    private String createNewAccountNumber() throws JsonProcessingException {
        Random rand = new Random();

        int num = 0;
        do {
            num = rand.nextInt(888889) + 111111;
        }
        while(redisUtils.getRedisValue("Account_" + String.valueOf(num), String.class) != null);

        String randomNumber = String.valueOf(num);
        redisUtils.setRedisValue("Account_" + randomNumber, "1");

        String validCode = "";

        int divideNum = num;
        for(int i = 0; i < 3; i++) {
            int num1 = divideNum % 10;
            divideNum /= 10;
            int num2 = divideNum % 10;

            int sum = 0;
            if(i == 1) {
                sum = (num1 * num2) % 10;
            }
            else {
                sum = (num1 + num2) % 10;
            }
            divideNum /= 10;

            validCode = String.valueOf(sum) + validCode;
        }

        return "171-" + randomNumber + "-" + validCode + "-27"; // 3-6-3-2
    }
}
