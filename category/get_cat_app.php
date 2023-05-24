<?php
	include '../connect.php';

	$outlet = $_GET['in_outlet'];

	$query = "SELECT * FROM category WHERE in_outlet = '".$outlet."'";
	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
		$rows['id'] = $consumer['id'];
		$rows['name'] = $consumer['name'];
		$rows['in_outlet'] = $consumer['in_outlet'];

		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>