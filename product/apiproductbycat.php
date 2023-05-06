<?php
	include '../connect.php';

    $nameCat = $_GET['cat_product'];

	$query = "SELECT * FROM product WHERE cat_product = '".$nameCat."'";
	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	$image = "http://localhost/pos/image/";

	while ($product = mysqli_fetch_assoc($msql)) {
		
		$rows['id'] = $product['id'];
		$rows['name'] = $product['name'];
        $rows['price'] = $product['price'];
        $rows['merk'] = $product['merk'];
		$rows['stock'] = $product['stock'];
        $rows['image'] = $image.$product['image'];
		$rows['description'] = $product['description'];
		$rows['cat_product'] = $product['cat_product'];

		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>