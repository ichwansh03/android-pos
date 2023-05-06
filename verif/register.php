<?php
	include '../connect.php';

	$nama_usaha = $_GET['nama_usaha'];
	$kat_usaha = $_GET['kat_usaha'];
	$alamat_usaha = $_GET['alamat_usaha'];
	$nama = $_GET['nama'];
	$no_hp = $_GET['no_hp'];
	$jabatan = $_GET['jabatan'];
	$email = $_GET['email'];
	$no_pin = $_GET['no_pin'];

	$queryRegister = "SELECT * FROM register WHERE email = '".$email."'";

	$msql = mysqli_query($conn, $queryRegister);

	$result = mysqli_num_rows($msql);

	if (!empty($nama_usaha) && !empty($kat_usaha) && !empty($alamat_usaha) && !empty($nama) && !empty($no_hp) && !empty($jabatan) && !empty($email) && !empty($no_pin)) {
		
		if ($result == 0) {
			
			$regis = "INSERT INTO register(nama_usaha, kat_usaha, alamat_usaha, nama, no_hp, jabatan, email, no_pin) VALUES ('$nama_usaha', '$kat_usaha', '$alamat_usaha', '$nama', '$no_hp', '$jabatan', '$email', '$no_pin')";
			$msqlRegis = mysqli_query($conn, $regis);

			echo "1";
		}else{
			echo "Email sudah di gunakan";
		}
	}else{
		echo "Semua data harus di isi lengkap";
	}
?>