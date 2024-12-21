package ua.cn.stu.univer03.debt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Отображение всех продуктов
    @GetMapping("/products")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "products"; // имя шаблона Thymeleaf
    }

    // Отображение конкретного продукта по его ID
    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        return "product"; // имя шаблона для одного продукта
    }
}
