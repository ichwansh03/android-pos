<?php
	include '../connect.php';

	$query = "SELECT * FROM order_item";
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