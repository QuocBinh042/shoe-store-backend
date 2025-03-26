package com.shoestore.Server.repositories;

import com.shoestore.Server.dto.response.FeaturedProductResponse;
import com.shoestore.Server.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> , JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p JOIN p.productDetails pd WHERE pd.productDetailID = :productDetailID")
    Product findProductByProductDetailId(@Param("productDetailID") int productDetailID);

    @Query("SELECT od.productDetail.product FROM OrderDetail od WHERE od.orderDetailID = :orderDetailId")
    Product findProductByOrderDetailId(int orderDetailId);

    List<Product> findTop10ByCategory_CategoryIDAndProductIDNot(int categoryId,int productId);
    List<Product> findTop10ByBrand_BrandIDAndProductIDNot(int brandId,int productId);
    @Query(value = "SELECT p.productID, p.productName, p.description, CAST(p.price AS DOUBLE) as price, " +
            "CAST(SUM(od.quantity) AS UNSIGNED) as totalQuantity, 0 as viewCount, p.createdAt, " +
            "(SELECT pi.imageURL FROM Product_ImageURL pi WHERE pi.productID = p.productID LIMIT 1) as imageURL " +
            "FROM orderdetail od " +
            "JOIN productdetail pd ON od.productDetailID = pd.productDetailID " +
            "JOIN product p ON pd.productID = p.productID " +
            "GROUP BY p.productID, p.productName, p.description, p.price, p.createdAt " +
            "ORDER BY SUM(od.quantity) DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<Object[]> findTopSellingProducts();
    @Query(value = "SELECT p.productID, p.productName, p.description, CAST(p.price AS DOUBLE) as price, " +
            "0 as totalQuantity, 0 as viewCount, p.createdAt, " +
            "(SELECT pi.imageURL FROM Product_ImageURL pi WHERE pi.productID = p.productID LIMIT 1) as imageURL " +
            "FROM product p " +
            "WHERE p.createdAt >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) " +
            "ORDER BY p.createdAt DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<Object[]> findNewArrivals();


}
