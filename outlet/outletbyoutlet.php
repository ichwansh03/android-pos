<?php
	include '../connect.php';

    $outlet = $_GET['in_outlet'];

	$query = "SELECT * FROM outlet WHERE in_outlet = '".$outlet."'";
	$msql = mysqli_query($conn, $query);

	$image = "http://localhost/pos/image/";

	$jsonArray = array();

	while ($outlet = mysqli_fetch_assoc($msql)) {
		
		$rows['id'] = $outlet['id'];
		$rows['name'] = $outlet['name'];
        $rows['address'] = $outlet['address'];
        $rows['image'] = $image.$outlet['image'];
        $rows['in_outlet'] = $outlet['in_outlet'];

		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>