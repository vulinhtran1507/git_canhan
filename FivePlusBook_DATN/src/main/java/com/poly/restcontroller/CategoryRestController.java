package com.poly.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dao.BookDAO;
import com.poly.dao.CategoryDAO;
import com.poly.entity.Book;
import com.poly.entity.Category;
import com.poly.service.CategoryService;
@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categories")
public class CategoryRestController {
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	 BookDAO bookdao;
	
	@Autowired
	CategoryDAO ctdao;

	@GetMapping
	public List<Category> getAll() {
		return categoryService.findAll();
	}

	@GetMapping("{id}")
	public Category getOne(@PathVariable("id") String id) {
		return categoryService.findById(id);
	}
	
	@GetMapping("product/{id}")
	public List<Book> getproductcategori(@PathVariable("id") String id) {
		Category cate = ctdao.findById(id).get();
		return bookdao.findByCategory(cate);
	}

	@PostMapping
	public Category create(@RequestBody Category category) {
		return categoryService.create(category);
	}

	@PutMapping("{id}")
	public Category update(@PathVariable("id") String id, @RequestBody Category category) {
		return categoryService.update(category);
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") String id) {
		categoryService.delete(id);
	}
}
