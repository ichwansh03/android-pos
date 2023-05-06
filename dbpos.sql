/*
SQLyog Community v13.1.9 (64 bit)
MySQL - 10.4.21-MariaDB : Database - dbpos
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`dbpos` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `dbpos`;

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `category` */

insert  into `category`(`id`,`name`) values 
(1,'minuman'),
(3,'Makanan'),
(4,'Jus'),
(5,'Steak');

/*Table structure for table `employee` */

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama_pegawai` varchar(200) NOT NULL,
  `phone_pegawai` varchar(200) NOT NULL,
  `job_pegawai` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `no_pin` varchar(200) NOT NULL,
  `img_pegawai` varchar(200) DEFAULT NULL,
  `id_outlet` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `employee` */

/*Table structure for table `order` */

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama_pelanggan` varchar(200) NOT NULL,
  `nohp_pelanggan` varchar(200) NOT NULL,
  `id_product` varchar(200) NOT NULL,
  `jml_beli` int(11) NOT NULL,
  `catatan` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `order` */

/*Table structure for table `outlet` */

DROP TABLE IF EXISTS `outlet`;

CREATE TABLE `outlet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama_outlet` varchar(200) NOT NULL,
  `alamat` varchar(200) NOT NULL,
  `gambar_outlet` varchar(200) DEFAULT NULL,
  `id_pegawai` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `outlet` */

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `price` varchar(100) NOT NULL,
  `merk` varchar(100) DEFAULT NULL,
  `cat_product` varchar(100) NOT NULL,
  `stock` int(11) DEFAULT NULL,
  `image` varchar(100) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Data for the table `product` */

insert  into `product`(`id`,`name`,`price`,`merk`,`cat_product`,`stock`,`image`,`description`) values 
(3,'Bakso aja','12000','pak budi','2',299,'','enak'),
(4,'Es Cekek','1000','Sisri Gula Batu','3',20,'Screenshot 2023-01-22 060820.jpg','manis'),
(5,'Es Teh','5000','sosro','1',200,'sirup markisa.png','mas'),
(7,'NasiGoreng','10000','gk ada','3',100,'kue semprong new.png','enak');

/*Table structure for table `register` */

DROP TABLE IF EXISTS `register`;

CREATE TABLE `register` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama_usaha` varchar(200) NOT NULL,
  `kat_usaha` varchar(200) NOT NULL,
  `alamat_usaha` varchar(200) NOT NULL,
  `nama` varchar(200) NOT NULL,
  `no_hp` varchar(200) NOT NULL,
  `jabatan` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `no_pin` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `register` */

insert  into `register`(`id`,`nama_usaha`,`kat_usaha`,`alamat_usaha`,`nama`,`no_hp`,`jabatan`,`email`,`no_pin`) values 
(1,'Kasirin','Cafe','Natar','Abdul','084756321234','Owner','abdul@gmail.com','123'),
(2,'ngajiin','kafe','natar','ichwan','09877','owner','ichwansh@gmail.com','123456'),
(3,'Testing','Kafe','Natar','Parko','123456','owner','ichwansh03@gmail.com','123456');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
