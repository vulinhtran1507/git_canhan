app.controller("order-ctrl", function($scope, $http) {

	$scope.items = [];
	$scope.status = [];
	$scope.form = {};

	$scope.initialize = function() {
		$http.get("/rest/order").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.date = new Date(item.createDate)
			})
		});
			$http.get("/rest/status").then(resp => {
			$scope.status = resp.data;
		});
		$http.get("/rest/order/index").then(resp => {
			$scope.list = resp.data;
			console.log(resp.data);
		});		
	}
	 $scope.changeStatus = function (orderId, newStatusId) {
        $http.put("/rest/order/" + orderId + "/status?newStatusId=" + newStatusId)
            .then(function (response) {
                $scope.items.push(response.data); // Sửa lại thành response.data
                // Update interface after successful status change
                for (var i = 0; i < $scope.items.length; i++) {
                    if ($scope.items[i].id === orderId) {
                        $scope.items[i].status.id = newStatusId;
                        break;
                    }
                }
            }).catch(function (error) {
               	$scope.initialize();
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Order status updated successfully',
                    confirmButtonText: 'OK'
                });
            });
    };
    
    
    
    $scope.showOrderDetail = function (orderId) {
        $http.get("/rest/orderDetails/" + orderId)
            .then(function (response) {
                $scope.selectedOrderDetails = response.data;
                $('#orderDetailModal').modal('show'); // Hiển thị modal chứa danh sách sản phẩm
            })
            .catch(function (error) {
                console.error("Error fetching order details:", error);
            });
    };
     $scope.closeModal = function() {
        $("#orderDetailModal").modal("hide");
    };




	$scope.pager = {
		page: 0,
		size: 10,
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
});