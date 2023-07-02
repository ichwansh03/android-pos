<?php
	include '../connect.php';

	$response = array();

	if ($_SERVER['REQUEST_METHOD'] == 'DELETE') {

		// check if the product exists
		$query_select = "SELECT * FROM cart_item";
		$result_select = mysqli_query($conn, $query_select);

		if (mysqli_num_rows($result_select) > 0) {
			// delete the product
			$query_delete = "DELETE FROM cart_item";
			$result_delete = mysqli_query($conn, $query_delete);

			if ($result_delete) {
				array_push($response, array(
					'status' => 'OK'
				));
			} else {
				array_push($response, array(
					'status' => 'FAILED TO DELETE'
				));
			}

		} else {
			array_push($response, array(
				'status' => 'PRODUCT NOT FOUND'
			));
		}

	} else {
		array_push($response, array(
			'status' => 'FAILED'
		));
	}

	header('Content-type: application/json');
	echo json_encode($response);
?>
