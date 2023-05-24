<?php
	include '../connect.php';

	$response = array();

	if (isset($_POST['name']) && isset($_POST['price']) && isset($_POST['quantity']) && isset($_POST['in_outlet'])) {

		$name = $_POST['name'];
		$price = $_POST['price'];
		$quantity = $_POST['quantity'];
		$outlet = $_POST['in_outlet'];

		// Query untuk mencari item dengan nama yang sama
		$query = "SELECT * FROM order_item WHERE name = '".$name."'";
		$result = mysqli_query($conn, $query);

		if (mysqli_num_rows($result) > 0) {
			// Item sudah ada, tambahkan quantities-nya
			$row = mysqli_fetch_assoc($result);
			$currentQuantity = $row['quantity'];
			$newQuantity = $currentQuantity + $quantity;

			// Update quantities pada item yang sudah ada
			$updateQuery = "UPDATE order_item SET quantity = '".$newQuantity."' WHERE name = '".$name."'";
			$updateResult = mysqli_query($conn, $updateQuery);

			if ($updateResult) {
				array_push($response, array(
					'status' => 'OK'
				));
			} else {
				array_push($response, array(
					'status' => 'FAILED'
				));
			}
		} else {
			// Item belum ada, tambahkan item baru ke dalam database
			$insertQuery = "INSERT INTO order_item (name, price, quantity, in_outlet) 
			VALUES ('".$name."', '".$price."', '".$quantity."', '".$outlet."')";
			$insertResult = mysqli_query($conn, $insertQuery);

			if ($insertResult) {
				array_push($response, array(
					'status' => 'OK'
				));
			} else {
				array_push($response, array(
					'status' => 'FAILED'
				));
			}
		}
	} else {
		array_push($response, array(
			'status' => 'FAILED IN ISSET'
		));
	}

	header('Content-type: application/json');
	echo json_encode($response);
?>
