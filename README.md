# Getting Started

## Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-template-engines)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-security)
* [Spring HATEOAS](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-spring-hateoas)

## Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Building a Hypermedia-Driven RESTful Web Service](https://spring.io/guides/gs/rest-hateoas/)

# Deployment guide

## Running application on local machine
1. Cloning repository with use [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=linux):
<strong>File > New > Project from Version Control</strong>. Copy and paste [correct link](https://github.com/pangeon/calc-bill.git) from Github.

2. Check [application.properties](https://github.com/pangeon/calc-bill/blob/master/src/main/resources/application.properties) 
file on location calc-bill/src/main/resources. You need database engine and software, in this case [MySQL](https://www.mysql.com/). 
Application used relation database: you need to create a new database according to the parameters in the file.
    - db name: CALC_BILL
    - db host: localhost
    - db user: cecherz
    - db password: Pangeon66#

3. Folder calc-bill/src/main/resources/static/sql consist:
    - SQL db create script: [create_db.sql](https://github.com/pangeon/calc-bill/blob/master/src/main/resources/static/sql/create_db.sql)
    - SQL db insert sample data script: [insert.sql](https://github.com/pangeon/calc-bill/blob/master/src/main/resources/static/sql/insert.sql)
    
The application uses ORM, it is not necessary to generate a database structure after it is created, all you need is 
a running environment and an empty database.

## Config database on local machine

<ol>
<li>
    Create a new MySQL database via console or GUI:<br />
    <code>
    CREATE DATABASE CALC_BILL CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci;
    </code>  
</li>
<li>
    create a new MySQL user account<br />
    <code>
    CREATE USER 'cecherz'@'localhost' IDENTIFIED BY 'Pangeon66#';
    </code>
</li>
<li>
    Grand all privileges to a user account over a specific database<br />   
    <code>
    GRANT ALL PRIVILEGES ON CALC_BILL.* TO 'cecherz'@'localhost';
    </code>
</li>          
</ol>

# Database CALC_BILL

## Structure:
* owner

        +---------+---------+------+-----+---------+----------------+
        | Field   | Type    | Null | Key | Default | Extra          |
        +---------+---------+------+-----+---------+----------------+
        | id      | int(11) | NO   | PRI | NULL    | auto_increment |
        | name    | text    | NO   |     | NULL    |                |
        | surname | text    | NO   |     | NULL    |                |
        +---------+---------+------+-----+---------+----------------+
        
* payments

        +----------+-----------+------+-----+-------------------+-------------------+
        | Field    | Type      | Null | Key | Default           | Extra             |
        +----------+-----------+------+-----+-------------------+-------------------+
        | id       | int       | NO   | PRI | NULL              | auto_increment    |
        | amount   | double    | NO   |     | NULL              |                   |
        | date     | timestamp | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
        | kind     | tinytext  | NO   |     | NULL              |                   |
        | owner_id | int       | YES  | MUL | NULL              |                   |
        +----------+-----------+------+-----+-------------------+-------------------+

## Relations:
![table diagram](readme-img/model_mysql_workbench.png)

## Diagram class UML
 
## pl.cecherz.calcbill.controller.json.v1
![uml diagram](readme-img/uml-diagram.png)