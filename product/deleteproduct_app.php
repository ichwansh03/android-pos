<?php
	include '../connect.php';

	$response = array();

	if ($_SERVER['REQUEST_METHOD'] == 'DELETE' && isset($_GET['id'])) {

		$id = $_GET['id'];

		// check if the product exists
		$query_select = "SELECT * FROM product WHERE id = '".$id."'";
		$result_select = mysqli_query($conn, $query_select);

		if (mysqli_num_rows($result_select) > 0) {
			// delete the product
			$query_delete = "DELETE FROM product WHERE id = '".$id."'";
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
