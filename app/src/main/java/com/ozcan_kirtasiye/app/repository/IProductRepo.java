package com.ozcan_kirtasiye.app.repository;

import com.ozcan_kirtasiye.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface IProductRepo extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Product findByPrice(Double price);

    //             @Query ("select & from Product where createTime ...")
//            Product findByPriceGreaterThan();
    // gerek yok çünkü jparepo'da var
}
