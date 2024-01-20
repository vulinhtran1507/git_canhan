app.controller("voucher-ctrl", function($scope, $http) {

	$scope.items = [];
	$scope.form = {};
	$scope.roles = {};

	$scope.initialize = function() {
		$http.get('/rest/vouchers').then(function(response) {
			$scope.items = response.data;
			$scope.items.forEach(item => {
				item.startdate = new Date(item.startdate)
				item.enddate = new Date(item.enddate)
			})
		});
	}




	$scope.edit = function(item) {
		$scope.form = angular.copy(item);
		$scope.index = -1;
		window.scrollTo({
			top: 0,
			behavior: 'smooth'
		});
	}

	$scope.reset = function() {
		$scope.form = {
			startdate: new Date(),
			enddate: Date()
		};
		$scope.index = 0;
		window.scrollTo({
			top: 0,
			behavior: 'smooth'
		});
	}

	$scope.imageChanged = function(files) {
		var data = new FormData();
		data.append('file', files[0]);
		$http.post('/rest/upload/voucher', data, {
			transformRequest: angular.identity,
			headers: {
				'Content-Type': undefined
			}
		}).then(resp => {
			$scope.form.image = resp.data.name;
			Swal.fire({
				type: 'success',
				title: 'Thêm ảnh thành công',
				text: '',
				icon: "success",
				showConfirmButton: false,
				timer: 3000
			})
		}).catch(error => {
			Swal.fire({
				type: 'error',
				title: 'Lỗi thêm ảnh',
				text: "",
				icon: "error",
				showConfirmButton: false,
				timer: 3000
			})
			console.log("Error", error);
		})
	}

	$scope.reset_smooth_table = function() {
		$scope.form = {};
		window.scrollTo({
			top: 1000,
			behavior: 'smooth'
		});
	}

	$scope.create = function() {
		var item = angular.copy($scope.form);
		$http.post(`/rest/vouchers`, item).then(resp => {
			$scope.initialize();
			resp.data.createDate = new Date(resp.data.createDate);
			$scope.items.push(resp.data);
			$scope.reset();
			Swal.fire({
				type: 'success',
				title: 'Thêm voucher thành công !',
				text: 'Người dùng được sắp xếp theo tên',
				icon: "success",
				showConfirmButton: false,
				timer: 3000
			})
			$scope.reset_smooth_table();
		}).catch(error => {
			Swal.fire({
				type: 'error',
				title: 'Lỗi thêm voucher !',
				text: 'Vui lòng nhập đầy đủ thông tin',
				icon: "error",
				showConfirmButton: false,
				timer: 3000
			})
			console.log("Lỗi", error);
		})

	}

	// Cập nhật
	$scope.update = function() {
		var item = angular.copy($scope.form);
		$http.put(`/rest/vouchers/${item.voucherid}`, item).then(function(response) {
			var index = $scope.items.findIndex(p => p.voucherid == item.voucherid);
			$scope.items[index] = item;
			Swal.fire({
				type: 'success',
				title: 'Cập nhật voucher thành công !',
				text: 'Thông voucher đã được cập nhật',
				icon: "success",
				showConfirmButton: false,
				timer: 3000
			})
			$scope.reset_smooth_table();
		}).catch(function(error) {
			Swal.fire({
				type: 'error',
				title: 'Lỗi cập nhật voucher !',
				text: "Vui lòng chọn voucher để cập nhật",
				icon: "error",
				showConfirmButton: false,
				timer: 3000
			})
			console.log("Lỗi", err);
		})
	}

	// Xóa
	$scope.delete = function(item) {
		$http.delete(`/rest/vouchers/${item.voucherid}`).then(function(response) {
			var index = $scope.items.findIndex(p => p.voucherid == item.voucherid);
			$scope.items.splice(index, 1);
			$scope.reset();
			Swal.fire({
				title: 'Xóa người dùng!',
				text: "Bạn chắc chắn muốn xóa người dùng này chứ ?",
				type: 'warning',
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Vâng, Tôi đồng ý!'
			}).then(function() {
				Swal.fire(
					'Xóa !',
					'Đã xóa thành công',
					'success'
				);
			})
		}).catch(function(err) {
			Swal.fire({
				type: 'error',
				title: 'Lỗi xóa voucher !',
				text: 'Vui lòng chọn voucher để xóa',
				icon: "error",
				showConfirmButton: false,
				timer: 3000
			})
			console.log("Lỗi", err);
		})
	}

	$scope.pager = {
		page: 0,
		size: 5,
		get items() {
			var start = this.page * this.size;
			return $scope.items.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.items.length / this.size);
		},
		first() {
			this.page = 0;
		},
		prev() {
			this.page--;
			if (this.page < 0) {
				this.last();
			}
		},
		next() {
			this.page++;
			if (this.page >= this.count) {
				this.first();
			}
		},
		last() {
			this.page = this.count - 1;
		}

	}

	$scope.initialize();
})