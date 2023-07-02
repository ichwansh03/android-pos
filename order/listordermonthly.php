<?php
	include '../connect.php';

    $outlet = $_GET['in_outlet'];

	$query = "SELECT SUM(total) AS total, SUM(quantity) AS quantity, 
    CASE MONTH(dates)
        WHEN 1 THEN 'Januari'
        WHEN 2 THEN 'Februari'
        WHEN 3 THEN 'Maret'
        WHEN 4 THEN 'April'
        WHEN 5 THEN 'Mei'
        WHEN 6 THEN 'Juni'
        WHEN 7 THEN 'Juli'
        WHEN 8 THEN 'Agustus'
        WHEN 9 THEN 'September'
        WHEN 10 THEN 'Oktober'
        WHEN 11 THEN 'November'
        WHEN 12 THEN 'Desember'
    END AS bulan
FROM orders
WHERE dates >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR) AND in_outlet = '".$outlet."' 
GROUP BY bulan";

	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
        $rows['total'] = $consumer['total'];
		$rows['quantity'] = $consumer['quantity'];
		$rows['bulan'] = $consumer['bulan'];
        
		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>