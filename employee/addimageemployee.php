<?php
include '../connect.php';
$target_path = dirname(__FILE__).'/../image/';
$_BASE_URL = 'http://192.168.43.8/pos/image/';

if(isset($_FILES['image']['name'])) {

    $image_tmp = $_FILES['image']['tmp_name'];
    $image = basename($_FILES['image']['name']);
    $target_path = $target_path . $image;

    try{
        if(!move_uploaded_file($image_tmp, $target_path)){
            echo json_encode(array('status'=>'KO', 'message'=> 'Image gagal diupload'));
        }else {
            $id = $_POST['id'];

            $sql = "UPDATE employee SET image = '".$image."' WHERE id = '".$id."';";

            if (mysqli_query($conn, $sql)) {
                echo json_encode(array('status'=>'OK', 'message'=>  'Image berhasil diupload'));
            } else {
                echo json_encode(array('status'=>'KO', 'message'=> 'Image gagal diupload'));
            }
    
            mysqli_close($conn);
        } 

    }catch(Exception $e){
        echo json_encode(array('status'=>'KO', 'message'=> $e->getMessage()));
    }
}else {
    echo json_encode(array('status'=>'KO', 'message'=> 'Mohon Masukan Image'));
}
?>
