<?php
	include '../connect.php';

	$name = $_GET['name'];
	$nohp = $_GET['nohp'];
	$quantity = $_GET['quantity'];
	$total = $_GET['total'];
	$notes = $_GET['notes'];
	$dates = $_GET['dates'];
	$status = $_GET['status'];
	$outlet = $_GET['in_outlet'];

	if (!empty($name) && !empty($nohp) && !empty($quantity) && !empty($total) && !empty($notes) && !empty($dates) && !empty($status) && !empty($outlet)) {
					
			$order = "INSERT INTO orders(name, nohp, quantity, total, notes, dates, status, in_outlet) VALUES ('$name', '$nohp', '$quantity', '$total', '$notes', '$dates', '$status', '$outlet')";
			$msqlOrder = mysqli_query($conn, $order);

			echo "1";
	}else{
		echo "Semua data harus di isi lengkap";
	}
?>