package com.github.backendpart.web.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    @Schema(description = "주문 고유 번호",nullable = false,example = "1")
    private Long orderId;

    @Schema(description = "주문 리스트",nullable = false,example = "물품이름,옵션 등")
    private List<OrderProductListDto> orderProductList;

}
