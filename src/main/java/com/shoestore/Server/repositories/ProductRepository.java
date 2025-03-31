package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Product;
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

}
