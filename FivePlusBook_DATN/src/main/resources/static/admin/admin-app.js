app = angular.module("admin-app", ["ngRoute"]);

app.config(function ($routeProvider) {
	$routeProvider
		.when("/product", {
			templateUrl: "/admin/product/index.html",
			controller: "product-ctrl"
		}).when("/user", {
			templateUrl: "/admin/user/index.html",
			controller: "account-ctrl"
		})
		.when("/statisticalorder", {
			templateUrl: "/admin/statistical/index.html",
			controller: "statisticalorder-ctrl"
		}).when("/order", {
			templateUrl: "/admin/order/index.html",
			controller: "order-ctrl"
		}).when("/author", {
			templateUrl: "/admin/author/index.html",
			controller: "author-ctrl"
		}).when("/category", {
			templateUrl: "/admin/category/index.html",
			controller: "category-ctrl"
		}).when("/nguoimua", {
			templateUrl: "/admin/report/statiscal.html",
			controller: "statistic-ctrl"
		}).when("/truyentranh", {
			templateUrl: "/admin/report/statiscalproduct.html",
			controller: "statisticalProduct-ctrl"
		}).when("/loaitruyen", {
			templateUrl: "/admin/report/statiscalcate.html",
			controller: "statisticalcate-ctrl"
		}).when("/authority", {
			templateUrl: "/admin/authority/index.html",
			controller: "authority-ctrl"
		}).when("/voucher", {
			templateUrl: "/admin/voucher/index.html",
			controller: "voucher-ctrl"
		}).when("/discount", {
			templateUrl: "/admin/discount/index.html",
			controller: "discount-ctrl"
		}).when("/revenue", {
			templateUrl: "/admin/revenue/index.html",
			controller: "revenue-ctrl"
		}).when("/giaodien", {
			templateUrl: "/admin/giaodien/index.html",
			controller: "product-ctrl"
		}).otherwise({
			templateUrl: "/admin/Home.html",
			controller: "statis-ctrl"
		})
})