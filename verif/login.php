<?php
	session_start();
	include '../connect.php';

	$email = $_GET['email'];
	$no_pin = $_GET['no_pin'];

	$cek = "SELECT * FROM register WHERE email = '".$email."' AND no_pin = '".$no_pin."'";
	$msql = mysqli_query($conn, $cek);
	$result = mysqli_num_rows($msql);

	if (!empty($email) && !empty($no_pin)) {
		
		if ($result == 0) {
			echo "Email atau password salah";
		}else{
			$_SESSION['email'] = $email;
			$_SESSION['is_login'] = true;
			echo "0";
		}
	}else{
		echo "Semua harus di isi terlebih dahulu";
	}
?>