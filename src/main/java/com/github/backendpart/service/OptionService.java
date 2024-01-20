package com.github.backendpart.service;

import com.github.backendpart.repository.OptionRepository;
import com.github.backendpart.web.dto.product.OptionDto;
import com.github.backendpart.web.dto.product.ProductDto;
import com.github.backendpart.web.dto.product.addProduct.OptionRequestDto;
import com.github.backendpart.web.entity.OptionEntity;
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
    public List<OptionDto> addOption(List<OptionRequestDto> options){
        List<OptionDto> addedOption = new ArrayList<>();

        for(OptionRequestDto optionDto : options){
            log.info("[OptionService] 옵션추가 진행중인 optionDto = " + optionDto);
            OptionDto newOption = OptionDto.builder()
                    .optionName(optionDto.getOptionName())
                    .optionStock(optionDto.getOptionStock())
                    .build();

            log.info("[OptionService] 추가될 newOption = " + newOption);
            addedOption.add(newOption);
            OptionEntity savedOption = OptionEntity.toEntity(newOption);
            optionRepository.save(savedOption);
        }

        return addedOption;
    }
}
