# System Overview
TripConnect is a platform that allows customers to search, compare, and discover flight and hotel offers from multiple external providers.

The system does not process payments or manage inventory directly. Instead, it integrates with external providers (airlines, hotel platforms) and redirects customers to the provider website to complete booking and payment.

# System Actors
## 2.1 Customer

A customer can:

- Search flights.
- Search hotels.
- Compare offers.
- Create itineraries.
- Save favorite offers.
- Receive notifications.
- Redirect to external providers for booking.

## 2.2 Administrator

The administrator manages:

- Users.
- External providers.
- System configuration.
- Reports.
- Monitoring.

## 2.3 External Provider

External providers are responsible for:

- Providing flight information.
- Providing hotel information.
- Managing availability.
- Processing payments.
- Confirming reservations.

# Functional Requirements

## Module 1: User Management

User Registration

Customers can create an account.

- Information:

  - Name
  - Email
  - Phone number
  - Password

## Authentication

Users can:

Login.
Logout.
Reset password.

## Profile Management

Users can:

Update personal information.
Manage preferences.
View activity history.

# Module 2: Flight Search Management
## Features

Customers can search available flights.

- Search criteria:

  - Departure airport.
  - Arrival airport.
  - Departure date.
  - Return date.
  - Number of passengers.
  - Cabin class.

## Flight Filtering

Users can filter flights by:

- Price.
- Airline.
- Duration.
- Number of stops.
- Departure time.
- CO2 emissions.

## Flight Details

System displays:

- Airline name.
- Flight number.
- Departure time.
- Arrival time.
- Duration.
- Stops.
- Baggage allowance.
- Price.

# Module 3: Hotel Search Management

## Customers can search hotels.

Search criteria:

- Destination.
- Check-in date.
- Check-out date.
- Number of guests.
- Number of rooms.


## Hotel Information Display

System displays:

- Hotel name.
- Images.
- Location.
- Rating.
- Amenities.
- Available rooms.
- Price.

## Hotel Filtering

Users can filter hotels by:

- Price.
- Rating.
- Location.
- Amenities.
- Property type.

# Module 4: External Provider Integration
Purpose
Connect the system with external providers.

Provider Connection

System connects to external APIs.

Function:
1- Retrieve Flight Data

System retrieves:

- Flight schedules.
- Prices.
- Availability.
- Flight details.

1- Retrieve Hotel Data

System retrieves:

- Hotel information.
- Room availability.
- Prices.
- Amenities.
- 

# Module 5: Offer Comparison

 The system compares offers from multiple providers.

Comparison criteria:

- Price.
- Rating.
- Duration.
- Provider.
- Services.


# Module 6: Itinerary Management
Purpose

 Allow customers to create travel plans.

An itinerary can contain:

- Multiple flights.
- Hotels.
- Activities.
- Features

# Module 7: External Booking Redirect
Purpose

Redirect users to external providers to complete booking and payment.

## Features

- Generate provider booking URL.

- Redirect customer

- Track user clicks


# Module 8: Favorites Management
Features

## Users can save:

- Favorite flights.
- Favorite hotels.


# Module 9: Notification System

## Send notifications:

- Price changes.
- New offers.
- Travel reminders.
- System messages.

# Module 10: Provider Management (Admin)

Admin can:

- Add providers.
- Update provider information.
- Activate/deactivate providers.
- Monitor API status.







