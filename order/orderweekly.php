<?php
	include '../connect.php';

	$outlet = $_GET['in_outlet'];

	$query = "SELECT SUM(total) AS total, SUM(quantity) AS quantity, 
    WEEK(dates) AS nomor_pekan
    FROM orders
    WHERE dates >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH) AND in_outlet = '".$outlet."' 
    GROUP BY nomor_pekan;";

	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
        $rows['total'] = $consumer['total'];
		$rows['quantity'] = $consumer['quantity'];
		$rows['nomor_pekan'] = $consumer['nomor_pekan'];
        
		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>