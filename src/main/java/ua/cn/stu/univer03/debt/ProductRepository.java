package ua.cn.stu.univer03.debt;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

public interface ProductRepository extends ListCrudRepository<Product, Long> {
    List<Product> findByName(String name);
}
