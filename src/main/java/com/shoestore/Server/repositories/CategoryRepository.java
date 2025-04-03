package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByCategoryID(int id);


}
