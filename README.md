# Library-SpringBoot-With-Security
# Java-Backend
Self Projects on Java Backend Development (Count=2)

#Library-With-Sping-Security-Added

-> The Library project is about Library Management System where a User can issue a Book or Return. 

-> If the user issues the book, the Admin takes the request to process. 

-> Once the Admin approves, a Transaction is created and the Book is assigned to the student.

-> Upon returning the Book the system checks for fine if any.

-> Necessary APIs are secured to avoid unnecessary interventions from unauthorized users (Be ready to see 401 and 403 error codes now!!!)

**What's New**
All the necessary APIs are made secured and requires authentication for using. Its recommended to use POSTMAN for the API testing.

**Skills:** 
MySQL, Java 8, SpringBoot, Hibernate, JPA, Maven, OOP, Spring Security
Tools/Technologies: IntelliJ Idea IDE, Postman, Git, MySQL Command Line Client

**Some Tips:**
You need to add first admin manually and then it allows other admins to be created.
If you get an error like 'Error creating bean with name 'entityManagerFactory'" then check:
1. Application.properties file
2. Check whether MySQL is running via cmd prompt
