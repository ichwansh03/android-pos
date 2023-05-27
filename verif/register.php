<?php
	include '../connect.php';

	$email = $_GET['email'];

	$query = "SELECT * FROM register WHERE email = '".$email."'";
	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	while ($register = mysqli_fetch_assoc($msql)) {
		
		$rows['id'] = $register['id'];
		$rows['nama_usaha'] = $register['nama_usaha'];
		$rows['alamat_usaha'] = $register['alamat_usaha'];

		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>