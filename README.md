# Barber Booking System💈

The Barber Booking System is a web application built using Thymeleaf, Spring Boot, JPA, and Spring Security. It allows customers to book appointments with barbers and manage their bookings efficiently. This README file provides an overview of the project, instructions for setting up and running the application, and other relevant information.

## Features

The Barber Booking System includes the following features:

- **User Registration and Authentication**: Customers can create an account, log in, and manage their profile information. Spring Security is used for secure user authentication and authorization.

- **Barber Listings**: Customers can view a list of available barbers, along with their profiles and services offered. The data is retrieved from the database using JPA (Java Persistence API).

- **Appointment Booking**: Customers can select a barber, choose a date and time, and book an appointment for a specific service. The booking information is stored in the database using JPA.

- **Appointment Management**: Customers can view their booked appointments, reschedule or cancel appointments, and receive notifications. The appointment data is managed through CRUD (Create, Read, Update, Delete) operations using JPA.

- **Barber Dashboard**: Barbers can log in to their accounts, manage their profiles, view their appointment schedules, and confirm or reject appointments. Spring Security is used to ensure only authorized barbers can access the dashboard.

## Technologies Used

The Barber Booking System utilizes the following technologies:

- **Thymeleaf**: Thymeleaf is a Java-based templating engine that allows for server-side rendering of dynamic web pages. It integrates seamlessly with Spring Boot and enables the creation of dynamic HTML templates.

- **Spring Boot**: Spring Boot is a Java framework that simplifies the development of web applications. It provides a robust set of features, including dependency management, configuration, and auto-configuration, making it easier to build and deploy applications.

- **JPA (Java Persistence API)**: JPA is a Java specification that provides an object-relational mapping approach for managing relational data in Java applications. It allows for the mapping of Java objects to database tables and provides convenient APIs for performing CRUD operations.

- **Spring Security**: Spring Security is a powerful framework for implementing authentication and authorization in Java applications. It provides a wide range of features, including user authentication, role-based access control, and protection against common security vulnerabilities.

## Prerequisites

Before running the Barber Booking System, ensure you have the following prerequisites installed:

- Java Development Kit (JDK): Install the JDK to run Java applications.

- Maven: Maven is a build automation tool used for managing dependencies and building Java projects. Install Maven to build and run the application.

- MySQL: MySQL is a relational database management system used for storing data. Install MySQL and create a database for the Barber Booking System.

## Getting Started

To set up and run the Barber Booking System, follow these steps:

1. Clone the project repository to your local machine.

2. Open the project in your preferred Integrated Development Environment (IDE).

3. Configure the database connection by modifying the `application.properties` file. Update the database URL, username, and password according to your MySQL configuration.

4. Build the project using Maven to download the dependencies and compile the code.

5. Run the application using your IDE or the command line.

6. Open a web browser and navigate to `http://localhost:8080` to access the Barber Booking System.

7. You can now use the application to register as a customer, browse barbers, book appointments, and manage your bookings.
