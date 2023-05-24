<?php
	include '../connect.php';

	$outlet = $_GET['in_outlet'];

	$query = "SELECT SUM(quantity) AS quantity FROM orders
    WHERE dates >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND in_outlet = '".$outlet."'";

	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
        $rows['quantity'] = $consumer['quantity'];
        
		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>