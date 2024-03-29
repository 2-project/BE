package com.github.backendpart.web.controller;

import com.github.backendpart.service.CartService;
import com.github.backendpart.web.dto.cart.AddCartDto;
import com.github.backendpart.web.dto.cart.CartDto;
import com.github.backendpart.web.dto.cart.ResultAddCartDto;
import com.github.backendpart.web.dto.cart.UpdateCartDTO;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.entity.users.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
@Tag(name="장바구니 API",description = "장바구니 추가,조회,수정,삭제를 담당하는 API입니다.")
public class CartController {


    private final CartService cartService;

    @Operation(summary = "장바구니 조회", description = "장바구니에 담긴 모든 물품을 조회한다")
    @GetMapping("")
    public ResponseEntity<List<CartDto>> viewCart(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("[GET] 장바구니 조회 요청이 들어왔습니다.");
        List<CartDto> allCart = cartService.findAllCart(customUserDetails);
        return ResponseEntity.ok().body(allCart);
    }


    @Operation(summary = "장바구니 추가", description = "장바구니에 새로운 물품을 추가한다.")
    @PostMapping("/{productId}")
    public ResponseEntity<ResultAddCartDto> addCart(
            @PathVariable(name ="productId") Long productId,
            @RequestBody AddCartDto addCartDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("[POST] 장바구니 상품 추가 요청이 들어왔습니다.");
        if (customUserDetails == null) {
            log.error("customUserDetails is null!");
            // 추가적인 디버깅 정보 또는 예외 처리를 추가할 수 있습니다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ResultAddCartDto resultAddCart = cartService.addCart(productId,addCartDTO,customUserDetails);
        return ResponseEntity.ok().body(resultAddCart);

    }

    @Operation(summary = "장바구니 삭제",description = "장바구니 목록에서 특정물품을 삭제한다")
    @DeleteMapping("/{productId}")
    public CommonResponseDto deleteCart(@PathVariable(name ="productId") Long productId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("[DELETE] 장바구니 물품 삭제 요청이 들어왔습니다.");
        return cartService.deleteCart(productId,customUserDetails);
    }

    @Operation(summary = "장바구니 수정",description = "장바구니 목록의 특정물품 수량 또는 옵션을 수정한다")
    @PutMapping("/{productId}") // 또는 cartId
    public CommonResponseDto updateCart(
            @PathVariable(name ="productId") Long productId,
            @RequestBody UpdateCartDTO updateCartDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("[PUT] 장바구니 물품 옵션/수량 변경 요청이 들어왔습니다.");
        return cartService.updateCart(productId,updateCartDTO,customUserDetails);

    }
}
