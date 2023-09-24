package com.keeping.bankservice.api.controller.online;

import com.keeping.bankservice.api.ApiResponse;
import com.keeping.bankservice.api.controller.online.request.AddOnlineRequest;
import com.keeping.bankservice.api.controller.online.request.ApproveOnlineRequest;
import com.keeping.bankservice.api.service.online.OnlineService;
import com.keeping.bankservice.api.service.online.dto.AddOnlineDto;
import com.keeping.bankservice.api.service.online.dto.ApproveOnlineDto;
import com.keeping.bankservice.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bank-service/api/{member-key}/online")
public class OnlineApiController {

    private final OnlineService onlineService;

    @PostMapping
    public ApiResponse<Void> addOnline(@PathVariable("member-key") String memberKey, @RequestBody AddOnlineRequest request) {
        log.debug("AddOnline={}", request);

        AddOnlineDto dto = AddOnlineDto.toDto(request);

        try {
            onlineService.addOnline(memberKey, dto);
        } catch (Exception e) {
            return ApiResponse.of(1, HttpStatus.SERVICE_UNAVAILABLE, "현재 서비스 이용이 불가능합니다. 잠시 후 다시 시도해 주세요.", null);
        }

        return ApiResponse.ok(null);
    }

    @PostMapping("/approve")
    public ApiResponse<Void> approveOnline(@PathVariable("member-key") String memberKey, @RequestBody ApproveOnlineRequest request) {
        log.debug("ApproveAllowance={}", request);

        ApproveOnlineDto dto = ApproveOnlineDto.toDto(request);

        try {
            onlineService.approveOnline(memberKey, dto);
        } catch (NotFoundException e) {
            return ApiResponse.of(1, e.getHttpStatus(), e.getResultMessage(), null);
        }

        return ApiResponse.ok(null);
    }


}
