<?php
	include '../connect.php';

	$query = "SELECT SUM(total) AS total, SUM(quantity) AS quantity, 
    MONTH(dates) AS nomor_bulan
    FROM orders
    WHERE dates >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR) 
    GROUP BY nomor_bulan";

	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
        $rows['total'] = $consumer['total'];
		$rows['quantity'] = $consumer['quantity'];
		$rows['nomor_bulan'] = $consumer['nomor_bulan'];
        
		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>