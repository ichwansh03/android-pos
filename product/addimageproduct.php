<?php
include '../connect.php';
$target_path = dirname(__FILE__).'../image/';
$_BASE_URL = 'http://192.168.43.8/pos/image/';

if(isset($_FILES['image']['name'])) {

    $target_path = $target_path . basename($_FILES['image']['name']);

    try{
        if(!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)){
            echo json_encode(array('status'=>'KO', 'message'=> 'Image gagal diupload'));
        }else {
            $id = $_POST['id'];

            $sql = "UPDATE product SET image = '".$_BASE_URL.basename($_FILES['image']['name'])."' WHERE id = $id;";

            if (mysqli_query($con, $sql)) {
                echo json_encode(array('status'=>'OK', 'message'=>  'Image berhasil diupload'));
            } else {
                echo json_encode(array('status'=>'KO', 'message'=> 'Image gagal diupload'));
            }
    
            mysqli_close($con);
        } 

    }catch(Exception $e){
        echo json_encode(array('status'=>'KO', 'message'=> $e->getMessage()));
    }
}else {
    echo json_encode(array('status'=>'KO', 'message'=> 'Mohon Masukan Image'));
}
?>