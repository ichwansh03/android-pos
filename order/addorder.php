<?php
	include '../connect.php';

	$name = $_GET['name'];
	$nohp = $_GET['nohp'];
	$quantity = $_GET['quantity'];
	$total = $_GET['total'];
	$notes = $_GET['notes'];
	$dates = $_GET['dates'];
	$status = $_GET['status'];

	if (!empty($name) && !empty($nohp) && !empty($quantity) && !empty($total) && !empty($notes) && !empty($dates) && !empty($status)) {
					
			$order = "INSERT INTO orders(name, nohp, quantity, total, notes, dates, status) VALUES ('$name', '$nohp', '$quantity', '$total', '$notes', '$dates', '$status')";
			$msqlOrder = mysqli_query($conn, $order);

			echo "1";
	}else{
		echo "Semua data harus di isi lengkap";
	}
?>