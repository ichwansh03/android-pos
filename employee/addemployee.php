<?php
	include '../connect.php';

	$response = array();

	if (isset($_POST['name']) && isset($_POST['phone']) && isset($_POST['job']) && isset($_POST['email']) && isset($_POST['no_pin']) && isset($_POST['in_outlet']) && isset($_POST['branch']) && isset($_FILES['image'])) {

		$name = $_POST['name'];
		$phone = $_POST['phone'];
		$job = $_POST['job'];
		$email = $_POST['email'];
		$in_outlet = $_POST['in_outlet'];
		$branch = $_POST['branch'];
		$no_pin = $_POST['no_pin'];
		$image_tmp = $_FILES['image']['tmp_name'];
		$image = $_FILES['image']['name'];

		$query = "INSERT INTO employee (name, phone, job, email, no_pin, image, in_outlet, branch) 
		VALUES ('".$name."', '".$phone."', '".$job."', '".$email."', '".$no_pin."', '".$image."', '".$in_outlet."', '".$branch."')";

		move_uploaded_file($image_tmp, "image/".$image);
		$result = mysqli_query($conn, $query);

		if ($result) {
            array_push($response, array(
                'status' => 'OK'
            ));
        } else {
            array_push($response, array(
                'status' => 'FAILED'
            ));
        }

	} else {
		array_push($response, array(
			'status' => 'FAILED IN ISSET'
		));
	}

	header('Content-type: application/json');
	echo json_encode($response);
?>
