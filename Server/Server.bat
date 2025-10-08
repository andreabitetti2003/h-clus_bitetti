@echo off 
mysql -u root -p < setup.sql
java -jar Server.jar 8080
pause