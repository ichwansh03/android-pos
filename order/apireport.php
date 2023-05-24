<?php
	include '../connect.php';

	$outlet = $_GET['in_outlet'];

	$query = "SELECT id, name, total, dates FROM orders WHERE in_outlet = '".$outlet."'";
	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
		$rows['id'] = $consumer['id'];
		$rows['name'] = $consumer['name'];
        $rows['total'] = $consumer['total'];
        $rows['dates'] = $consumer['dates'];

		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>