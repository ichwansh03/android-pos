<?php
	include '../connect.php';

	$outlet = $_GET['in_outlet'];

	$query = "SELECT name, total, quantity FROM orders WHERE dates = CURDATE() AND in_outlet = '".$outlet."'";

	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
		$rows['name'] = $consumer['name'];
        $rows['total'] = $consumer['total'];
		$rows['quantity'] = $consumer['quantity'];
        
		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>