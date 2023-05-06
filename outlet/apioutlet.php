<?php
	include '../connect.php';

	$query = "SELECT * FROM outlet";
	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	$image = "http://localhost/pos/image/";

	while ($outlet = mysqli_fetch_assoc($msql)) {
		
		$rows['id'] = $outlet['id'];
		$rows['name'] = $outlet['name'];
        $rows['address'] = $outlet['address'];
        $rows['image'] = $image.$outlet['image'];

		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>