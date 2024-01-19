package com.github.backendpart.web.controller;

import com.github.backendpart.service.CartService;
import com.github.backendpart.web.dto.cart.AddCartDto;
import com.github.backendpart.web.dto.cart.CartDto;
import com.github.backendpart.web.dto.cart.UpdateCartDTO;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {


    private final CartService cartService;


    //장바구니 조회 api (해당 userid에 해당하는 userCart를 가져오고
    // userCart의 cartList를 dto로 변경해 보여주기
    @Operation(summary = "장바구니 조회", description = "장바구니에 담긴 모든 물품을 조회한다")
    @GetMapping("")
    public List<CartDto> viewCart() { // 토큰 가져와서 findAllCart에 넘겨주기
        return cartService.findAllCart();
    }




    //  3.장바구니 담기 api (추가)
    //실행 ok
    @Operation(summary = "장바구니 추가", description = "장바구니에 새로운 물품을 추가한다.")
    @PostMapping("/{productid}")
    public CommonResponseDto addCart(
            @PathVariable Long productid,
            @RequestBody AddCartDto addCartDTO) {
//            Long cartcid =
        return cartService.addCart(productid,addCartDTO);


    }




    // 4. 장바구니 삭제 api
    // 실행 ok
    @Operation(summary = "장바구니 삭제",description = "장바구니 목록에서 특정물품을 삭제한다")
    @DeleteMapping("/{product_id}")
    public CommonResponseDto deleteCart(@Parameter(description = "물품 고유 번호") Long productId) {
        return cartService.deleteCart(productId);


    }


    //장바구니 물품 수량 또는 옵션 수정
    // 실행 ok
    @Operation(summary = "장바구니 수정",description = "장바구니 목록의 특정물품 수량 또는 옵션을 수정한다")
    @PutMapping("/{productid}") // 또는 cartId
    public CommonResponseDto updateCart(
            @PathVariable Long productid,
            @RequestBody UpdateCartDTO updateCartDTO){
        return cartService.updateCart(productid,updateCartDTO);

    }
}
