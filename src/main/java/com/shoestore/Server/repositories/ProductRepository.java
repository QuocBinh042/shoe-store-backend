package com.shoestore.Server.repositories;

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
    List<Product> findByProductID(int productID);


    // này của hieu
    @Query("SELECT p FROM Product p WHERE p.productID NOT IN :productIDs")
    List<Product> findByProductIDNotIn(@Param("productIDs") List<Integer> productIDs);


    @Query("SELECT p.imageURL FROM Product p JOIN p.imageURL pi WHERE p.productID = :productID")
    List<String> findImageUrlsByProductID(@Param("productID") int productID);

    @Query("SELECT od.productDetail.product FROM OrderDetail od WHERE od.orderDetailID = :orderDetailId")
    Product findProductByOrderDetailId(int orderDetailId);

    List<Product> findTop10ByCategory_CategoryIDAndProductIDNot(int categoryId,int productId);
    List<Product> findTop10ByBrand_BrandIDAndProductIDNot(int brandId,int productId);
}
