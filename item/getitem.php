<?php
	include '../connect.php';

	$outlet = $_GET['in_outlet'];

	$query = "SELECT * FROM cart_item WHERE in_outlet = '".$outlet."'";
	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($product = mysqli_fetch_assoc($msql)) {
		
		$rows['id'] = $product['id'];
		$rows['name'] = $product['name'];
        $rows['price'] = $product['price'];
        $rows['quantity'] = $product['quantity'];
		
		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>