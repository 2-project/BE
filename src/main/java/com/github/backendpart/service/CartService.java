package com.github.backendpart.service;



import com.github.backendpart.repository.*;
import com.github.backendpart.web.dto.cart.AddCartDto;
import com.github.backendpart.web.dto.cart.CartDto;
import com.github.backendpart.web.dto.cart.ResultAddCartDto;
import com.github.backendpart.web.dto.cart.UpdateCartDTO;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.entity.CartEntity;
import com.github.backendpart.web.entity.OptionEntity;
import com.github.backendpart.web.entity.ProductEntity;
import com.github.backendpart.web.entity.UserCartEntity;
import com.github.backendpart.web.entity.users.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {


    private final UserInfoRepository userInfoRepository;
    private final UserCartRepository userCartRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;


    @Transactional
    public List<CartDto> findAllCart() {
        //1. 토큰에서 유저정보 빼오기
//        Long userId = 토큰.getUserId;
        Long userId = 1L;
        UserEntity user = userInfoRepository.findById(userId).orElseThrow();
        UserCartEntity userCart = userCartRepository.findUserCartEntityByUser(user);
        List<CartEntity> cartList = userCart.getCartList();
        // "주문 완료"된 장바구니는 포함시키지 않아야함
        List<CartEntity> NowUserCartList = cartList.stream().filter(cartEntity -> cartEntity.getCartStatus().equals("주문 전")).toList();


        List<ProductEntity> productList = NowUserCartList.stream().map(CartEntity::getProduct).toList();

        //첫번째 이미지 주소와 이름 빼오기(대표사진)
        List<String> imgAddress = productList.stream().map(product -> product.getProductImages().get(0).getProductImagePath()).toList();
        List<String> imgName = productList.stream().map(product -> product.getProductImages().get(0).getProductImageName()).toList();

        // 리스트에서 카트 하나씩 빼와서 mapper로 dto로 바꾸기
        List<CartDto> cartListdto = NowUserCartList.stream().map(CartMapper.INSTANCE::CartEntityToDTO).collect(Collectors.toList());
        for(int i=0;i<cartListdto.size();i++){
            cartListdto.get(i).setImagename(imgAddress.get(i));
            cartListdto.get(i).setImageAddress(imgName.get(i));
        }

        return cartListdto;
    }


    @Transactional
    public CommonResponseDto deleteCart(Long productId) {
        productId = 1L;
        ProductEntity product = productRepository.findById(productId).orElseThrow(); // 못찾았을데 notfound에러
        cartRepository.deleteByProductAndCartStatus(product,"주문 전");
        return CommonResponseDto.builder()
                .success(true)
                .code(200)
                .message("장바구니 삭제에 성공했습니다.")
                .build();
    }



    public ResultAddCartDto addCart(Long productid, AddCartDto addCartDTO) {
        Long optionId = addCartDTO.getOptionid();
        OptionEntity option = optionRepository.findById(optionId).orElseThrow();
        ProductEntity product = productRepository.findById(productid).orElseThrow();
        Integer quantity = addCartDTO.getQuantity();
        UserEntity user = userInfoRepository.findById(1L).orElseThrow();
//만약 이미 해당 productid에 해당하는 장바구니가 존재하면 추가하지 않아야함("주문 완료"된 상품이면 상관없음)
//만약 이미 userCart가 존재하면 추가하지 않게 해야함
        if(userCartRepository.existsUserCartEntityByUser(user)){
            UserCartEntity usercart = userCartRepository.findUserCartEntityByUser(user);
            boolean isAlreadyInCart = cartRepository.existsCartEntityByProductAndUserCartAndCartStatus(product,usercart,"주문 전");
            if(!isAlreadyInCart){
                CartEntity cart = new CartEntity(null,usercart,product,option,"주문 전", quantity );
                cartRepository.save(cart);
                return ResultAddCartDto.builder()
                        .success(true)
                        .code(200)
                        .message("장바구니에 물품이 등록되었습니다.")
                        .cartId(cart.getCartCid())
                        .build();
            }
            else {
                return ResultAddCartDto.builder()
                        .success(false)
                        .code(404)
                        .message("이미 장바구니에 존재하는 물건입니다")
                        .build();
            }
        }
        else {
            UserCartEntity usercart = UserCartEntity.builder().user(user).build();
            CartEntity cart = new CartEntity(null, usercart, product, option, "주문 전", quantity);
            cartRepository.save(cart);
            return ResultAddCartDto.builder()
                    .success(true)
                    .code(200)
                    .message("장바구니에 물품이 등록되었습니다.")
                    .cartId(cart.getCartCid())
                    .build();
        }
    }


    public CommonResponseDto updateCart(Long productId, UpdateCartDTO modifyCartDto) {
        // 1.해당 유저를 가져온다 유저엔티티
        // 2. productId로 product를 가져온다.
        // 3. 유저로 userCartEntity를 찾는다.
        // 4. userCartEntity 와 product가 일치하는 cartEntity를 찾는다. (status가 "주문 전"이어야함)
        // 5. 해당 cartEntity에서 option부분을 새로운 옵션엔티티를 넣어준다.
        Long userId = 1L;
        UserEntity user = userInfoRepository.findById(userId).orElseThrow();
        ProductEntity product = productRepository.findById(productId).orElseThrow();
        UserCartEntity userCart = userCartRepository.findUserCartEntityByUser(user);
        CartEntity cart = cartRepository.findByProductAndUserCartAndCartStatus(product,userCart,"주문 전");
        Long optionidAfter = modifyCartDto.getOptionid();
        OptionEntity option = optionRepository.findById(optionidAfter).orElseThrow();
        cart.setOption(option);
        Integer cartQuantity = modifyCartDto.getQuantity();
        cart.setCartQuantity(cartQuantity);
        cartRepository.save(cart);
        return CommonResponseDto.builder()
                .success(true)
                .code(200)
                .message("장바구니 물품이 수정되었습니다.")
                .build();
    }
}
