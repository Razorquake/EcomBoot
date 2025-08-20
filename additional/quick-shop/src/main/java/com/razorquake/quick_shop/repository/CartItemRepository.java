package com.razorquake.quick_shop.repository;

import com.razorquake.quick_shop.model.CartItem;
import com.razorquake.quick_shop.model.Product;
import com.razorquake.quick_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<CartItem> findAllByUser(User user);

    void deleteByUser(User user);

}
