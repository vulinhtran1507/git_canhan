package com.poly.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.dao.AccountDAO;
import com.poly.dao.BillDAO;
import com.poly.dao.BillDetailDAO;
import com.poly.dao.BookDAO;
import com.poly.dao.CartDAO;
import com.poly.dao.CartDetailDAO;
import com.poly.dao.CategoryDAO;
import com.poly.dao.EvaluateDAO;
import com.poly.dao.ImageDAO;
import com.poly.dao.ProofreadDAO;
import com.poly.dao.StorageDAO;
import com.poly.dao.UserVoucherDAO;
import com.poly.entity.Account;
import com.poly.entity.Bill;
import com.poly.entity.BillDetail;
import com.poly.entity.Book;
import com.poly.entity.Cart;
import com.poly.entity.CartDetail;
import com.poly.entity.Evaluate;
import com.poly.entity.Image;
import com.poly.entity.OrderGHN;
import com.poly.entity.Proofread;
import com.poly.entity.Storage;
import com.poly.entity.UserVoucher;
import com.poly.service.ProductService;
import com.poly.service.SessionService;

@Controller
public class IndexController {
	@Autowired
	ProductService productService;

	@Autowired
	BookDAO bookdao;

	@Autowired
	SessionService session;

	@Autowired
	ImageDAO imagedao;

	@Autowired
	AccountDAO accdao;

	@Autowired
	ProofreadDAO proodreaddao;

	@Autowired
	CartDAO cartdao;

	@Autowired
	CategoryDAO categoryDAo;

	@Autowired
	CartDetailDAO cddao;

	@Autowired
	EvaluateDAO edao;

	@Autowired
	BillDAO bdao;

	@Autowired
	BillDetailDAO bddao;

	@Autowired
	StorageDAO stdao;

	@Autowired
	UserVoucherDAO ucdao;

	@Autowired
	HttpServletRequest req;

	private final String apiUrl = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";
	private final String token = "0f9ab5f8-89c2-11ee-b1d4-92b443b7a897";
	private final int shopId = 190382;

	@RequestMapping("/")
	public String index(Model model) {
		return "User/index";
	}

	@RequestMapping("/khuyenmai")
	public String khuyenmai(Model model) {
		return "User/Khuyenmai";
	}

	@RequestMapping("/gioithieu")
	public String gioithieu(Model model) {
		return "User/Gioithieu";
	}

	@RequestMapping("/chitetsukien")
	public String sukien(Model model) {
		return "User/tintuc_sukien";
	}

	@RequestMapping("/checkout")
	public String checkout(Model model) {
		return "User/checkout";
	}

	@RequestMapping("/product/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		Book item = productService.findById(id);
		List<Image> image = imagedao.findByBook(item);
		List<Proofread> proofread = proodreaddao.findByBook(item);
		model.addAttribute("item", item);
		model.addAttribute("image", image);
		model.addAttribute("proofread", proofread);
		return "User/book-page";
	}

	@RequestMapping("/evaluate/{id}")
	public String evaluate(Model model, @PathVariable("id") Integer id) {
		// Account acc = accdao.findById(session.get("username")).get();
		Book item = productService.findById(id);
		model.addAttribute("item", item);
		return "User/evaluate";
	}

	@RequestMapping("/add-comments/{id}")
	public String review(Model model, @RequestParam("star") Integer star, @RequestParam("comment") String comment,
			@PathVariable("id") Integer id) {
		Account acc = accdao.findById(session.get("username")).get();
		Book book = productService.findById(id);
		Evaluate check = edao.findByAccountAndBook(acc, book);
		if (check == null) {
			Date date = new Date();
			Evaluate evaluate = new Evaluate();
			evaluate.setAccount(acc);
			evaluate.setBook(book);
			evaluate.setComment(comment);
			evaluate.setStar(star);
			evaluate.setCommentDate(date);
			edao.save(evaluate);
		} else {
			boolean hasReviewed = true;
			model.addAttribute("hasReviewed", hasReviewed);
			return "redirect:/evaluate/" + id;
		}

		return "redirect:/evaluate/19";

	}

	@RequestMapping("/order")
	public String oder(Model model) {
		return "User/order";
	}

	@RequestMapping("/favorite")
	public String favorite() {
		return "User/fav";
	}

	@RequestMapping("/favorite/error")
	public String error() {
		return "redirect:/";
	}

	@RequestMapping("/edit")
	public String edit(Model model) {
		return "User/profile-edit";
	}

	@GetMapping("vnpay/test/{total}")
	public String getTest(Model model, @PathVariable("total") Double total) {
		model.addAttribute("total", total);
		return "User/test";
	}

	@RequestMapping("/voucher")
	public String sanVouceher() {
		return "User/voucher";
	}

	@GetMapping("/vnpay/return")
	public String returnTest(Model model) {
		//OrderGHN orderghn = session.get("order");
		//Account acc = session.get("user");
		long id = 0;
		if (req.getParameter("vnp_ResponseCode").equals("00")) {
			String message = "Giao dịch thành công!";
			model.addAttribute("message", message);
//			RestTemplate restTemplate = new RestTemplate();
//
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("token", token);
//			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
//			headers.set("ShopId", String.valueOf(shopId));
//
//			int total = Integer.parseInt(orderghn.getTotal());
//			ObjectMapper objectMapper = new ObjectMapper();
//			Map<String, Object> data = new HashMap<>();
//			data.put("payment_type_id", 1);
//			data.put("note", orderghn.getNote());
//			data.put("required_note", "KHONGCHOXEMHANG");
//			data.put("from_ward_code", "550108");
//			data.put("return_phone", "0969434926");
//			data.put("return_address", "Ninh Kiều - Cần Thơ");
//			data.put("return_district_id", 1442);
//			data.put("return_ward_code", "20109");
//			data.put("to_name", acc.getFullname());
//			data.put("to_phone", orderghn.getTo_phone());
//			data.put("to_address", orderghn.getTo_address());
//			data.put("to_ward_code", orderghn.getWardcode());
//			data.put("to_district_id", orderghn.getDistrict());
//			data.put("cod_amount", 0);
//			data.put("weight", 200);
//			data.put("length", 3);
//			data.put("width", 18);
//			data.put("height", 19);
//			data.put("cod_failed_amount", 2000);
//			data.put("pick_station_id", 1442);
//			data.put("insurance_value", total);
//			data.put("service_id", 0);
//			data.put("service_type_id", 2);
//			data.put("coupon", null);
//			data.put("items", orderghn.getItems());
//			try {
//				String jsonData = objectMapper.writeValueAsString(data);
//				HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);
//
//				ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
//
//				if (response.getStatusCode() == HttpStatus.OK) {
//					System.out.println("API call successful. Response: " + response.getBody());
//					JsonNode jsonNode = objectMapper.readTree(response.getBody());
//					String orderCode = jsonNode.get("data").get("order_code").asText();
//					order_code = orderCode;
					Bill bill = session.get("billPayment");
					Account acc = bill.getAccount();
					List<BillDetail> details = session.get("detailsPayment");
					bill.setOrdercode(null);
					bdao.save(bill);
					id = bill.getId();
					bddao.saveAll(details);
					UserVoucher usvc = ucdao.findByAccountAndVoucher(bill.getAccount(), bill.getVoucher());
					if (usvc != null) {
						ucdao.delete(usvc);
					}
					Cart cart = cartdao.findByAccount(acc);
					for (BillDetail bdetail : details) {
						Book book = productService.findById(bdetail.getBook().getId());
						Storage storage = stdao.findByBook(book);
						storage.setQuantity(storage.getQuantity() - bdetail.getQuantity());
						stdao.save(storage);
						CartDetail cddetail = cddao.findByCartAndBook(cart, book);
						cddao.deleteById(cddetail.getId());
					}
//				} else {
//					System.out.println("API call failed. Status code: " + response.getStatusCode());
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		} else {
			String message = "Giao dịch thất bại!";
			model.addAttribute("message", message);
		}
		return "redirect:/bill-details/" +id;
	}

	@RequestMapping("/bill-details/{id}")
	public String billDetails(Model model, @PathVariable("id") Long id) {
		Bill bill = bdao.findById(id).get();
		List<BillDetail> bdetails = bddao.findByBill(bill);
		String leadtime_input = bill.getLeadtime();
		Instant instant = Instant.ofEpochSecond(Long.parseLong(leadtime_input));
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Ho_Chi_Minh"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d-M-yyyy")
				.withLocale(new java.util.Locale("vi", "VN"));
		String formattedDate = localDateTime.format(formatter);

		model.addAttribute("leadtime", formattedDate);
		model.addAttribute("bill", bill);
		model.addAttribute("bdetails", bdetails);
		return "User/bill";
	}
}
