package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.notice.NoticeDto;
import com.geeks.geeksbackend.dto.product.ProductDto;
import com.geeks.geeksbackend.dto.product.ProductListDto;
import com.geeks.geeksbackend.dto.product.ReceiveProductDto;
import com.geeks.geeksbackend.dto.product.SettleProductDto;
import com.geeks.geeksbackend.entity.Product;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.entity.ProductUser;
import com.geeks.geeksbackend.enumeration.GroupBuyingStatus;
import com.geeks.geeksbackend.enumeration.GroupBuyingUserType;
import com.geeks.geeksbackend.enumeration.ProductType;
import com.geeks.geeksbackend.repository.ProductRepository;
import com.geeks.geeksbackend.repository.ProductUserRepository;
import com.geeks.geeksbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.geeks.geeksbackend.enumeration.MessageTemplate.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductUserRepository productUserRepository;

    private final NoticeService noticeService;

    public ProductDto createProduct(ProductDto input, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        Product product = Product.builder()
                .user(user)
                .name(input.getName())
                .type1(ProductType.valueOfTitle(input.getType1()))
                .price(input.getPrice())
                .startTime(LocalDateTime.parse(input.getStartTime(), DateTimeFormatter.ISO_DATE_TIME))
                .endTime(LocalDateTime.parse(input.getEndTime(), DateTimeFormatter.ISO_DATE_TIME))
                .maxParticipant(input.getMaxParticipant())
                .destination(input.getDestination())
                .thumbnailUrl(input.getThumbnailUrl())
                .status(GroupBuyingStatus.OPEN)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        productRepository.save(product);

        ProductUser productUser = ProductUser.builder()
                .product(product)
                .user(user)
                .type(GroupBuyingUserType.MANAGER)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        productUserRepository.save(productUser);

        return ProductDto.from(product);
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto input, Long userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));

        // 권한 확인
        if (product.getCreatedBy() != userId) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // TODO: 공동구매에 이미 참여한 사용자가 있으면 수정 불가
        // ...

        // JPA 변경감지 사용
        product.setName(input.getName());
        product.setType1(ProductType.valueOfTitle(input.getType1()));
        product.setPrice(input.getPrice());
        product.setStartTime(LocalDateTime.parse(input.getStartTime(), DateTimeFormatter.ISO_DATE_TIME));
        product.setEndTime(LocalDateTime.parse(input.getEndTime(), DateTimeFormatter.ISO_DATE_TIME));
        product.setMaxParticipant(input.getMaxParticipant());
        product.setDestination(input.getDestination());
        product.setThumbnailUrl(input.getThumbnailUrl());
        product.setUpdatedBy(userId);

        return ProductDto.from(product);
    }

    public void deleteProduct(Long id, Long userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));

        // 권한 확인
        if (product.getCreatedBy() != userId) {
            throw new RuntimeException("권한이 없습니다.");
        }

        productRepository.delete(product);
    }

    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));

        if (product.getEndTime().isBefore(LocalDateTime.now())) {
            product.setStatus(GroupBuyingStatus.EXPIRE);
        }

        return ProductDto.from(product);
    }

    public ProductListDto getProductList(String query, Pageable pageable) {
        Page<Product> page = productRepository.findByNameContains(query, pageable);
        List<Product> products = new ArrayList<>();

        for (Product product : page.getContent()) {
            if (product.getEndTime().isBefore(LocalDateTime.now())) {
                product.setStatus(GroupBuyingStatus.EXPIRE);
            }
            products.add(product);
        }

        return ProductListDto.builder()
                .totalCount(page.getTotalElements())
                .elements(products.stream()
                        .map(ProductDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

    public ProductDto joinProduct(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        if (productUserRepository.existsByProductAndUser(product, user)) {
            throw new RuntimeException("이미 참여한 공동구매입니다.");
        }

        if (product.getStatus() == GroupBuyingStatus.EXPIRE ||
                product.getEndTime().isBefore(LocalDateTime.now())) {
            product.setStatus(GroupBuyingStatus.EXPIRE); // 동작안함
            throw new RuntimeException("만료된 공동구매입니다.");
        }

        if (product.getStatus() != GroupBuyingStatus.OPEN) {
            throw new RuntimeException("참여할 수 없는 공동구매입니다.");
        }

        ProductUser productUser = ProductUser.builder()
                .product(product)
                .user(user)
                .type(GroupBuyingUserType.MEMBER)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        productUserRepository.save(productUser);

        // 진행자에게 [공동구매 참여] 알림 전송
        NoticeDto message = NoticeDto.builder()
                .object("PRODUCT")
                .title(GROUP_BUYING_JOIN_01.getTitle())
                .content(GROUP_BUYING_JOIN_01.getContent())
                .value1(user.getName())
                .value2(product.getName())
                .build();

        noticeService.sendNotice(message, product.getUser().getId());

        return ProductDto.from(product);
    }

    public ProductDto cancelProduct(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        ProductUser productUser = productUserRepository.findByProductIdAndUserId(product.getId(), user.getId())
                .orElseThrow(() -> new NoSuchElementException("참여하지 않은 공동구매입니다."));

        if (productUser.getType() == GroupBuyingUserType.MANAGER) {
            throw new RuntimeException("공동구매 진행자는 취소할 수 없습니다.");
        }

        if (product.getStatus() != GroupBuyingStatus.OPEN) {
            throw new RuntimeException("취소할 수 없는 공동구매입니다.");
        }

        productUserRepository.delete(productUser);

        return ProductDto.from(product);
    }

    public ProductDto closeProduct(Long productId, Long userId) {
        List<ProductUser> productUsers = productUserRepository.findAllByProductId(productId);
        Product product = productUsers.get(0).getProduct();

        // 권한 확인
        if (product.getCreatedBy() != userId) {
            throw new RuntimeException("권한이 없습니다.");
        }

        if (product.getStatus() != GroupBuyingStatus.OPEN) {
            throw new RuntimeException("마감할 수 없는 공동구매입니다.");
        }

        int curParticipant = productUsers.size();
        if (curParticipant < product.getMaxParticipant()) {
            throw new RuntimeException("충분한 인원이 모집되지 않았습니다.");
        }

        product.setStatus(GroupBuyingStatus.CLOSE);

        // TODO: 공동구매 참여자들에게 마감 알림 전송
        // ...

        return ProductDto.from(product);
    }

    public ProductDto settleProduct(SettleProductDto input, Long userId) {
        ProductUser productUser = productUserRepository.findByProductIdAndUserId(input.getId(), userId)
                .orElseThrow(() -> new NoSuchElementException());

        Product product = productUser.getProduct();

        // 권한 확인
        if (product.getCreatedBy() != userId) {
            throw new RuntimeException("권한이 없습니다.");
        }

        if (productUser.getType() != GroupBuyingUserType.MANAGER) {
            throw new RuntimeException("공동구매 진행자만 정산을 요청할 수 있습니다.");
        }

        if (product.getStatus() != GroupBuyingStatus.CLOSE) {
            throw new RuntimeException("정산할 수 없는 공동구매입니다.");
        }

        product.setBankName(input.getBankName());
        product.setAccountNumber(input.getAccountNumber());
        product.setTotalAmount(input.getTotalAmount());
        product.setAmount(input.getAmount());
        product.setStatus(GroupBuyingStatus.SETTLE);

        // TODO: 공동구매 참여자들에게 정산 알림 전송
        // ...

        return ProductDto.from(product);
    }

    public ProductDto receiveProduct(ReceiveProductDto input, Long userId) {
        ProductUser productUser = productUserRepository.findByProductIdAndUserId(input.getId(), userId)
                .orElseThrow(() -> new NoSuchElementException());

        Product product = productUser.getProduct();

        // 권한 확인
        if (product.getCreatedBy() != userId) {
            throw new RuntimeException("권한이 없습니다.");
        }

        if (productUser.getType() != GroupBuyingUserType.MANAGER) {
            throw new RuntimeException("공동구매 진행자만 수령을 요청할 수 있습니다.");
        }

        if (product.getStatus() != GroupBuyingStatus.SETTLE) {
            throw new RuntimeException("수령할 수 없는 공동구매입니다.");
        }

        product.setPickupLocation(input.getPickupLocation());
        product.setPickupDatetime(LocalDateTime.parse(input.getPickupDatetime(), DateTimeFormatter.ISO_DATE_TIME));
        product.setStatus(GroupBuyingStatus.RECEIVE);

        // TODO: 공동구매 참여자들에게 수령 알림 전송
        // ...

        return ProductDto.from(product);
    }

    public ProductDto confirmProduct(Long productId, Long userId) {
        ProductUser productUser = productUserRepository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new NoSuchElementException());

        Product product = productUser.getProduct();

        // 권한 확인
        if (product.getCreatedBy() != userId) {
            throw new RuntimeException("권한이 없습니다.");
        }

        if (productUser.getType() != GroupBuyingUserType.MANAGER) {
            throw new RuntimeException("공동구매 진행자만 완료를 요청할 수 있습니다.");
        }

        if (product.getStatus() != GroupBuyingStatus.RECEIVE) {
            throw new RuntimeException("완료할 수 없는 공동구매입니다.");
        }

        product.setStatus(GroupBuyingStatus.COMPLETE);

        return ProductDto.from(product);
    }
}
