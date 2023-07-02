<?php
	include '../connect.php';

	$name = $_GET['name'];
	$phone = $_GET['phone'];
	$order_id = $_GET['order_id'];
	$payment_status = $_GET['payment_status'];

	if (!empty($name) && !empty($phone) && !empty($order_id) && !empty($payment_status)) {
					
			$order = "INSERT INTO mid_status (order_id, name, phone, payment_status) VALUES ('$order_id', '$name', '$phone', '$payment_status')";
			$msqlOrder = mysqli_query($conn, $order);

			echo "1";
	}else{
		echo "Semua data harus di isi lengkap";
	}
?>