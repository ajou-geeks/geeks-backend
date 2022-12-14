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
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

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

        Product product = Product.createProduct(input, user);
        productRepository.save(product);

        ProductUser manager = ProductUser.createProductUser(product, user, GroupBuyingUserType.MANAGER);
        productUserRepository.save(manager);

        return getProductDto(product);
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto input, Long userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));

        if (product.getCreatedBy() != userId) throw new RuntimeException("권한이 없습니다.");

        Product.updateProduct(product, input);

        return getProductDto(product);
    }

    public void deleteProduct(Long id, Long userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));

        if (product.getCreatedBy() != userId) throw new RuntimeException("권한이 없습니다.");

        productRepository.delete(product);
    }

    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));

        if (product.getEndTime().isBefore(LocalDateTime.now())) product.setStatus(GroupBuyingStatus.EXPIRE);

        return getProductDto(product);
    }

    public ProductListDto getProductList(String query, Pageable pageable) {
        Page<Product> page = productRepository.findByNameContains(query, pageable);
        List<Product> products = new ArrayList<>();

        for (Product product : page.getContent()) {
            if (product.getEndTime().isBefore(LocalDateTime.now())) product.setStatus(GroupBuyingStatus.EXPIRE);
            products.add(product);
        }

        return ProductListDto.builder()
                .totalCount(page.getTotalElements())
                .elements(products.stream()
                        .map(p -> getProductDto(p))
                        .collect(Collectors.toList()))
                .build();
    }

    public ProductDto joinProduct(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        if (productUserRepository.existsByProductAndUser(product, user)) throw new RuntimeException("이미 참여한 공동구매입니다.");

        if (product.getStatus() == GroupBuyingStatus.EXPIRE || product.getEndTime().isBefore(LocalDateTime.now())) {
//            product.setStatus(GroupBuyingStatus.EXPIRE);
            throw new RuntimeException("만료된 공동구매입니다.");
        }

        if (product.getStatus() != GroupBuyingStatus.OPEN) throw new RuntimeException("참여할 수 없는 공동구매입니다.");

        ProductUser member = ProductUser.createProductUser(product, user, GroupBuyingUserType.MEMBER);
        productUserRepository.save(member);

        // 진행자에게 [공동구매 참여] 알림 전송
        NoticeDto message = NoticeDto.builder()
                .object("PRODUCT")
                .title(GROUP_BUYING_JOIN_01.getTitle())
                .content(GROUP_BUYING_JOIN_01.getContent())
                .value1(user.getName())
                .value2(product.getName())
                .build();

        noticeService.sendNotice(message, product.getUser().getId());

        return getProductDto(product);
    }

    public ProductDto cancelProduct(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        ProductUser member = productUserRepository.findByProductIdAndUserIdAndType(product.getId(), user.getId(), GroupBuyingUserType.MEMBER)
                .orElseThrow(() -> new NoSuchElementException("취소할 수 없는 공동구매입니다."));

        if (product.getStatus() != GroupBuyingStatus.OPEN) throw new RuntimeException("취소할 수 없는 공동구매입니다.");

        productUserRepository.delete(member);

        return getProductDto(product);
    }

    public ProductDto closeProduct(Long productId, Long userId) {
        List<ProductUser> productUsers = productUserRepository.findAllByProductId(productId);
        Product product = productUsers.get(0).getProduct();

        if (product.getCreatedBy() != userId) throw new RuntimeException("권한이 없습니다.");

        if (product.getStatus() != GroupBuyingStatus.OPEN) throw new RuntimeException("마감할 수 없는 공동구매입니다.");

        product.setStatus(GroupBuyingStatus.CLOSE);

        // 진행자에게 [공동구매 마감] 알림 전송
        NoticeDto message1 = NoticeDto.builder()
                .object("PRODUCT")
                .title(GROUP_BUYING_CLOSE_01.getTitle())
                .content(GROUP_BUYING_CLOSE_01.getContent())
                .value1(product.getName())
                .build();

        noticeService.sendNotice(message1, userId);

        // 참여자에게 [공동구매 마감] 알림 전송
        NoticeDto message2 = NoticeDto.builder()
                .object("PRODUCT")
                .title(GROUP_BUYING_CLOSE_02.getTitle())
                .content(GROUP_BUYING_CLOSE_02.getContent())
                .value1(product.getName())
                .build();

        for (ProductUser productUser : productUsers) {
            if (productUser.getType() == GroupBuyingUserType.MEMBER)
                noticeService.sendNotice(message2, productUser.getUser().getId());
        }

        return getProductDto(product);
    }

    public ProductDto settleProduct(SettleProductDto input, Long userId) {
        List<ProductUser> productUsers = productUserRepository.findAllByProductId(input.getId());
        Product product = productUsers.get(0).getProduct();

        if (product.getCreatedBy() != userId) throw new RuntimeException("권한이 없습니다.");

        if (product.getStatus() != GroupBuyingStatus.CLOSE) throw new RuntimeException("정산할 수 없는 공동구매입니다.");

        product.setBankName(input.getBankName());
        product.setAccountNumber(input.getAccountNumber());
        product.setTotalAmount(input.getTotalAmount());
        product.setAmount(input.getAmount());
        product.setStatus(GroupBuyingStatus.SETTLE);

        // 참여자에게 [공동구매 정산] 알림 전송
        NoticeDto message = NoticeDto.builder()
                .object("PRODUCT")
                .title(GROUP_BUYING_SETTLE_01.getTitle())
                .content(GROUP_BUYING_SETTLE_01.getContent())
                .value1(product.getName())
                .value2(product.getBankName())
                .value3(product.getAccountNumber())
                .value4(String.valueOf(product.getAmount()))
                .build();

        for (ProductUser productUser : productUsers) {
            if (productUser.getType() == GroupBuyingUserType.MEMBER)
                noticeService.sendNotice(message, productUser.getUser().getId());
        }

        return getProductDto(product);
    }

    public ProductDto receiveProduct(ReceiveProductDto input, Long userId) {
        List<ProductUser> productUsers = productUserRepository.findAllByProductId(input.getId());
        Product product = productUsers.get(0).getProduct();

        if (product.getCreatedBy() != userId) throw new RuntimeException("권한이 없습니다.");

        if (product.getStatus() != GroupBuyingStatus.SETTLE) throw new RuntimeException("수령할 수 없는 공동구매입니다.");

        product.setPickupLocation(input.getPickupLocation());
        product.setPickupDatetime(LocalDateTime.parse(input.getPickupDatetime(), ISO_DATE_TIME));
        product.setStatus(GroupBuyingStatus.RECEIVE);

        // 참여자에게 [공동구매 수령] 알림 전송
        NoticeDto message = NoticeDto.builder()
                .object("PRODUCT")
                .title(GROUP_BUYING_RECEIVE_01.getTitle())
                .content(GROUP_BUYING_RECEIVE_01.getContent())
                .value1(product.getName())
                .value2(product.getPickupDatetime().format(DateTimeFormatter.ofPattern("Y년 MM월 dd일 HH시 mm분")))
                .value3(product.getPickupLocation())
                .build();

        for (ProductUser productUser : productUsers) {
            if (productUser.getType() == GroupBuyingUserType.MEMBER)
                noticeService.sendNotice(message, productUser.getUser().getId());
        }

        return getProductDto(product);
    }

    public ProductDto confirmProduct(Long productId, Long userId) {
        ProductUser productUser = productUserRepository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new NoSuchElementException());
        Product product = productUser.getProduct();

        if (product.getCreatedBy() != userId) throw new RuntimeException("권한이 없습니다.");

        if (product.getStatus() != GroupBuyingStatus.RECEIVE) throw new RuntimeException("완료할 수 없는 공동구매입니다.");

        product.setStatus(GroupBuyingStatus.COMPLETE);

        return getProductDto(product);
    }

    private ProductDto getProductDto(Product product) {
        ProductDto productDto = ProductDto.from(product);
        productDto.setCurParticipant(productUserRepository.countAllByProductId(product.getId()));

        return productDto;
    }
}
