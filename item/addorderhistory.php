<?php
	include '../connect.php';

	$response = array();

	if (isset($_POST['name']) && isset($_POST['phone']) && isset($_POST['notes']) && isset($_POST['status']) && isset($_POST['dates'])) {

		$name = $_POST['name'];
		$phone = $_POST['phone'];
		$notes = $_POST['notes'];
		$status = $_POST['status'];
        $dates = $_POST['dates'];

		// Query untuk mencari item dengan nama yang sama
		$query = "SELECT * FROM order_history WHERE name = '".$name."'";
		$result = mysqli_query($conn, $query);

		if (mysqli_num_rows($result) > 0) {
			// Item sudah ada, tambahkan quantities-nya
			$row = mysqli_fetch_assoc($result);
			$currentnotes = $row['notes'];
			$newnotes = $currentnotes + $notes;

			// Update quantities pada item yang sudah ada
			$updateQuery = "UPDATE order_history SET notes = '".$newnotes."' WHERE name = '".$name."'";
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
			$insertQuery = "INSERT INTO order_history (name, phone, notes, status, dates) 
			VALUES ('".$name."', '".$phone."', '".$notes."', '".$status."', '".$dates."')";
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
