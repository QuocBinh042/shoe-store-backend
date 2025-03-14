package com.shoestore.Server.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor

public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productID")
    private int productID;
    @Column(name = "productName", nullable = false)
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 50, message = "Tên sản phẩm không được vượt quá 50 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Tên sản phẩm chỉ được chứa chữ, số và khoảng trắng")
    private String productName;
    @ElementCollection
    @CollectionTable(name = "Product_ImageURL", joinColumns = @JoinColumn(name = "productID"))
    @Column(name = "imageURL")
    private List<String> imageURL;
    @Column(name = "description", nullable = false, length = 100)
    @NotBlank(message = "Mô tả không được để trống")
    @Size(max = 100, message = "Mô tả không được vượt quá 100 ký tự")
    private String description;

    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0", inclusive = false, message = "Giá phải lớn hơn 0")
    private double price;
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brandID")
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryID")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplierID")
    private Supplier supplier;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference("productDetailsReference")
    private List<ProductDetail> productDetails;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "promotionID", nullable = true)
    @JsonIgnore
    private Promotion promotion;


}