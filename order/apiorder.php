<?php
	include '../connect.php';

	$outlet = $_GET['in_outlet'];

	$query = "SELECT id, name, nohp, status FROM orders WHERE in_outlet = '".$outlet."'";
	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($consumer = mysqli_fetch_assoc($msql)) {
		
		$rows['id'] = $consumer['id'];
		$rows['name'] = $consumer['name'];
        $rows['nohp'] = $consumer['nohp'];
        $rows['status'] = $consumer['status'];

		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>