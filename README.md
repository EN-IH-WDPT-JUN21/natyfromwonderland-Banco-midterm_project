<img alt="hero image" src="https://github.com/EN-IH-WDPT-JUN21/natyfromwonderland-Banco-midterm_project/blob/master/images/readme%20hero%20image.png">

***Banco*** is a brand new bank offering Checking, Student Checking, Credit Card and Savings accounts. Especially designed for students 
and young professionals looking to get the most of their money.


Use, Class and EER Diagrams
===========================

<img alt="use diagram" src="https://github.com/EN-IH-WDPT-JUN21/natyfromwonderland-Banco-midterm_project/blob/master/images/use%20case%20diagram.png">


<img alt="class diagram" src="https://github.com/EN-IH-WDPT-JUN21/natyfromwonderland-Banco-midterm_project/blob/master/images/class%20diagram%20v2.png">


<img alt="eer diagram" src="https://github.com/EN-IH-WDPT-JUN21/natyfromwonderland-Banco-midterm_project/blob/master/images/eer%20diagmam%20ultimo.png">


Database set-up
===========================

create database banco;

use banco;

CREATE USER 'banker'@'localhost' IDENTIFIED BY '1r0nH@ck3r';

GRANT ALL PRIVILEGES ON \*.\* TO 'banker'@'localhost';

FLUSH PRIVILEGES;


API description
===========================

Different transactions require different level of access that is demonstrated in the table below:

<img alt="access roles" src="https://github.com/EN-IH-WDPT-JUN21/natyfromwonderland-Banco-midterm_project/blob/master/images/roles.png">

***Thank you!***
