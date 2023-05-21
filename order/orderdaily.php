<?php
	include '../connect.php';

	$query = "SELECT SUM(total) AS total, SUM(quantity) AS quantity, dates,
	CASE WEEKDAY(dates)
		WHEN 0 THEN 'Senin'
		WHEN 1 THEN 'Selasa'
		WHEN 2 THEN 'Rabu'
		WHEN 3 THEN 'Kamis'
		WHEN 4 THEN 'Jumat'
		WHEN 5 THEN 'Sabtu'
		WHEN 6 THEN 'Minggu'
	  END AS hari
	FROM orders
	WHERE dates >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) 
	GROUP BY DATE(dates)";

	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
        $rows['total'] = $consumer['total'];
        $rows['quantity'] = $consumer['quantity'];
		$rows['hari'] = $consumer['hari'];
		$rows['dates'] = $consumer['dates'];
        
		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>