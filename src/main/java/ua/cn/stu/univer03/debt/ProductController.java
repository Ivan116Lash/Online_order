package ua.cn.stu.univer03.debt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

}
