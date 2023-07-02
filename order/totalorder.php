<?php
	include '../connect.php';

	$outlet = $_GET['in_outlet'];

	$query = "SELECT 
    (SELECT SUM(total) FROM orders WHERE dates = CURDATE() AND in_outlet = '".$outlet."') AS total_harian,
    (SELECT SUM(total) FROM orders WHERE WEEK(dates) = WEEK(CURDATE()) AND in_outlet = '".$outlet."') AS total_mingguan,
    (SELECT SUM(total) FROM orders WHERE MONTH(dates) = MONTH(CURDATE()) AND in_outlet = '".$outlet."') AS total_bulanan 
	GROUP BY total_harian, total_mingguan, total_bulanan";
	
	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
        $rows['total_harian'] = $consumer['total_harian'];
		$rows['total_mingguan'] = $consumer['total_mingguan'];
		$rows['total_bulanan'] = $consumer['total_bulanan'];
        
		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>