package com.github.backendpart.service;



import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.github.backendpart.mapper.CartMapper;
import com.github.backendpart.repository.*;
import com.github.backendpart.web.dto.cart.AddCartDto;
import com.github.backendpart.web.dto.cart.CartDto;
import com.github.backendpart.web.dto.cart.ResultAddCartDto;
import com.github.backendpart.web.dto.cart.UpdateCartDTO;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.dto.product.addProduct.AddProductRequestDto;
import com.github.backendpart.web.entity.*;
import com.github.backendpart.web.entity.users.CustomUserDetails;
import com.github.backendpart.web.entity.users.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    public List<CartDto> findAllCart(CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUser().getUserCid();
        UserEntity user = userInfoRepository.findById(userId).orElseThrow(()->new NoSuchElementException("해당 유저를 찾을 수 없습니다.: (회원번호) " + userId));
        UserCartEntity userCart = userCartRepository.findUserCartEntityByUser(user);
        List<CartEntity> cartList = userCart.getCartList();
        List<CartDto> cartListdto = new ArrayList<>();
        // "주문 완료"된 장바구니는 포함시키지 않아야함
        List<CartEntity> NowUserCartList = cartList.stream().filter(cartEntity -> cartEntity.getCartStatus().equals("주문 전")).toList();
        if(NowUserCartList.isEmpty()){
            log.info("[Cart] 해당 장바구니는 비어있습니다.");
        }
        else {
            log.info("[Cart] 주문 전인 장바구니 리스트를 가져왔습니다.");
            List<ProductEntity> productList = NowUserCartList.stream().map(CartEntity::getProduct).toList();
            //첫번째 이미지 주소와 이름 빼오기(대표사진)
            List<String> imgAddress = productList.stream().map(product -> product.getProductImages().get(0).getProductImagePath()).toList();
            List<String> imgName = productList.stream().map(product -> product.getProductImages().get(0).getProductImageName()).toList();
            log.info("[product] 현재 장바구니에 담긴 물품들의 정보를 가져왔습니다.");
            // 리스트에서 카트 하나씩 빼와서 mapper로 dto로 바꾸기
            cartListdto = NowUserCartList.stream().map(CartMapper.INSTANCE::CartEntityToDTO).collect(Collectors.toList());
            for (int i = 0; i < cartListdto.size(); i++) {
                cartListdto.get(i).setImagename(imgAddress.get(i));
                cartListdto.get(i).setImageAddress(imgName.get(i));
            }
        }
        return cartListdto;
    }


    @Transactional
    public CommonResponseDto deleteCart(Long productId,CustomUserDetails customUserDetails) {
            ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("해당 제품을 찾을 수 없습니다.: (제품ID) " + productId)); //
            Long userId = customUserDetails.getUser().getUserCid();
            UserEntity user = userInfoRepository.findById(userId).orElseThrow(()->new NoSuchElementException("해당 유저를 찾을 수 없습니다.: (회원번호) " + userId));
            UserCartEntity userCart = userCartRepository.findUserCartEntityByUser(user);
            cartRepository.deleteByProductAndCartStatusAndUserCart(product, "주문 전",userCart);
            log.info("[deleteCart] 장바구니에 담긴 물품을 삭제했습니다.");
            return CommonResponseDto.builder()
                    .success(true)
                    .code(200)
                    .message("장바구니 삭제에 성공했습니다.")
                    .build();
    }

    @Transactional
    public ResultAddCartDto addCart(Long productid, AddCartDto addCartDTO,CustomUserDetails customUserDetails) {
        Long optionId = addCartDTO.getOptionid();
        OptionEntity option = optionRepository.findById(optionId)
            .orElseThrow(()->new NoSuchElementException("해당 옵션을 찾을 수 없습니다.: (옵션번호) " + optionId));
        ProductEntity product = productRepository.findById(productid)
            .orElseThrow(()->new NoSuchElementException("해당 제품을 찾을 수 없습니다.: (물품번호) " + productid));
        Integer quantity = addCartDTO.getQuantity();
        Long userId = customUserDetails.getUser().getUserCid();
        UserEntity user = userInfoRepository.findById(userId).orElseThrow(()->new NoSuchElementException("해당 유저를 찾을 수 없습니다.: (회원번호) " + userId));

        if (userCartRepository.existsUserCartEntityByUser(user)){
            log.info("[userCart] 이미 존재하는 회원의 장바구니에 물품을 추가합니다.");
            UserCartEntity usercart = userCartRepository.findUserCartEntityByUser(user);
            boolean isAlreadyInCart = cartRepository.existsCartEntityByProductAndUserCartAndCartStatus(product, usercart, "주문 전");
            if (!isAlreadyInCart) {
                CartEntity cart = CartEntity.builder()
                        .userCart(usercart)
                        .product(product).option(option)
                    .cartStatus("주문 전").cartQuantity(quantity).build();
                log.info("[addCart] 새로운 물품이 장바구니에 담겼습니다.");
                CartEntity newCart = cartRepository.save(cart);
                Long cartId = newCart.getCartCid();

                return ResultAddCartDto.builder()
                    .success(true)
                    .code(200)
                    .message("장바구니에 물품이 등록되었습니다.")
                    .cartId(cartId)
                    .build();
            } else {
                log.error("[ERROR] 이미 장바구니에 존재하는 물품입니다.");
                Long cartId = cartRepository.findByProductAndCartStatus(product,"주문 전").getCartCid();
                throw new AlreadyExistsException("이미 장바구니에 존재하는 물품입니다. : (장바구니 번호)"+cartId);
            }
        } else {
            UserCartEntity usercart = UserCartEntity.builder().user(user).build();
            log.info("[userCart] 회원의 장바구니가 존재하지않으므로 장바구니를 생성합니다.");
            userCartRepository.save(usercart);
            CartEntity cart = CartEntity.builder().userCart(usercart).product(product).option(option)
                .cartStatus("주문 전").cartQuantity(quantity).build();
            log.info("[addCart] 새로운 물품이 장바구니에 담겼습니다.");
            CartEntity newCart = cartRepository.save(cart);
            Long cartId = newCart.getCartCid();

            return ResultAddCartDto.builder()
                .success(true)
                .code(200)
                .message("장바구니에 물품이 등록되었습니다.")
                .cartId(cartId)
                .build();
        }
    }


    public CommonResponseDto updateCart(Long productId, UpdateCartDTO updateCartDTO
            ,CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUser().getUserCid();
        UserEntity user = userInfoRepository.findById(userId).orElseThrow(()->new NoSuchElementException("해당 유저를 찾을 수 없습니다.: (회원번호) " + userId));
        ProductEntity product = productRepository.findById(productId).orElseThrow(()->new NoSuchElementException("해당 제품을 찾을 수 없습니다.: (물품번호) " + productId));
        UserCartEntity userCart = userCartRepository.findUserCartEntityByUser(user);
        CartEntity cart = cartRepository.findByProductAndUserCartAndCartStatus(product,userCart,"주문 전");
        Long optionidAfter = updateCartDTO.getOptionid();
        OptionEntity option = optionRepository.findById(optionidAfter).orElseThrow(()->new NoSuchElementException("해당 옵션을 찾을 수 없습니다.: (옵션번호) " + optionidAfter));
        cart.setOption(option);
        Integer cartQuantity = updateCartDTO.getQuantity();
        cart.setCartQuantity(cartQuantity);
        log.info("[updateCart] 장바구니에 담은 물품의 옵션 및 수량을 수정하였습니다.");
        cartRepository.save(cart);
        return CommonResponseDto.builder()
                .success(true)
                .code(200)
                .message("장바구니 물품이 수정되었습니다.")
                .build();
    }
}
