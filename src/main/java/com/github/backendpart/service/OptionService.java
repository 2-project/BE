package com.github.backendpart.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.github.backendpart.repository.OptionRepository;
import com.github.backendpart.web.dto.common.CommonResponseDto;
import com.github.backendpart.web.dto.product.addProduct.OptionRequestDto;
import com.github.backendpart.web.dto.product.editProduct.EditProductRequestDto;
import com.github.backendpart.web.entity.OptionEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptionService{
    private final OptionRepository optionRepository;
    public List<OptionEntity> addOption(List<OptionRequestDto> options){
        List<OptionEntity> addedOption = new ArrayList<>();

        for(OptionRequestDto optionDto : options){
            log.info("[OptionService] 옵션추가 진행중인 optionDto = " + optionDto);
            OptionEntity newOption = OptionEntity.builder()
                    .optionName(optionDto.getOptionName())
                    .optionStock(optionDto.getOptionStock())
                    .build();

            log.info("[OptionService] 추가될 newOption = " + newOption);
            addedOption.add(newOption);
            optionRepository.save(newOption);
        }



        return addedOption;
    }

    @Transactional
    public CommonResponseDto editOption(List<EditProductRequestDto> editProductRequestDtoList) {
        for(EditProductRequestDto option: editProductRequestDtoList){
            log.info("[OptionService] 옵션 재고 변경 진행중 optionDto = " + option);
            OptionEntity targetEntity = optionRepository.findById(option.getOptionCid()).orElseThrow(()-> new NotFoundException("변경할 옵션이 존재하지 않습니다."));
            targetEntity.setOptionStock(option.getOptionStock());

            optionRepository.save(targetEntity);
        }

        return CommonResponseDto.builder()
                .code(200)
                .message("옵션 변경에 성공하였습니다.")
                .success(true)
                .build();
    }
}
