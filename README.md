# Plate2Purpose – Smart Food Donation Platform  
### Desktop (JavaFX) + Mobile (Apache Cordova)

Plate2Purpose is a technology-driven platform that connects food donors, NGOs, and recipients to reduce food waste and ensure surplus food reaches people in need efficiently.

The system is built as a multi-platform solution:
- Desktop application for management and coordination  
- Mobile application for real-time field usage and quick response  


## Problem Statement

Every day, large quantities of food are wasted while many people suffer from hunger.  
Current food donation processes are unorganized, slow, and lack real-time coordination between donors and NGOs.

There is a need for:
- a centralized donation tracking system  
- location-based coordination  
- quick communication between stakeholders  


## Solution Overview

Plate2Purpose provides a digital platform where:

- donors can register surplus food  
- NGOs can accept and manage requests  
- recipients can receive food faster  
- donation flow can be tracked transparently  

The system integrates desktop and mobile interfaces for complete accessibility.


## Platforms

### Desktop Application
- Built using JavaFX  
- Used by NGOs/admin for monitoring and coordination  
- Manages donation records and requests  

### Mobile Application
- Built using Apache Cordova  
- Used by donors and volunteers  
- Enables real-time notifications and location-based donation tracking  


## Key Features

- Donor → NGO connection module  
- Live location mapping (Google Maps integration)  
- QR code generation for donation tracking  
- Notification system for donation acceptance  
- Donation history and records  
- Cross-platform accessibility (Desktop + Mobile)  


## Tech Stack

### Desktop
- Language: Java  
- Framework: JavaFX  

### Mobile
- Framework: Apache Cordova  
- Languages: HTML, CSS, JavaScript  

### Backend / Data
- SQLite / MySQL / File-based storage  
- Google Maps API integration  


## Goals

- Reduce food waste  
- Improve NGO–donor coordination  
- Enable transparent food donation tracking  
- Support UN SDG 2: Zero Hunger  
- Support UN SDG 12: Responsible Consumption  


## How to Run

### Desktop (JavaFX)

1. Clone this repository  
2. Open in NetBeans / IntelliJ  
3. Add JavaFX SDK  
4. Configure database (SQLite/MySQL)  
5. Run main Java file  


### Mobile (Cordova)

1. Install Node.js  
2. Install Cordova  
3. Navigate to /cordova-mobile-app  
4. Run:
   cordova build android  
   cordova run android  


## Project Structure

Plate2Purpose/
 ├── javafx-app/            # Desktop application  
 ├── cordova-mobile-app/    # Mobile application  
 ├── screenshots/  
 └── README.md  


## Impact

This project demonstrates how technology can:

- reduce food waste  
- improve logistics of food donation  
- support social impact initiatives  
- connect communities and NGOs effectively  


## Future Enhancements

- Real-time push notifications  
- AI-based demand prediction  
- Cloud database integration  
- Volunteer tracking system  
- Live donation analytics dashboard  


## Author

Developed as a social-impact technology project to address hunger and food waste through digital innovation.
