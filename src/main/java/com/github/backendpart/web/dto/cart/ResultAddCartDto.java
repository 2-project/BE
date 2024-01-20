package com.github.backendpart.web.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class ResultAddCartDto {

        @Schema(description = "요청의 성공 상태", example = "true")
        private Boolean success;

        @Schema(description = "요청 코드의 status", example = "200")
        private Integer code;

        @Schema(description = "요청 코드의 에러 메시지", example = "이미 장바구니에 담겨있는 물품입니다.")
        private String message;

        @Schema(description = "장바구니 번호", example = "1")
        private Long cartId;
    }

