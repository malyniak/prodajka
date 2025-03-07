package backend.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import backend.domain.dto.ProductDto;
import backend.domain.dto.PhoneDto;
import backend.rdb.entities.PhoneEntity;
import backend.rdb.repositories.PhoneRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PhoneService {
    private final PhoneRepository phoneRepository;
    private final ProductService productService;
    public Flux<PhoneDto> getAll() {
       return phoneRepository.findAll()
                .flatMap(phoneEntity -> {
                   return productService.getById(phoneEntity.getProductId())
                            .map(productDto -> toPhoneDto(phoneEntity, productDto));
                        });


    }

//    public Flux<ProductDetailsDto<PhoneDto>> getSortPhonesByPriceAsc() {
//       return phoneRepository.sortByPriceAsc()
//                        .flatMap(phoneEntity -> {
//                          return productService.getById(phoneEntity.getProductId())
//                                  .defaultIfEmpty(new ProductDto())
//                                   .map(product -> {
//                                       return new ProductDetailsDto<PhoneDto>(product, toPhone(phoneEntity));
//                                   });
//                        });
//    }
//


    public Mono<PhoneDto> getById(String id) {
        return phoneRepository.findById(id).
                flatMap(phoneEntity -> {
                    return productService.getById(phoneEntity.getProductId())
                            .map(productDto -> toPhoneDto(phoneEntity, productDto));
                });
    }

    public Mono<PhoneEntity> create(PhoneDto phoneDto) {
        var phoneEntity = toPhoneEntity(phoneDto);
        return phoneRepository.save(phoneEntity)
                .doOnNext(__ -> System.out.println("Phone created"))
                .doOnError(__ -> System.out.println("Save error"));
    }

    public Mono<PhoneEntity> update(PhoneDto phoneDto, String id) {
        var phoneEntityMono = phoneRepository.findById(id);
       return phoneEntityMono.flatMap(phoneEntity-> {
            if(phoneEntity.getProductId() != null) {
                phoneEntity.setProductId(phoneDto.getProductId());
            }
            if(phoneEntity.getDisplay() != null) {
                phoneEntity.setDisplay(phoneDto.getDisplay());
            }
            if(phoneEntity.getCamera() != null) {
                phoneEntity.setCamera(phoneDto.getCamera());
            }
            if(phoneEntity.getFrequencies() != null) {
                phoneEntity.setFrequencies(phoneDto.getFrequencies());
            }
            if(phoneEntity.getBatteryPower() != null) {
                phoneEntity.setBatteryPower(phoneDto.getBatteryPower());
            }
            if(phoneEntity.getIs3g() != null) {
                phoneEntity.setIs3g(phoneDto.getIs3g());
            }
            if(phoneEntity.getVersionOS() != null) {
                phoneEntity.setVersionOS(phoneDto.getVersionOS());
            }
            if(phoneEntity.getOtherFunctions() != null) {
                phoneEntity.setOtherFunctions(phoneDto.getOtherFunctions());
            }
            if(phoneEntity.getIsLTE() != null) {
                phoneEntity.setIsLTE(phoneDto.getIsLTE());
            }
            if(phoneEntity.getIs5g() != null) {
                phoneEntity.setIs5g(phoneDto.getIs5g());
            }
            if(phoneEntity.getIsNanoSim() != null) {
                phoneEntity.setIsNanoSim(phoneDto.getIsNanoSim());
            }
            if(phoneEntity.getIs_2Sim() != null) {
                phoneEntity.setIs_2Sim(phoneDto.getIs_2Sim());
            }
           phoneEntity.setNewEntity(false);
           return phoneRepository.save(phoneEntity);
        });
    }


    public PhoneDto toPhoneDto (PhoneEntity phoneEntity, ProductDto productDto) {
        var phone = new PhoneDto();
        phone.setId(phoneEntity.getId());
        phone.setProductId(phoneEntity.getProductId());
        phone.setCamera(phoneEntity.getCamera());
        phone.setDisplay(phoneEntity.getDisplay());
        phone.setFrequencies(phoneEntity.getFrequencies());
        phone.setIs3g(phoneEntity.getIs3g());
        phone.setBatteryPower(phoneEntity.getBatteryPower());
        phone.setIs5g(phoneEntity.getIs5g());
        phone.setIs_2Sim(phoneEntity.getIs_2Sim());
        phone.setIsLTE(phoneEntity.getIsLTE());
        phone.setIsNanoSim(phoneEntity.getIsNanoSim());
        phone.setVersionOS(phoneEntity.getVersionOS());
        phone.setOtherFunctions(phoneEntity.getOtherFunctions());
        phone.setFullName(productDto.getFullName());
        phone.setPrice(productDto.getPrice());
        phone.setModel(productDto.getModel());
        phone.setBrand(productDto.getBrand());
        phone.setAvgGrade(productDto.getAvgGrade());
        phone.setSaleStatus(productDto.getSaleStatus());
        phone.setProductCategory(productDto.getProductCategory());
        phone.setIsAccess(productDto.getIsAccess());
        phone.setDescription(productDto.getDescription());
        phone.setImageUrl(productDto.getImageUrl());
        phone.setVideoUrl(productDto.getVideoUrl());
        phone.setColor(productDto.getColor());
        return phone;
    }




    public PhoneEntity toPhoneEntity(PhoneDto phoneDto) {
        var phoneEntity = new PhoneEntity();
        phoneEntity.setId(UUID.randomUUID().toString());
        phoneEntity.setCamera(phoneDto.getCamera());
        phoneEntity.setDisplay(phoneDto.getDisplay());
        phoneEntity.setFrequencies(phoneDto.getFrequencies());
        phoneEntity.setIs3g(phoneDto.getIs3g());
        phoneEntity.setBatteryPower(phoneDto.getBatteryPower());
        phoneEntity.setIs5g(phoneDto.getIs5g());
        phoneEntity.setIs_2Sim(phoneDto.getIs_2Sim());
        phoneEntity.setIsNanoSim(phoneDto.getIsNanoSim());
        phoneEntity.setVersionOS(phoneDto.getVersionOS());
        phoneEntity.setOtherFunctions(phoneDto.getOtherFunctions());
        phoneEntity.setNewEntity(true);
        return phoneEntity;
    }


    public Mono<Void> delete(String id) {
        var phone = phoneRepository.findById(id).doOnNext(System.out::println);
       return phone.flatMap(phoneRepository::delete);
    }
}
