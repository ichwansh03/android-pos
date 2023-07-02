<?php
	include '../connect.php';

	$outlet = $_GET['in_outlet'];

	$query = "SELECT 
    (SELECT SUM(quantity) FROM orders WHERE dates = CURDATE() AND in_outlet = '".$outlet."') AS quantity_harian,
    (SELECT SUM(quantity) FROM orders WHERE WEEK(dates) = WEEK(CURDATE()) AND in_outlet = '".$outlet."') AS quantity_mingguan,
    (SELECT SUM(quantity) FROM orders WHERE MONTH(dates) = MONTH(CURDATE()) AND in_outlet = '".$outlet."') AS quantity_bulanan 
	GROUP BY quantity_harian, quantity_mingguan, quantity_bulanan";

	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
        $rows['quantity_harian'] = $consumer['quantity_harian'];
		$rows['quantity_mingguan'] = $consumer['quantity_mingguan'];
		$rows['quantity_bulanan'] = $consumer['quantity_bulanan'];
        
		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>