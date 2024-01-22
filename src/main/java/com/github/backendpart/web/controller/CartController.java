package com.github.backendpart.web.controller;

import com.github.backendpart.service.CartService;
import com.github.backendpart.web.dto.cart.AddCartDto;
import com.github.backendpart.web.dto.cart.CartDto;
import com.github.backendpart.web.dto.cart.ResultAddCartDto;
import com.github.backendpart.web.dto.cart.UpdateCartDTO;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
@Tag(name="Cart",description = "Cart API Document")
public class CartController {


    private final CartService cartService;

    @Operation(summary = "장바구니 조회", description = "장바구니에 담긴 모든 물품을 조회한다")
    @GetMapping("")
    public ResponseEntity<List<CartDto>> viewCart() {
        // 토큰 가져와서 findAllCart에 넘겨주기
        log.info("[GET] 장바구니 조회 요청이 들어왔습니다.");
        List<CartDto> allCart = cartService.findAllCart();
        return ResponseEntity.ok().body(allCart);
    }


    @Operation(summary = "장바구니 추가", description = "장바구니에 새로운 물품을 추가한다.")
    @PostMapping("/{productId}")
    public ResponseEntity<ResultAddCartDto> addCart(
            @PathVariable(name ="productId") Long productId,
            @RequestBody AddCartDto addCartDTO) {
        log.info("[POST] 장바구니 상품 추가 요청이 들어왔습니다.");
        ResultAddCartDto resultAddCart = cartService.addCart(productId,addCartDTO);
        return ResponseEntity.ok().body(resultAddCart);

    }

    @Operation(summary = "장바구니 삭제",description = "장바구니 목록에서 특정물품을 삭제한다")
    @DeleteMapping("/{productId}")
    public CommonResponseDto deleteCart(@PathVariable(name ="productId") Long productId) {
        log.info("[DELETE] 장바구니 물품 삭제 요청이 들어왔습니다.");
        return cartService.deleteCart(productId);
    }

    @Operation(summary = "장바구니 수정",description = "장바구니 목록의 특정물품 수량 또는 옵션을 수정한다")
    @PutMapping("/{productId}") // 또는 cartId
    public CommonResponseDto updateCart(
            @PathVariable(name ="productId") Long productId,
            @RequestBody UpdateCartDTO updateCartDTO){
        log.info("[PUT] 장바구니 물품 옵션/수량 변경 요청이 들어왔습니다.");
        return cartService.updateCart(productId,updateCartDTO);

    }
}
