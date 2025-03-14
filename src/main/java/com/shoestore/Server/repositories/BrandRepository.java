package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Brand findByBrandID(int id);
}
