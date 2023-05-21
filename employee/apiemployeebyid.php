<?php
	include '../connect.php';

	$idUser = $_GET['id'];

	$query = "SELECT * FROM employee WHERE id = '".$idUser."'";
	$msql = mysqli_query($conn, $query);

	$jsonArray = array();

	$image = "http://localhost/pos/image/";

	while ($employee = mysqli_fetch_assoc($msql)) {
		
		$rows['id'] = $employee['id'];
		$rows['name'] = $employee['name'];
        $rows['phone'] = $employee['phone'];
        $rows['email'] = $employee['email'];
        $rows['no_pin'] = $employee['no_pin'];
        $rows['image'] = $image.$employee['image'];

		array_push($jsonArray, $rows);
	}

	echo json_encode($jsonArray, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
?>