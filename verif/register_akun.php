<?php
	include '../connect.php';
	$response = array();

	if (isset($_POST['nama_usaha']) && isset($_POST['kat_usaha']) && isset($_POST['alamat_usaha']) && isset($_POST['nama']) && isset($_POST['no_hp']) && isset($_POST['jabatan'])  && isset($_POST['email']) && isset($_POST['no_pin'])) {

		$nama_usaha = $_POST['nama_usaha'];
		$kat_usaha = $_POST['kat_usaha'];
		$alamat_usaha = $_POST['alamat_usaha'];
		$nama = $_POST['nama'];
		$no_hp = $_POST['no_hp'];
		$jabatan = $_POST['jabatan'];
		$email = $_POST['email'];
        $no_pin = $_POST['no_pin'];

		$query = "INSERT INTO register (nama_usaha, kat_usaha, alamat_usaha, nama, no_hp, jabatan, email, no_pin) 
		VALUES ('".$nama_usaha."', '".$kat_usaha."', '".$alamat_usaha."', '".$nama."', '".$no_hp."', '".$jabatan."', '".$email."', '".$no_pin."')";
        
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
			'status' => 'FAILED IN ASSET'
		));
	}

	header('Content-type: application/json');
	echo json_encode($response);
?>
